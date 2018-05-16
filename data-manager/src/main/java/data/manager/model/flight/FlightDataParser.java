package data.manager.model.flight;

import data.manager.dto.FlightDataDto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FlightDataParser {

    public static List<FlightDataDto> getFlightData(String fromDate) {
        return null;
        
    }

    public static List<FlightDataDto> getFlightData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return getFlightData(sdf.format(new Date()));
    }
}
