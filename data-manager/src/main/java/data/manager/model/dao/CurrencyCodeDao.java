package data.manager.model.dao;

import data.manager.db.jdbc.MySqlConnection;
import data.manager.db.setup.InsertCurrencyData;
import data.manager.model.dao.exception.CurrencyDataException;
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
public class CurrencyCodeDao extends AbstractDao {
    private final static Logger logger = Logger.getLogger(CurrencyCodeDao.class);
    private String currencyId;
    private String currencyCode;
    private String currencyName;
    private static Map<String, CurrencyCodeDao> resultsBuffer = new HashMap<String,CurrencyCodeDao>();

    @Override
    public void insert() {
        StringBuffer insertTableSQL = new StringBuffer().append(
                "INSERT INTO currencyCodes (currencyId, currencyCode, currencyName) VALUES ")
                .append("(" + this.currencyId).append(",\"" + this.currencyCode + "\"")
                .append(",\"" + this.currencyName + "\"").append(")");      
        executeUpdate(insertTableSQL);  
    }

    public static CurrencyCodeDao selectById(String currencyId){
        return CurrencyCodeDao.select("currencyId", currencyId);
    }
    
    public static CurrencyCodeDao selectByCode(String currencyCode){
        return CurrencyCodeDao.select("currencyCode", currencyCode);
    }
    
    private static CurrencyCodeDao select(String fieldName, String fieldValue){
        if(resultsBuffer.containsKey(fieldName)) {
            return resultsBuffer.get(fieldName);
        }

        try {
            String query = "SELECT COUNT(*) FROM currencyCodes WHERE " + fieldName + "='" + fieldValue + "'";
            logger.trace(query);
            if (!isCountOne(
                    MySqlConnection.executeQuery(query))) {
                throw new CurrencyDataException(
                        "There is an issue with result data, there should be exactly 1 result");
                
            }
            StringBuffer selectSQL = new StringBuffer().append(
                    "SELECT * FROM currencyCodes WHERE " + fieldName + "='" + fieldValue + "'");

            logger.trace(selectSQL);
            ResultSet rs = MySqlConnection.executeQuery(selectSQL.toString());
            rs.first();
            CurrencyCodeDao result = new CurrencyCodeDao().setCurrencyId(rs.getString(1))
                    .setCurrencyCode(rs.getString(2)).setCurrencyName(rs.getString(3));
            resultsBuffer.put(fieldName, result);
            return result;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return null;    
    }
}
