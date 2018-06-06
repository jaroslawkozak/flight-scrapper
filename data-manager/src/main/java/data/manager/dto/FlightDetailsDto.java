package data.manager.dto;

import data.manager.model.dao.AirportDao;
import data.manager.model.dao.CurrencyCodeDao;
import data.manager.model.dao.FlightDao;
import lombok.Data;
import lombok.experimental.Accessors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Data
public class FlightDetailsDto {
    private FlightDao flightData;
    private AirportDao departureAirport;
    private AirportDao arrivalAirport;
    private CurrencyCodeDao currency;
    
    public FlightDetailsDto(FlightDao flightData) {
        this.flightData = flightData;
        this.departureAirport = AirportDao.select(flightData.getDepartureStationIATA());
        this.arrivalAirport = AirportDao.select(flightData.getArrivalStationIATA());
        this.currency = CurrencyCodeDao.selectById(flightData.getCurrencyId());
    }
}
