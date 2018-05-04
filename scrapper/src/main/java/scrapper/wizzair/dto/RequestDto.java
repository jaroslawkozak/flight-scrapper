package scrapper.wizzair.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RequestDto {
    private List<RequestFlightDto> flightList;
    private String priceType;
    private int adultCount;
    private int childCount;
    private int infantCount;
}
