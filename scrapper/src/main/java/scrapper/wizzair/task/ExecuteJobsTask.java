package scrapper.wizzair.task;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import scrapper.config.ScrapperConfiguration;
import scrapper.wizzair.datamanager.dto.JobDto;

import java.util.List;
import java.util.TimerTask;

@RequiredArgsConstructor
public class ExecuteJobsTask extends TimerTask {
    private final static Logger logger = Logger.getLogger(UpdateJobsTask.class);
    private static final String JOB_REQUEST = "http://" + ScrapperConfiguration.getDataManagerHost() + ":" + ScrapperConfiguration.getDataManagerPort() + "/getJobs";
    
    @NonNull
    @SuppressWarnings("unused")
    private List<JobDto> jobs;
    
    @Override
    public void run() {
        
        
    }

}
