package scrapper.wizzair.task;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import io.restassured.RestAssured;
import scrapper.config.ScrapperConfiguration;
import scrapper.wizzair.datamanager.dto.JobDto;
import scrapper.wizzair.dto.RequestDto;
import scrapper.wizzair.dto.RequestFlightDto;
import scrapper.wizzair.dto.TimetableResponseDto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

@RequiredArgsConstructor
public class ExecuteJobsTask extends TimerTask {
    private final static Logger logger = Logger.getLogger(UpdateJobsTask.class);
    private static final String JOB_REQUEST = "http://" + ScrapperConfiguration.getDataManagerHost() + ":" + ScrapperConfiguration.getDataManagerPort() + "/getJobs";
    private static final String SCRAP_API_VERSION = "7.12.0";
    private static final String SCRAP_REQUEST = "https://be.wizzair.com/" + SCRAP_API_VERSION + "/Api/search/timetable";
    
    @NonNull
    @SuppressWarnings("unused")
    private List<JobDto> jobs;
    
    @Override
    public void run() {
    	ObjectMapper mapper = new ObjectMapper();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String nowDate = sdf.format(new DateTime());
    	for(JobDto job : jobs) {
            
            
    		
    	       List<RequestFlightDto> requestFlights = new ArrayList<RequestFlightDto>();
    	        requestFlights.add(new RequestFlightDto()
    	                .setDepartureStation(job.getDepartureStationIATA())
    	                .setArrivalStation(job.getArrivalStationIATA())
    	                .setFrom("2018-07-01")
    	                .setTo("2018-07-30"));
    	        requestFlights.add(new RequestFlightDto()
    	                .setDepartureStation("LCA")
    	                .setArrivalStation("KTW")
    	                .setFrom("2018-07-01")
    	                .setTo("2018-07-30"));
    	         
    	        RequestDto request = new RequestDto()
    	                .setFlightList(requestFlights)
    	                .setPriceType("wdc")
    	                .setAdultCount(1)
    	                .setChildCount(0)
    	                .setInfantCount(0);
    	        
    	        String response = RestAssured.given()
    	                .contentType("application/json")
    	                .body(mapper.writeValueAsString(request))
    	                .when()
    	                .post("https://be.wizzair.com/7.12.0/Api/search/timetable")
    	                .asString()
    	                .toString();
    	}
    	
    	
 
        
        
        TimetableResponseDto responseDto = mapper.readValue(response, TimetableResponseDto.class);
        
        RestAssured.given()
                .contentType("application/json")
                .body(response)
                .when()
                .post("http://localhost:7701/recordedFlights?scrapperName=Wizzair")
                .then()
                .assertThat()
                .statusCode(200);  
        //collection.replaceOne()
 
        //collection.insertMany(documents);
        
        
        //collection.
        System.out.println(responseDto);
        
    }

}
