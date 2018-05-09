package data.manager.db.dao;

import data.manager.db.dao.exception.CurrencyDataException;
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
@EqualsAndHashCode(callSuper = false)
public class AirlineDao extends AbstractDao {
    private final static Logger logger = Logger.getLogger(AirlineDao.class);
    private int airlineId;
    private String airlineName;
    private static Map<Integer, AirlineDao> resultsBufferById = new HashMap<Integer,AirlineDao>();
    private static Map<String, AirlineDao> resultsBufferByName = new HashMap<String,AirlineDao>();

    public AirlineDao() {}
    public AirlineDao(ResultSet rs) throws SQLException {
        this
        .setAirlineId(rs.getInt(1))
        .setAirlineName(rs.getString(2));
    }
    
    @Override
    public void insert() {
        StringBuffer insertTableSQL = new StringBuffer()
                .append("INSERT INTO airlines (airlineId, airlineName) VALUES ")
                .append("(" + this.airlineId)
                .append(",\"" + this.airlineName + "\"")
                .append(")");                  
        executeUpdate(insertTableSQL);   
    }

    public static AirlineDao select(String airlineName) {
        if(resultsBufferByName.containsKey(airlineName)) {
            return resultsBufferByName.get(airlineName);
        }

        try {
            if (!isCountOne(
                    MySqlConnection.executeQuery("SELECT COUNT(*) FROM airlines WHERE airlineName='"
                            + airlineName + "'"))) {
                throw new CurrencyDataException(
                        "There is an issue with result data, there should be exactly 1 result");
            }

            StringBuffer selectSQL = new StringBuffer().append(
                    "SELECT * FROM airlines WHERE airlineName='" + airlineName + "'");

            logger.trace(selectSQL);
            ResultSet rs = MySqlConnection.executeQuery(selectSQL.toString());
            rs.first();
            AirlineDao result = new AirlineDao(rs);
            resultsBufferByName.put(airlineName, result);
            resultsBufferById.put(result.getAirlineId(), result);
            return result;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
    
    public static AirlineDao select(int airlineId){
        if(resultsBufferById.containsKey(airlineId)) {
            return resultsBufferById.get(airlineId);
        }
   
        try {
            if (!isCountOne(
                    MySqlConnection.executeQuery("SELECT COUNT(*) FROM airlines WHERE airlineId='"
                            + airlineId + "'"))) {
                throw new CurrencyDataException(
                        "There is an issue with result data, there should be exactly 1 result");
            }

            StringBuffer selectSQL = new StringBuffer().append(
                    "SELECT * FROM airlines WHERE airlineId='" + airlineId + "'");

            logger.trace(selectSQL);
            ResultSet rs = MySqlConnection.executeQuery(selectSQL.toString());
            rs.first();
            AirlineDao result = new AirlineDao(rs);
            resultsBufferByName.put(result.getAirlineName(), result);
            resultsBufferById.put(airlineId, result);
            return result;   
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
