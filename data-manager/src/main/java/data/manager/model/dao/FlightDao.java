package data.manager.model.dao;

import data.manager.db.jdbc.MySqlConnection;
import data.manager.model.dao.exception.IncompleteFlightDataException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
public class FlightDao extends AbstractDao{
    private final static Logger logger = Logger.getLogger(FlightDao.class);
    private int flightId = -1;
    private String departureStationIATA;
    private String arrivalStationIATA;
    private String departureDate;
    private String amount;
    private String currencyId;
    private String priceType;
    private List<String> departureDates;
    private String classOfService;
    private boolean hasMacFlight;
    private String airlineName;
    private String createdDate;
    private String updatedDate;
        
    public FlightDao() {}
    
    public FlightDao(ResultSet rs) throws SQLException {
        this
        .setFlightId(rs.getInt(1))
        .setDepartureStationIATA(AirportDao.select(rs.getInt(2)).getIATA())
        .setArrivalStationIATA(AirportDao.select(rs.getInt(3)).getIATA())
        .setDepartureDate(rs.getString(4))
        .setAmount(rs.getString(5))
        .setCurrencyId(rs.getString(6))
        .setPriceType(rs.getString(7))
        .setDepartureDates(convertStringToList(rs.getString(8)))
        .setClassOfService(rs.getString(9))
        .setHasMacFlight(Boolean.getBoolean(rs.getString(10)))
        .setAirlineName(AirlineDao.select(rs.getInt(11)).getAirlineName())
        .setCreatedDate(rs.getString(12))
        .setUpdatedDate(rs.getString(13));
    }
    
    public static FlightDao select(int flightId) {
        StringBuffer selectQuery = new StringBuffer()
                .append("SELECT * FROM flights WHERE flightId='" + flightId + "'");
        logger.trace(selectQuery);
        ResultSet rs;
        try {
            rs = MySqlConnection.executeQuery(selectQuery.toString());
            rs.first();
            return new FlightDao(rs);          
        } catch (SQLException e) {
            logger.error("There was an issue with processing query", e);
        }       
        return null;        
    }
        
    /**
     * 
     * @param departureStationIATA
     * @param arrivalStationIATA
     * @param fromDate
     * @param toDate
     * @return List of FlightDao objects, empty list if no results was found.
     */
    public static List<FlightDao> select(List<String> departureStationIATA, List<String> arrivalStationIATA, String fromDate, String toDate){
        if(departureStationIATA.isEmpty()) {
            logger.error("departure station list cannot be empty!");
            return null;
        }
        if(arrivalStationIATA.isEmpty()) {
            logger.error("arrival station list cannot be empty!");
            return null;
        }
        
        StringBuffer selectQuery = new StringBuffer()
            .append("SELECT * FROM flights WHERE")
            .append(prepareDepartureStationQueryPiece(departureStationIATA))
            .append(" AND")
            .append(prepareArrivalStationQueryPiece(arrivalStationIATA))
            .append(" AND departureDate between '" + fromDate + "' AND '" + toDate + "'");
        
        logger.trace(selectQuery);
        ResultSet rs;
        List<FlightDao> resultFlights = new ArrayList<FlightDao>();
        try {
            rs = MySqlConnection.executeQuery(selectQuery.toString());
            while(rs.next()) {
                resultFlights.add(new FlightDao(rs));
            }
        } catch (SQLException e) {
            logger.error("There was an issue with processing query", e);
        }
        return resultFlights;  
    }
    
    private static String prepareArrivalStationQueryPiece(List<String> arrivalStationIATA) {
        StringBuffer queryPiece = new StringBuffer();
        queryPiece.append(" (");
        for(int i = 0; i < arrivalStationIATA.size(); i++) {
            queryPiece.append("arrivalStationId=" + AirportDao.select(arrivalStationIATA.get(i)).getAirportId());
            if(i < arrivalStationIATA.size() - 1) {
                queryPiece.append(" OR ");
            }
        }
        queryPiece.append(")");
        return queryPiece.toString();
    }
    
    private static String prepareDepartureStationQueryPiece(List<String> departureStationIATA) {
        StringBuffer queryPiece = new StringBuffer();
        queryPiece.append(" (");
        for(int i = 0; i < departureStationIATA.size(); i++) {
            queryPiece.append(" departureStationId=" + AirportDao.select(departureStationIATA.get(i)).getAirportId());
            if(i < departureStationIATA.size() - 1) {
                queryPiece.append(" OR ");
            }
        }
        queryPiece.append(")");
        return queryPiece.toString();
    }
    
    @Override
    public void insert() {
        if(this.flightId != -1) {
            logger.error("Cannot INSERT an object with valid flightId (" + this.flightId + "). Use UPDATE instead.");
            return;
        }
        StringBuffer insertTableSQL = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String nowDate = sdf.format(new Date());
        insertTableSQL.append("INSERT INTO flights (departureStationId, arrivalStationId, departureDate, amount, currencyId, priceType, departureDates, classOfService, hasMacFlight, airlineId, createdDate, updatedDate) VALUES ")
                        .append("(\"" + AirportDao.select(this.departureStationIATA).getAirportId() + "\"")
                        .append(",\"" + AirportDao.select(this.arrivalStationIATA).getAirportId() + "\"")
                        .append(",\"" + this.departureDate + "\"")
                        .append(",\"" + this.amount + "\"")
                        .append(",\"" + this.currencyId + "\"")
                        .append(",\"" + this.priceType + "\"")
                        .append(",\"" + convertListToString(this.departureDates) + "\"")
                        .append(",\"" + this.classOfService + "\"")
                        .append(",\"" + this.hasMacFlight + "\"")
                        .append(",\"" + AirlineDao.select(this.airlineName).getAirlineId() + "\"")
                        .append(",\"" + nowDate + "\"")
                        .append(",\"" + nowDate + "\"")
                        .append(")");                                   
        executeUpdate(insertTableSQL);      
    }
    
    @Override
    public void update(){
        if(this.flightId == -1) {
            logger.error("Cannot UPDATE an object without valid flightId (" + this.flightId + "). Use INSERT instead.");
            return;
        }
        
        StringBuffer insertTableSQL = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String nowDate = sdf.format(new Date());
        insertTableSQL.append("UPDATE flights ")
                        .append("SET ")
                        .append("amount = \"" + this.amount + "\"")
                        .append(", currencyId = \"" + this.currencyId + "\"")
                        .append(", priceType = \"" + this.priceType + "\"")
                        .append(", departureDates = \"" + convertListToString(this.departureDates) + "\"")
                        .append(", classOfService = \"" + this.classOfService + "\"")
                        .append(", hasMacFlight = \"" + this.hasMacFlight + "\"") 
                        .append(", updatedDate = \"" + nowDate + "\"")
                        .append(" WHERE flightId='" + this.flightId + "'");                       
        executeUpdate(insertTableSQL);
    }
        
    public int isInDB() {
        if(this.departureStationIATA == null) {
            throw new IncompleteFlightDataException("departureStation is missing, cannot check in database.");
        } else if(this.arrivalStationIATA == null){
            throw new IncompleteFlightDataException("arrivalStation is missing, cannot check in database.");
        } else if(this.departureDate == null) {
            throw new IncompleteFlightDataException("departureDate is missing, cannot check in database.");
        } else if(this.airlineName == null) {
            throw new IncompleteFlightDataException("airline is missing, cannot check in database.");
        } 
        FlightDao resultFlight = null;
        try {
            resultFlight = selectIfExist();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (resultFlight == null) ? -1 : resultFlight.getFlightId();
    }
    
    private FlightDao selectIfExist() throws SQLException {
        if (!isCountOne(
                MySqlConnection.executeQuery("SELECT COUNT(*) "
                        + "FROM flights "
                        + "WHERE departureStationId='" + AirportDao.select(this.departureStationIATA).getAirportId() + "' "
                        + "AND arrivalStationId='" + AirportDao.select(this.arrivalStationIATA).getAirportId() + "' "
                        + "AND departureDate='" + this.departureDate + "'"
                        + "AND airlineId='" + AirlineDao.select(this.airlineName).getAirlineId() + "'"))) {
            return null;
        } 
        
        StringBuffer selectSQL = new StringBuffer()
                .append("SELECT * "
                        + "FROM flights "
                        + "WHERE departureStationId='" + AirportDao.select(this.departureStationIATA).getAirportId() + "' "
                        + "AND arrivalStationId='" + AirportDao.select(this.arrivalStationIATA).getAirportId() + "' "
                        + "AND departureDate='" + this.departureDate + "'"
                        + "AND airlineId='" + AirlineDao.select(this.airlineName).getAirlineId() + "'");          
               
        logger.trace(selectSQL);
        ResultSet rs = MySqlConnection.executeQuery(selectSQL.toString());
        rs.first();
        return new FlightDao(rs);
    }
    
    public List<String> convertStringToList(String stringArray) {
        List<String> result = new ArrayList<String>();
        for(String el : stringArray.split(",")) {
            result.add(el);
        }
        return result;
    }
    
    public String convertListToString(List<String> list) {
        StringBuffer result = new StringBuffer();
        for(int i = 0; i < list.size(); i++) {
            result.append(list.get(i));
            if(i < list.size() -1) {
                result.append(", ");
            }
        }
        return result.toString();
    }
}
