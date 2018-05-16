package data.manager.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FlightDataDto {
    private int outboundFlightId;
    private int inboundFlightId;
    private String outboundFlightDate;
    private String inboundFlightDate;
    private int days;
    private int price;
}
