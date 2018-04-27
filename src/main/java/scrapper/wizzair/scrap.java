package scrapper.wizzair;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.ValidationOptions;
import com.mongodb.util.JSON;
import io.restassured.RestAssured;
import org.bson.Document;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import scrapper.wizzair.dto.RequestDto;
import scrapper.wizzair.dto.RequestFlightDto;
import scrapper.wizzair.dto.ResponseFlightDto;
import scrapper.wizzair.dto.TimetableResponseDto;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class scrap {

    public static void main(String[] args) throws IOException, ParseException {
        //RestAssured.when().post(request).then().assertThat().statusCode(200);
        //{"flightList":[{"departureStation":"KTW","arrivalStation":"KUT","from":"2018-04-26","to":"2018-05-06"},{"departureStation":"KUT","arrivalStation":"KTW","from":"2018-04-30","to":"2018-06-03"}],"priceType":"wdc","adultCount":1,"childCount":0,"infantCount":0}
        //"{\"flightList\":[{\"departureStation\":\"KTW\",\"arrivalStation\":\"KUT\",\"from\":\"2018-05-01\",\"to\":\"2018-05-30\"},{\"departureStation\":\"KUT\",\"arrivalStation\":\"KTW\",\"from\":\"2018-05-01\",\"to\":\"2018-05-30\"}],\"priceType\":\"wdc\",\"adultCount\":1,\"childCount\":0,\"infantCount\":0}"
        MongoClient mongo = new MongoClient();
        MongoDatabase database = mongo.getDatabase("flight-scrapper");
        //new ValidationOptions().
        //new CreateCollectionOptions().validationOptions(validationOptions)
        //database.createCollection(collectionName, createCollectionOptions);
        MongoCollection collection = database.getCollection("flights");
        ObjectMapper mapper = new ObjectMapper();
        
        List<RequestFlightDto> requestFlights = new ArrayList<RequestFlightDto>();
        requestFlights.add(new RequestFlightDto()
                .setDepartureStation("KTW")
                .setArrivalStation("TFS")
                .setFrom("2018-05-01")
                .setTo("2018-05-30"));
        requestFlights.add(new RequestFlightDto()
                .setDepartureStation("TFS")
                .setArrivalStation("KTW")
                .setFrom("2018-05-01")
                .setTo("2018-05-30"));
         
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
        
        List<ResponseFlightDto> flights = new ArrayList<ResponseFlightDto>(); 
        flights.addAll(responseDto.getOutboundFlights());
        flights.addAll(responseDto.getReturnFlights());
           
        //DBObject dbObject = BasicDBObject.parse(mapper.writeValueAsString(flights));

        List<Document> documents = new ArrayList<Document>();
        
        flights.forEach(f -> {
                
                try {
                    System.out.println(mapper.writeValueAsString(f));
                    documents.add(new Document((BasicDBObject.parse(mapper.writeValueAsString(f))).toMap()));
                } catch (JsonGenerationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
     
        });
        //collection.replaceOne()
 
        collection.insertMany(documents);
        
        
        //collection.
        System.out.println(responseDto);
        
        
    
        
    }

}
