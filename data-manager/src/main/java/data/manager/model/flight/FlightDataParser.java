package data.manager.model.flight;

import data.manager.dto.FlightDataDto;
import data.manager.model.dao.FlightDao;
import data.manager.model.dao.JobDao;
import data.manager.utils.DMDateUtils;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightDataParser {
    private final static Logger logger = Logger.getLogger(FlightDataParser.class);
    private final static int MAXIMUM_DAYS_FLIGHT_DURATION = 30;
    
    public static List<FlightDataDto> getFlightData(int jobId, String fromDate){
        
        JobDao job = JobDao.select(jobId);
        
        List<String> stations = new ArrayList<String>();
        stations.add(job.getDepartureStationIATA());
        stations.add(job.getArrivalStationIATA());
              
        String toDate;
        try {
            toDate = DMDateUtils.addMonth(fromDate);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return null;
        }
               
        List<FlightDao> flights = FlightDao.select(stations, stations, fromDate, toDate);
        List<FlightDao> outboundFlights = new ArrayList<FlightDao>();
        List<FlightDao> inboundFlights = new ArrayList<FlightDao>();
        
        for(FlightDao flight : flights) {
            if(job.getDepartureStationIATA().contains(flight.getDepartureStationIATA()) &&
                    job.getArrivalStationIATA().contains(flight.getArrivalStationIATA())){
                outboundFlights.add(flight);
            } else if(job.getArrivalStationIATA().contains(flight.getDepartureStationIATA()) &&
                    job.getDepartureStationIATA().contains(flight.getArrivalStationIATA())){
                inboundFlights.add(flight);
            } else {
                logger.error("Error with stations: departure " + flight.getDepartureStationIATA() + ", arrival " + flight.getArrivalStationIATA());
            }
        }
        flights = null;
                
        List<FlightDataDto> flightDataResults = new ArrayList<FlightDataDto>();
        for(FlightDao outflight : outboundFlights) {
            for(FlightDao inflight : inboundFlights) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdfShort = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date outDate = sdf.parse(outflight.getDepartureDate());
                    Date inDate = sdf.parse(inflight.getDepartureDate());
                    if(outDate.before(inDate) && DMDateUtils.getDaysBetween(outDate, inDate) <= MAXIMUM_DAYS_FLIGHT_DURATION ){
                        FlightDataDto flightData = new FlightDataDto()
                                .setOutboundFlightDate(sdfShort.format(outDate))
                                .setInboundFlightDate(sdfShort.format(inDate))
                                .setOutboundFlightId(outflight.getFlightId())
                                .setInboundFlightId(inflight.getFlightId())
                                .setDays(DMDateUtils.getDaysBetween(outDate, inDate))
                                .setPrice(sumPrices(outflight.getAmount(), inflight.getAmount()));  
                        flightDataResults.add(flightData);
                    }
                } catch (ParseException e) {
                    logger.error(e.getMessage());
                    return null;
                }
            }
        } 
        return flightDataResults;    
    }
        
    private static String sumPrices(String p1, String p2) {
        return Double.toString(Double.parseDouble(p1) + Double.parseDouble(p2));
    }
    
    public static List<FlightDataDto> getFlightData(int jobId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return getFlightData(jobId, sdf.format(new Date()));
    }
}
