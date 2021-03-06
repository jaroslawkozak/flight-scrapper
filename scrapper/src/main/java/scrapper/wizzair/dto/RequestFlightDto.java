package scrapper.wizzair.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RequestFlightDto {
    private String departureStation;
    private String arrivalStation;
    private String from;
    private String to;
}
