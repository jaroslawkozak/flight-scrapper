package scrapper.wizzair.task;

import io.restassured.RestAssured;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import scrapper.config.ScrapperConfiguration;
import scrapper.wizzair.datamanager.dto.JobDto;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

@RequiredArgsConstructor
public class UpdateJobsTask extends TimerTask{
    private final static Logger logger = Logger.getLogger(UpdateJobsTask.class);
    private static final String JOB_REQUEST = "http://" + ScrapperConfiguration.getDataManagerHost() + ":" + ScrapperConfiguration.getDataManagerPort() + "/getJobs";
    
    @NonNull
    @SuppressWarnings("unused")
    private List<JobDto> jobs;
    
    @Override
    public void run() {       
        try {
            jobs = new ObjectMapper().readValue(RestAssured.when().get(JOB_REQUEST).body().asString().toString(), new TypeReference<List<JobDto>>(){});         
        } catch (IOException e) {
            logger.error(e.getMessage());
        } 
        logger.debug("Job list have been updated.");
    }

}
