package scrapper.db.jdbc;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {
    private final static Logger logger = Logger.getLogger(MySqlConnection.class);
    private final static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String DB_CONN = "jdbc:mysql://localhost:3306/flight-scrapper";
    private final static String DB_USER = "root";
    private final static String DB_PASS = "qwe123";
    
    public static Connection getConnection() {
        logger.debug("Connecting to database...");
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("Driver " + DB_DRIVER + " not found.");
            e.printStackTrace();
            return null;
        }

        Connection connection = null;

        try {
            connection = DriverManager
            .getConnection(DB_CONN, DB_USER, DB_PASS);

        } catch (SQLException e) {
            logger.error("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }

        if (connection != null) {
            logger.debug("Connected");
        } else {
            logger.error("Failed to make connection!");
        }
        return connection;
      }
    
}
