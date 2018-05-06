package data.manager.db.jdbc;

import data.manager.config.MysqlConfiguration;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {
    private final static Logger logger = Logger.getLogger(MySqlConnection.class);
    private final static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String DB_CONN = "jdbc:mysql://" + MysqlConfiguration.getHost() + ":" + MysqlConfiguration.getPort() + "/";
    private final static String DB_NAME = MysqlConfiguration.getDatabase();
    private final static String DB_USER = MysqlConfiguration.getUser();
    private final static String DB_PASS = MysqlConfiguration.getPassword();
    
    public static Connection getConnection() {
        return getConnection(DB_NAME);
    } 
    
    public static Connection getConnection(String dbName) {
        logger.debug("Connecting to database...");
        try {
        	logger.debug("1");
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("Driver " + DB_DRIVER + " not found.");
            e.printStackTrace();
            return null;
        }

        Connection connection = null;
        logger.debug("2");
        try {
        	logger.debug("3");
            connection = DriverManager
            .getConnection(DB_CONN + dbName, DB_USER, DB_PASS);

        } catch (SQLException e) {
            logger.error("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }
        logger.debug("4");
        if (connection != null) {
            logger.debug("Connected");
        } else {
            logger.error("Failed to make connection!");
        }
        return connection;
    }
}
