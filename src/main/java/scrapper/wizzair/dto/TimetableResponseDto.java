package scrapper.wizzair.dto;

import lombok.Data;

import java.util.List;

@Data
public class TimetableResponseDto {
    private List<FlightDto> outboundFlights;
    private List<FlightDto> returnFlights;
}
