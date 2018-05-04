package scrapper.wizzair;

import io.restassured.RestAssured;
import org.codehaus.jackson.map.ObjectMapper;
import scrapper.wizzair.dto.RequestDto;
import scrapper.wizzair.dto.RequestFlightDto;
import scrapper.wizzair.dto.TimetableResponseDto;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class scrap {
        
    public static void main(String[] args) throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        
        List<RequestFlightDto> requestFlights = new ArrayList<RequestFlightDto>();
        requestFlights.add(new RequestFlightDto()
                .setDepartureStation("KTW")
                .setArrivalStation("LCA")
                .setFrom("2018-07-01")
                .setTo("2018-07-30"));
        requestFlights.add(new RequestFlightDto()
                .setDepartureStation("LCA")
                .setArrivalStation("KTW")
                .setFrom("2018-07-01")
                .setTo("2018-07-30"));
         
        RequestDto request = new RequestDto()
                .setFlightList(requestFlights)
                .setPriceType("wdc")
                .setAdultCount(1)
                .setChildCount(0)
                .setInfantCount(0);
        
        String response = RestAssured.given()
                .contentType("application/json")
                .body(mapper.writeValueAsString(request))
                .when()
                .post("https://be.wizzair.com/7.11.1/Api/search/timetable")
                .asString()
                .toString();
        
        
        TimetableResponseDto responseDto = mapper.readValue(response, TimetableResponseDto.class);
        
        RestAssured.given()
                .contentType("application/json")
                .body(response)
                .when()
                .post("http://localhost:7701/recordedFlights?scrapperName=Wizzair")
                .then()
                .assertThat()
                .statusCode(200);  
        //collection.replaceOne()
 
        //collection.insertMany(documents);
        
        
        //collection.
        System.out.println(responseDto);
        
        
    
        
    }

}
