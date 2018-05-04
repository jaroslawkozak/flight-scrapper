package data.manager.flight;

import data.manager.db.dao.FlightDao;
import data.manager.db.jdbc.MySqlConnection;
import data.manager.dto.FlightDto;
import data.manager.dto.TimetableScrapDto;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlightHandler {
    public static void parseTimetable(TimetableScrapDto timetable, String scrapperName) {
        List<FlightDto> flights = new ArrayList<FlightDto>(); 
        flights.addAll(timetable.getOutboundFlights());
        flights.addAll(timetable.getReturnFlights());
           
        ObjectMapper mapper = new ObjectMapper();
        
        try(Connection connection = MySqlConnection.getConnection()){
            flights.forEach(f -> {
                try {
                    System.out.println(mapper.writeValueAsString(f));
                    
                    FlightDao flight = new FlightDao()
                            .setDepartureStationIATA(f.getDepartureStation())
                            .setArrivalStationIATA(f.getArrivalStation())
                            .setDepartureDate(f.getDepartureDate())
                            .setAmount(Double.toString(f.getPrice().getAmount()))
                            .setCurrencyCode(f.getPrice().getCurrencyCode())
                            .setPriceType(f.getPriceType())
                            .setDepartureDates(f.getDepartureDates())
                            .setClassOfService(f.getClassOfService())
                            .setHasMacFlight(f.isHasMacFlight())
                            .setAirlineName(scrapperName);

                    int flightId;
                    if((flightId = flight.isInDB(connection)) == -1) {
                        flight.insert(connection);
                    } else {
                        flight.setFlightId(flightId);
                        flight.update(connection);
                    }                       
                } catch (IOException|SQLException e) {
                    e.printStackTrace();
                } 
            });
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }    
    }
}
