package data.manager.model.dao;

import data.manager.db.jdbc.MySqlConnection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao {
    protected final static Logger logger = Logger.getLogger(AbstractDao.class);
        
    @Autowired
    protected static JdbcTemplate jdbcTemplate;
    
    public void insert() {}
  
    public void update() {}
    
    protected void executeUpdate(StringBuffer query) {
        try {
            logger.trace(query);
            MySqlConnection.executeUpdate(query.toString());
            logger.trace("Record is updated in table!");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
        
    public static boolean isCountOne(ResultSet rs){
        int count;
        try {
            rs.first();
            if((count = rs.getInt(1)) != 1) {
                logger.trace("Count found: " + count);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
