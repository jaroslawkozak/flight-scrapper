package data.manager;

import data.manager.dto.TimetableScrapDto;
import data.manager.dto.response.JobsResponseDto;
import data.manager.flight.FlightHandler;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataManagerController {
    @RequestMapping(value = "/addJob", method = RequestMethod.POST)
    public JobsResponseDto addJob(@RequestParam("departureStation") String departureStation, @RequestParam("arrivalStation") String arrivalStation) {
       return null;
    }
    
    @RequestMapping(value = "/getJobs", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE})
    public JobsResponseDto getJobs() {
       return null;
    }
    
    @PostMapping(value = "/recordedFlights")
    public void putFlights(@RequestBody TimetableScrapDto timetable, @RequestParam("scrapperName") String scrapperName) {
        FlightHandler.parseTimetable(timetable, scrapperName);
    }
    
    
}
