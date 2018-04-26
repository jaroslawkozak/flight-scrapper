package scrapper.wizzair;

import io.restassured.RestAssured;
import org.codehaus.jackson.map.ObjectMapper;
import scrapper.wizzair.dto.TimetableResponseDto;

import java.io.IOException;

public class scrap {

    public static void main(String[] args) throws IOException {
        //RestAssured.when().post(request).then().assertThat().statusCode(200);
        //{"flightList":[{"departureStation":"KTW","arrivalStation":"KUT","from":"2018-04-26","to":"2018-05-06"},{"departureStation":"KUT","arrivalStation":"KTW","from":"2018-04-30","to":"2018-06-03"}],"priceType":"wdc","adultCount":1,"childCount":0,"infantCount":0}
        String response = RestAssured.given().contentType("application/json").body("{\"flightList\":[{\"departureStation\":\"KTW\",\"arrivalStation\":\"KUT\",\"from\":\"2018-05-01\",\"to\":\"2018-05-30\"},{\"departureStation\":\"KUT\",\"arrivalStation\":\"KTW\",\"from\":\"2018-05-01\",\"to\":\"2018-05-30\"}],\"priceType\":\"wdc\",\"adultCount\":1,\"childCount\":0,\"infantCount\":0}")
        .when().post("https://be.wizzair.com/7.11.1/Api/search/timetable").asString().toString();
        
        ObjectMapper mapper = new ObjectMapper();
        TimetableResponseDto responseDto = mapper.readValue(response, TimetableResponseDto.class);
        
        System.out.println(responseDto);
        
        
        //System.out.println(response);
        
        /*String url = "https://wizzair.com/en-gb/flights/timetable/katowice/tenerife-canary-islands#/1/0/1/0/0/2018-05/2018-05";
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc.toString());
        System.out.println("here");
        Elements monthlyTables = doc.getElementsByClass("fare-finder__calendar__days__list");
        System.out.println("here1");
        for(Element monthlyTable : monthlyTables) {
            System.out.println("here2");
            System.out.println(monthlyTable.toString());
            System.out.println();
        }*/
        
    }

}
