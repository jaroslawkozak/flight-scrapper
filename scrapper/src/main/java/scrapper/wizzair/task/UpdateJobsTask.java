package scrapper.wizzair.task;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.restassured.RestAssured;
import scrapper.config.ScrapperConfiguration;
import scrapper.wizzair.WizzairScrapperApplication;
import scrapper.wizzair.datamanager.dto.JobDto;

@Component
@EnableScheduling
public class UpdateJobsTask extends TimerTask{
    private final static Logger logger = Logger.getLogger(UpdateJobsTask.class);
    private static final String JOB_REQUEST = "http://" + ScrapperConfiguration.getDataManagerHost() + ":" + ScrapperConfiguration.getDataManagerPort() + "/getJobs";
    
    private List<JobDto> jobs = WizzairScrapperApplication.jobs;
    
    @Override
    @Scheduled(fixedDelay = 600000, initialDelay = 0)
    public void run() {       
        try {
            List<JobDto> results = new ObjectMapper().readValue(RestAssured.when().get(JOB_REQUEST).body().asString().toString(), new TypeReference<List<JobDto>>(){});
            if(results != null && results.size() > 0) {
                jobs.clear();
                jobs.addAll(results); 
            }         
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("Job list update failed.");
            return;
        } 
        logger.debug("Job list have been updated.");
    }

}
