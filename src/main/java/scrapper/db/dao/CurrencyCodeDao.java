package scrapper.db.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import scrapper.db.dao.exception.AirportDataException;
import scrapper.db.dao.exception.CurrencyDataException;
import scrapper.db.jdbc.MySqlConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CurrencyCodeDao extends AbstractDao {
    private int currencyId;
    private String currencyCode;
    private String currencyName;
    private static Map<String, CurrencyCodeDao> resultsBuffer = new HashMap<String,CurrencyCodeDao>();

    @Override
    public void insert(Connection connection) {
        StringBuffer insertTableSQL = new StringBuffer().append(
                "INSERT INTO currencyCodes (currencyId, currencyCode, currencyName) VALUES ")
                .append("(" + this.currencyId).append(",\"" + this.currencyCode + "\"")
                .append(",\"" + this.currencyName + "\"").append(")");
        try (Statement statement = connection.createStatement()) {
            logger.debug(insertTableSQL);
            statement.executeUpdate(insertTableSQL.toString());
            logger.debug("Record is inserted into table!");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static CurrencyCodeDao select(String currencyCode) throws SQLException {
        if(resultsBuffer.containsKey(currencyCode)) {
            return resultsBuffer.get(currencyCode);
        }
        try (Connection connection = MySqlConnection.getConnection();
                Statement statement = connection.createStatement()) {

            if (!isCountOne(
                    statement.executeQuery("SELECT COUNT(*) FROM currencyCodes WHERE currencyCode='"
                            + currencyCode + "'"))) {
                throw new CurrencyDataException(
                        "There is an issue with result data, there should be exactly 1 result");
            }

            StringBuffer selectSQL = new StringBuffer().append(
                    "SELECT * FROM currencyCodes WHERE currencyCode='" + currencyCode + "'");

            logger.debug(selectSQL);
            ResultSet rs = statement.executeQuery(selectSQL.toString());
            rs.first();
            CurrencyCodeDao result = new CurrencyCodeDao().setCurrencyId(rs.getInt(1))
                    .setCurrencyCode(rs.getString(2)).setCurrencyName(rs.getString(3));
            resultsBuffer.put(currencyCode, result);
            return result;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
