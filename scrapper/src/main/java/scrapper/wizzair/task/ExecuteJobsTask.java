package scrapper.wizzair.task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import scrapper.wizzair.WizzairScrapperApplication;
import scrapper.wizzair.datamanager.dto.JobDto;

@Component
@EnableScheduling
public class ExecuteJobsTask extends TimerTask {
    private final static Logger logger = Logger.getLogger(ExecuteJobsTask.class);
    
    private List<JobDto> jobs = WizzairScrapperApplication.jobs;
    
    @Override
    @Scheduled(fixedDelay = 600000, initialDelay = 10000)
    public void run() { 
        logger.debug("Running all jobs. Jobs found: " + jobs.size());
    	for(JobDto job : jobs) {   
    	    if(job.getIsActive() == 0) { continue;}
    	    final int THREAD_POOL = 12;
    	    
    	    /*for(int i = 0; i < THREAD_POOL ; i++) {
    	        String from = getFirstDayOfMonthDate(i);
                String to = getLastDayOfMonthDate(i);
    	        new ExecuteJobWorker(job, from, to).run();
    	    }*/
    	    
    	    
    	    ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL);
   	    
    	    for(int i = 0; i < THREAD_POOL ; i++) {
    	        String from = getFirstDayOfMonthDate(i);
    	        String to = getLastDayOfMonthDate(i);
    	        logger.debug("Starting sub-jobs with dates from: " + from + " to: " + to);
    	        executor.execute(new ExecuteJobWorker(job, from, to));
    	    }
    	    executor.shutdown();
            while (!executor.isTerminated()) {}
    	}
    }

    private String getFirstDayOfMonthDate(int plusMonths) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateTime workingDate = DateTime.now();
        if(plusMonths < 0) {
            logger.error("Incorrect plusMonths value: " + plusMonths);
            return null;
        }
        
        if(plusMonths == 0) {
            return sdf.format(workingDate.toDate());
        } else {
            return sdf.format(workingDate.plusMonths(plusMonths).withDayOfMonth(1).toDate());
        }
    }
    
    private String getLastDayOfMonthDate(int plusMonths) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateTime workingDate = DateTime.now();
        if(plusMonths < 0) {
            logger.error("Incorrect plusMonths value: " + plusMonths);
            return null;
        }
        workingDate = workingDate.plusMonths(plusMonths);
        return sdf.format(workingDate.dayOfMonth().withMaximumValue().toDate());   
    }
}
