package data.manager.dto;

import lombok.Data;

import java.util.List;

@Data
public class TimetableScrapDto {
    private List<FlightDto> outboundFlights;
    private List<FlightDto> returnFlights;
}
