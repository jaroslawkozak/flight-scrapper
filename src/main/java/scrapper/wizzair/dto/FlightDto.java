package scrapper.wizzair.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FlightDto {
    private String departureStation;
    private String arrivalStation;
    private Date departureDate;
    private PriceDto price;
    private String priceType;
    private List<Date> departureDates;
    private String classOfService;
    private boolean hasMacFlight;
    
}
