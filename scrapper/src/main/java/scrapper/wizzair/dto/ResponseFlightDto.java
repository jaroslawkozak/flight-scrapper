package scrapper.wizzair.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseFlightDto {
    private String departureStation;
    private String arrivalStation;
    private String departureDate;
    private PriceDto price;
    private String priceType;
    private List<String> departureDates;
    private String classOfService;
    private boolean hasMacFlight;
    
    public String getDepartureDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            return sdf.format(sdf.parse(departureDate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
