package data.manager.db.jdbc;

import data.manager.config.MysqlConfiguration;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlConnection {
    private final static Logger logger = Logger.getLogger(MySqlConnection.class);
    private final static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String DB_CONN = "jdbc:mysql://" + MysqlConfiguration.getHost() + ":" + MysqlConfiguration.getPort() + "/";
    private final static String DB_NAME = MysqlConfiguration.getDatabase();
    private final static String DB_USER = MysqlConfiguration.getUser();
    private final static String DB_PASS = MysqlConfiguration.getPassword();
    private static Connection CONNECTION;
    private static ConnectionKiller connKiller;
    
    public static ResultSet executeQuery(String query) throws SQLException {
        Statement statement = getConnection().createStatement();
        return statement.executeQuery(query);
    }
    
    public static int executeUpdate(String query) throws SQLException {
        try (Statement statement = getConnection().createStatement()){
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            throw e;
        }      
    }
       
    private static Connection getConnection() {
        try {
            if(CONNECTION == null || CONNECTION.isClosed()) {
                return MySqlConnection.startConnection(DB_NAME);
            } else {
                connKiller.resetTimer();  
                return MySqlConnection.CONNECTION;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
        
    private static Connection startConnection(String dbName) {
        logger.debug("Connecting to database...");
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("Driver " + DB_DRIVER + " not found.");
            e.printStackTrace();
            return null;
        }

        try {
        	MySqlConnection.CONNECTION = DriverManager
            .getConnection(DB_CONN + dbName, DB_USER, DB_PASS);
        } catch (SQLException e) {
            logger.error("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }
        if (MySqlConnection.CONNECTION != null) {
            logger.debug("Connected");
        } else {
            logger.error("Failed to make connection!");
        }
        connKiller = new ConnectionKiller(MySqlConnection.CONNECTION, 60000);
        Thread thread = new Thread(connKiller);
        thread.start();
        
        return MySqlConnection.CONNECTION;
    }

    private static void resetTimer() {
        connKiller.resetTimer();        
    }
}
