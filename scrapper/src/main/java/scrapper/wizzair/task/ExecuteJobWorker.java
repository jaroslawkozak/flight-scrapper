package scrapper.wizzair.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import io.restassured.RestAssured;
import lombok.AllArgsConstructor;
import scrapper.config.ScrapperConfiguration;
import scrapper.wizzair.WizzairScrapperApplication;
import scrapper.wizzair.datamanager.dto.JobDto;
import scrapper.wizzair.datamanager.dto.JobReportDto;
import scrapper.wizzair.datamanager.enums.JobStatus;
import scrapper.wizzair.dto.RequestDto;
import scrapper.wizzair.dto.RequestFlightDto;
import scrapper.wizzair.dto.TimetableResponseDto;

@AllArgsConstructor
public class ExecuteJobWorker implements Runnable{
    private final static Logger logger = Logger.getLogger(ExecuteJobWorker.class);
    private static final String DATA_MANAGER_ADDRESS = "http://" + ScrapperConfiguration.getDataManagerHost() + ":" + ScrapperConfiguration.getDataManagerPort();
    private static final String FLIGHT_RECORD_REQUEST = DATA_MANAGER_ADDRESS + "/recordedFlights?scrapperName=" + WizzairScrapperApplication.SCRAPPER_AIRLINE_NAME;
    private static final String JOB_REPORT_REQUEST = DATA_MANAGER_ADDRESS + "/jobReport";
    private static final String WIZZAIR_METADATA_URL = "https://wizzair.com/static/metadata.json";
    private static final String SCRAP_REQUEST = "/search/timetable";
    //https://wizzair.com/static/metadata.json
    private JobDto job;
    private String from;
    private String to;

    @Override
    public void run() {
        List<RequestFlightDto> requestFlights = prepareRequestFlights();              
        RequestDto request = prepareRequest(requestFlights);   
        String response;
        try {
            response = makeRequestAndGetResponseString(request);
        } catch (IOException e) {              
            logger.error("Job " + job.getJobId() + " failed: " + e.getMessage());
            sendJobReport(job, JobStatus.FAILED);
            return;
        } 

        try {
            validateResponseContent(response);
            sendFlightRecordRequest(response);             
        } catch (IOException e) {             
            logger.error("Job " + job.getJobId() + " failed: " + e.getMessage());
            logger.error("Response: " + response); 
            sendJobReport(job, JobStatus.FAILED);
            return;
        }
        sendJobReport(job, JobStatus.SUCCESS);                   
        logger.debug("Job " + job.getJobId() + " successfull");
        
    }
    
    private void validateResponseContent(String response)
            throws IOException, JsonParseException, JsonMappingException {
        new ObjectMapper().readValue(response, TimetableResponseDto.class);
    }

    private static String getWizzairApiUrl() {
        String response = RestAssured.when()
                .get(WIZZAIR_METADATA_URL)
                .asString()
                .toString();  

        try {
            return ((JSONObject) new JSONParser().parse(response)).get("apiUrl").toString();
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }  
        return null;
    }
    
    private void sendFlightRecordRequest(String response) {
        try {   
            RestAssured.given()
                .contentType("application/json")
                .body(response)
                .when()
                .post(FLIGHT_RECORD_REQUEST)
                .then()
                .assertThat()
                .statusCode(202);
            logger.debug("Sending flight records successfull");
        } catch (AssertionError e) {
            logger.error("Sending flight records failed. Failed response: " + response);
            logger.error(e.getMessage());
        }
    }
    
    private RequestDto prepareRequest(List<RequestFlightDto> requestFlights) {
        return new RequestDto()
                .setFlightList(requestFlights)
                .setPriceType("wdc") //Wizz Discount Club
                .setAdultCount(1)
                .setChildCount(0)
                .setInfantCount(0);
    }

    private List<RequestFlightDto> prepareRequestFlights() {
        List<RequestFlightDto> requestFlights = new ArrayList<RequestFlightDto>();
        requestFlights.add(new RequestFlightDto()
                .setDepartureStation(job.getDepartureStationIATA())
                .setArrivalStation(job.getArrivalStationIATA())
                .setFrom(from)
                .setTo(to));
        requestFlights.add(new RequestFlightDto()
                .setDepartureStation(job.getArrivalStationIATA())
                .setArrivalStation(job.getDepartureStationIATA())
                .setFrom(from)
                .setTo(to));
        return requestFlights;
    }
    
    private String makeRequestAndGetResponseString(RequestDto request)
            throws IOException, JsonGenerationException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        
        return RestAssured.given()
                .contentType("application/json")
                .body(mapper.writeValueAsString(request))
                .when()
                .post(getWizzairApiUrl() + SCRAP_REQUEST)
                .asString()
                .toString();
    }
    
    private void sendJobReport(JobDto job, JobStatus status) {     
        try {
            RestAssured.given()
            .contentType("application/json")
            .body(getJobReportBody(job, status))
            .when()
            .post(JOB_REPORT_REQUEST)
            .then()
            .assertThat()
            .statusCode(202);
            logger.debug("Sending job " + job.getJobId() + " report successfull");
        } catch (Exception e) {
            logger.debug("Sending job " + job.getJobId() + " report failed");
            logger.error(e.getMessage());
        }
    }

    private String getJobReportBody(JobDto job, JobStatus status) {
        try {
            return new ObjectMapper().writeValueAsString(new JobReportDto()
                    .setJobId(job.getJobId())
                    .setScrapperName(WizzairScrapperApplication.SCRAPPER_AIRLINE_NAME)
                    .setStatus(status));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
