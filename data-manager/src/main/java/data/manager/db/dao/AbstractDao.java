package data.manager.db.dao;

import data.manager.db.jdbc.MySqlConnection;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao {
    protected final static Logger logger = Logger.getLogger(AbstractDao.class);
        
    public void insert() {}
  
    public void update() {}
    
    protected void executeUpdate(StringBuffer query) {
        try {
            logger.debug(query);
            MySqlConnection.executeUpdate(query.toString());
            logger.debug("Record is inserted into table!");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
        
    public static boolean isCountOne(ResultSet rs){
        int count;
        try {
            rs.first();
            if((count = rs.getInt(1)) != 1) {
                logger.debug("Count found: " + count);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
