package scrapper.wizzair;

import scrapper.wizzair.datamanager.dto.JobDto;
import scrapper.wizzair.task.ExecuteJobsTask;
import scrapper.wizzair.task.UpdateJobsTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

public class WizzairScrapperApplication {
    public static final String SCRAPPER_AIRLINE_NAME = "Wizzair";    
    private static List<JobDto> jobs = Collections.synchronizedList(new ArrayList<JobDto>());
    
    public static void main (String[] args) {
        Timer updateJobsTimer = new Timer(true);
        updateJobsTimer.scheduleAtFixedRate(new UpdateJobsTask(jobs), 0, 10*60*1000);

        Timer executeJobsTimer = new Timer(true);
        executeJobsTimer.scheduleAtFixedRate(new ExecuteJobsTask(jobs), 30*1000, 10*60*1000);

        while(true) {}
    }
}
