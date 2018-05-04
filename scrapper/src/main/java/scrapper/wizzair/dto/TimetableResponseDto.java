package scrapper.wizzair.dto;

import lombok.Data;

import java.util.List;

@Data
public class TimetableResponseDto {
    private List<ResponseFlightDto> outboundFlights;
    private List<ResponseFlightDto> returnFlights;
}
