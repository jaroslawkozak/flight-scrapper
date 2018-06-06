package data.manager.model.flight;

import data.manager.dto.FlightDto;
import data.manager.dto.TimetableScrapDto;
import data.manager.model.dao.FlightDao;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class FlightHandler {
    public void parseTimetable(TimetableScrapDto timetable, String scrapperName) {
        new Thread(new ParseTimeTableThread(timetable, scrapperName)).run();
    }
    
    @AllArgsConstructor
    private class ParseTimeTableThread implements Runnable{
        private TimetableScrapDto timetable;
        private String scrapperName;
        
        @Override
        public void run() {
            List<FlightDto> flights = new ArrayList<FlightDto>();
            if(timetable.getOutboundFlights() != null) {
                flights.addAll(timetable.getOutboundFlights());
            }
            if(timetable.getReturnFlights() != null) {
                flights.addAll(timetable.getReturnFlights());
            }      
               
            flights.forEach(f -> {
                FlightDao flight = new FlightDao()
                        .setDepartureStationIATA(f.getDepartureStation())
                        .setArrivalStationIATA(f.getArrivalStation())
                        .setDepartureDate(f.getDepartureDate())
                        .setAmount(Double.toString(f.getPrice().getAmount()))
                        .setCurrencyId(f.getPrice().getCurrencyCode())
                        .setPriceType(f.getPriceType())
                        .setDepartureDates(f.getDepartureDates())
                        .setClassOfService(f.getClassOfService())
                        .setHasMacFlight(f.isHasMacFlight())
                        .setAirlineName(scrapperName);

                int flightId;
                if((flightId = flight.isInDB()) == -1) {
                    flight.insert();
                } else {
                    flight.setFlightId(flightId);
                    flight.update();
                }                       
            });     
        }      
    }
}
