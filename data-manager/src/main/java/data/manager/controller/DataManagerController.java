package data.manager.controller;

import data.manager.dto.FlightDataDto;
import data.manager.dto.FlightDetailsDto;
import data.manager.dto.JobReportDto;
import data.manager.dto.TimetableScrapDto;
import data.manager.model.dao.FlightDao;
import data.manager.model.dao.JobDao;
import data.manager.model.flight.FlightDataParser;
import data.manager.model.flight.FlightHandler;
import data.manager.model.job.JobHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin
@RestController
public class DataManagerController {
    @RequestMapping(value = "/jobs/add", method = RequestMethod.POST)
    public ResponseEntity<?> addJob(@RequestParam("departureStation") String departureStation, @RequestParam("arrivalStation") String arrivalStation) {
        new JobDao()
            .setDepartureStationIATA(departureStation)
            .setArrivalStationIATA(arrivalStation)
            .insert();
        return new ResponseEntity<Object>(HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jobs/get", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<JobDao> getJobs() {    
       return JobDao.selectAll();
    }
    
    @RequestMapping(value = "/jobs/activate", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> activateJob(@RequestParam int jobId) {       
        JobDao.select(jobId).activate();
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jobs/deactivate", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deactivateJob(@RequestParam int jobId) {       
        JobDao.select(jobId).deactivate();
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jobs/delete", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteJob(@RequestParam int jobId) {       
        JobDao.select(jobId).softDelete();
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/flights/getOneMonthData", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE})
    public List<FlightDataDto> getJobData(@RequestParam int jobId, @RequestParam(value = "fromDate", required = false) String fromDate) {   
       if(fromDate == null || fromDate.equals("")) {
           return FlightDataParser.getFlightData(jobId);
       }
       return FlightDataParser.getFlightData(jobId, fromDate);
    }
    
    @RequestMapping(value = "/flights/getSingleFlightDetails", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE})
    public FlightDetailsDto getFlightDetails(@RequestParam int flightId) {    
       return new FlightDetailsDto(FlightDao.select(flightId));
    }

    @PostMapping(value = "/flights/addFlightRecords")
    public ResponseEntity<?> putFlights(@RequestBody TimetableScrapDto timetable, @RequestParam("scrapperName") String scrapperName) {
    	new FlightHandler().parseTimetable(timetable, scrapperName);
        return new ResponseEntity<Object>(HttpStatus.ACCEPTED);
    }
    
    @PostMapping(value = "/jobReport")
    public ResponseEntity<?> jobReport(@RequestBody JobReportDto jobReport) {
        new JobHandler().processJobReport(jobReport);        
        return new ResponseEntity<Object>(HttpStatus.ACCEPTED);
    }
}
