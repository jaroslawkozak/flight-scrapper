package data.manager.db.dao;

import data.manager.db.dao.exception.AirportDataException;
import data.manager.db.jdbc.MySqlConnection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class AirportDao extends AbstractDao {
    private final static Logger logger = Logger.getLogger(AirportDao.class);
    private int airportId;
    private String name;
    private String city;
    private String country;
    private String IATA;
    private String ICAO;
    private String latitude;
    private String longitude;
    private String altitude;
    private String timezone;
    private String DST;
    private static Map<String, AirportDao> resultsBufferByIATA = new HashMap<String, AirportDao>();
    private static Map<Integer, AirportDao> resultsBufferById = new HashMap<Integer, AirportDao>();
      
    public AirportDao() {}
    public AirportDao(ResultSet rs) throws SQLException {
        this
            .setAirportId(rs.getInt(1))
            .setName(rs.getString(2))
            .setCity(rs.getString(3))
            .setCountry(rs.getString(4))
            .setIATA(rs.getString(5))
            .setICAO(rs.getString(6))
            .setLatitude(rs.getString(7))
            .setLongitude(rs.getString(8))
            .setAltitude(rs.getString(9))
            .setTimezone(rs.getString(10))
            .setDST(rs.getString(11));
    }
    
    @Override
    public void insert() {
        StringBuffer insertTableSQL = new StringBuffer()
                        .append("INSERT INTO airports (airportId, name, city, country, IATA, ICAO, latitude, longitude, altitude, timezone, DST) VALUES ")
                        .append("(" + this.airportId)
                        .append(",\"" + this.name + "\"")
                        .append(",\"" + this.city + "\"")
                        .append(",\"" + this.country + "\"")
                        .append(",\"" + this.IATA + "\"")
                        .append(",\"" + this.ICAO + "\"")
                        .append(", " + this.latitude)
                        .append(", " + this.longitude)
                        .append(", " + this.altitude)
                        .append(", " + this.timezone)
                        .append(",\"" + this.DST + "\"")
                        .append(")");        
        executeUpdate(insertTableSQL);
    }
    
    public static AirportDao select(String IATA) {
        if(resultsBufferByIATA.containsKey(IATA)) {
            return resultsBufferByIATA.get(IATA);
        }               
         
        try {
            if (!isCountOne(
                    MySqlConnection.executeQuery("SELECT COUNT(*) FROM airports WHERE IATA='" + IATA + "'"))) {
                throw new AirportDataException(
                        "There is an issue with result data, there should be exactly 1 result (IATA='" + IATA + "')");
            }
            StringBuffer selectSQL = new StringBuffer()
                    .append("SELECT * FROM airports WHERE IATA='" + IATA + "'");          
                   
            logger.debug(selectSQL);
            ResultSet rs = MySqlConnection.executeQuery(selectSQL.toString());
            rs.first();
            
            AirportDao result = new AirportDao(rs);

            resultsBufferByIATA.put(IATA, result);
            resultsBufferById.put(result.getAirportId(), result);
            return result; 
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return null; 
    }
    
    public static AirportDao select(int airportId){
        if(resultsBufferById.containsKey(airportId)) {
            return resultsBufferById.get(airportId);
        }                            
            
        try {
            if (!isCountOne(
                    MySqlConnection.executeQuery("SELECT COUNT(*) FROM airports WHERE airportId='" + airportId + "'"))) {
                throw new AirportDataException(
                        "There is an issue with result data, there should be exactly 1 result");
            }
            StringBuffer selectSQL = new StringBuffer()
                    .append("SELECT * FROM airports WHERE airportId='" + airportId + "'");          
                   
            logger.debug(selectSQL);
            ResultSet rs = MySqlConnection.executeQuery(selectSQL.toString());
            rs.first();
            
            AirportDao result = new AirportDao(rs);

            resultsBufferById.put(airportId, result);
            resultsBufferByIATA.put(result.getIATA(), result);
            return result;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
     
    public String toString() {
        return new StringBuffer()
                .append("[")
                .append("airportId: " + airportId + ", ")
                .append("name: " + name + ", ")
                .append("city: " + city + ", ")
                .append("country: " + country + ", ")
                .append("IATA: " + IATA + ", ")
                .append("ICAO: " + ICAO + ", ")
                .append("latitude: " + latitude + ", ")
                .append("longitude: " + longitude + ", ")
                .append("altitude: " + altitude + ", ")
                .append("timezone: " + timezone + ", ")
                .append("DST: " + DST)
                .append("]")
                .toString();
    }
}

