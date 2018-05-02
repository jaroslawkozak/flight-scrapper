package scrapper.db.dao;

import org.apache.log4j.Logger;
import scrapper.db.dao.exception.CurrencyDataException;
import scrapper.db.jdbc.MySqlConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractDao {
    protected final static Logger logger = Logger.getLogger(AbstractDao.class);
    
    public void insert() {     
        try(Connection connection = MySqlConnection.getConnection()){     
            this.insert(connection);        
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } 
    }
    
    public void insert(Connection connection) throws SQLException {}
    
    public void update() {     
        try(Connection connection = MySqlConnection.getConnection()){     
            this.update(connection);        
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } 
    }
    
    public void update(Connection connection) throws SQLException {}
    
    public static boolean isCountOne(ResultSet rs) throws SQLException {
        int count;
        rs.first();
        if((count = rs.getInt(1)) != 1) {
            logger.debug("Count found: " + count);
            return false;
        }
        return true;
    }
}
