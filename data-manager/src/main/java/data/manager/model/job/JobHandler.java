package data.manager.model.job;

import data.manager.dto.JobReportDto;
import data.manager.model.dao.JobDao;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;

public class JobHandler {
    private final static Logger logger = Logger.getLogger(JobHandler.class);
    
    public void processJobReport(JobReportDto jobReport) {
        new Thread(new JobReportThread(jobReport)).run();
    }
    
    
    @AllArgsConstructor
    private class JobReportThread implements Runnable{
        JobReportDto jobReport;
        
        @Override
        public void run() {
            JobDao job = JobDao.select(jobReport.getJobId());
            logger.debug("Job report received for job " + jobReport.getJobId() + " from " + jobReport.getScrapperName() + " scrapper with status " + jobReport.getStatus());
            job.update(jobReport.getStatus());        
        }
        
    }
}
