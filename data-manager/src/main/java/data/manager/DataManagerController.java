package data.manager;

import data.manager.db.dao.JobDao;
import data.manager.dto.JobReportDto;
import data.manager.dto.TimetableScrapDto;
import data.manager.dto.response.JobsResponseDto;
import data.manager.flight.FlightHandler;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataManagerController {
    private final static Logger logger = Logger.getLogger(DataManagerController.class);
    @RequestMapping(value = "/addJob", method = RequestMethod.POST)
    public ResponseEntity<?> addJob(@RequestParam("departureStation") String departureStation, @RequestParam("arrivalStation") String arrivalStation) {
        new JobDao()
            .setDepartureStationIATA(departureStation)
            .setArrivalStationIATA(arrivalStation)
            .insert();
        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/getJobs", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<JobDao> getJobs() {    
       return JobDao.selectAll();
    }
    
    @PostMapping(value = "/recordedFlights")
    public ResponseEntity<?> putFlights(@RequestBody TimetableScrapDto timetable, @RequestParam("scrapperName") String scrapperName) {
        new Thread(new FlightHandler(timetable, scrapperName)).run();
        return new ResponseEntity<Object>(HttpStatus.ACCEPTED);
    }
    
    @PostMapping(value = "/jobReport")
    public ResponseEntity<?> jobReport(@RequestBody JobReportDto jobReport) {
        JobDao job = JobDao.select(jobReport.getJobId());
        logger.debug("Job report received for job " + jobReport.getJobId() + " from " + jobReport.getScrapperName() + " scrapper with status " + jobReport.getStatus());
        job.update(jobReport.getStatus());           
        return new ResponseEntity<Object>(HttpStatus.ACCEPTED);
    }
}
