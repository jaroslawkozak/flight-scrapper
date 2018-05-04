package data.manager.db.dao;

import data.manager.db.dao.exception.AirportDataException;
import data.manager.db.dao.exception.CurrencyDataException;
import data.manager.db.jdbc.MySqlConnection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AirlineDao extends AbstractDao {
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
    public void insert(Connection connection) {
        StringBuffer insertTableSQL = new StringBuffer()
                .append("INSERT INTO airlines (airlineId, airlineName) VALUES ")
                .append("(" + this.airlineId)
                .append(",\"" + this.airlineName + "\"")
                .append(")");        
        try(Statement statement = connection.createStatement()){                 
            logger.debug(insertTableSQL);
            statement.executeUpdate(insertTableSQL.toString());
            logger.debug("Record is inserted into table!");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static AirlineDao select(String airlineName) throws SQLException {
        if(resultsBufferByName.containsKey(airlineName)) {
            return resultsBufferByName.get(airlineName);
        }
        try (Connection connection = MySqlConnection.getConnection();
                Statement statement = connection.createStatement()) {

            if (!isCountOne(
                    statement.executeQuery("SELECT COUNT(*) FROM airlines WHERE airlineName='"
                            + airlineName + "'"))) {
                throw new CurrencyDataException(
                        "There is an issue with result data, there should be exactly 1 result");
            }

            StringBuffer selectSQL = new StringBuffer().append(
                    "SELECT * FROM airlines WHERE airlineName='" + airlineName + "'");

            logger.debug(selectSQL);
            ResultSet rs = statement.executeQuery(selectSQL.toString());
            rs.first();
            AirlineDao result = new AirlineDao(rs);
            resultsBufferByName.put(airlineName, result);
            resultsBufferById.put(result.getAirlineId(), result);
            return result;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
    
    public static AirlineDao select(int airlineId) throws SQLException {
        if(resultsBufferById.containsKey(airlineId)) {
            return resultsBufferById.get(airlineId);
        }
        try (Connection connection = MySqlConnection.getConnection();
                Statement statement = connection.createStatement()) {

            if (!isCountOne(
                    statement.executeQuery("SELECT COUNT(*) FROM airlines WHERE airlineId='"
                            + airlineId + "'"))) {
                throw new CurrencyDataException(
                        "There is an issue with result data, there should be exactly 1 result");
            }

            StringBuffer selectSQL = new StringBuffer().append(
                    "SELECT * FROM airlines WHERE airlineId='" + airlineId + "'");

            logger.debug(selectSQL);
            ResultSet rs = statement.executeQuery(selectSQL.toString());
            rs.first();
            AirlineDao result = new AirlineDao(rs);
            resultsBufferByName.put(result.getAirlineName(), result);
            resultsBufferById.put(airlineId, result);
            return result;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
