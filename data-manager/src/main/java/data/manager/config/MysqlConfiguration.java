package data.manager.config;

import lombok.Getter;

import org.apache.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import data.manager.db.dao.AirlineDao;
import data.manager.db.dao.exception.CurrencyDataException;
import data.manager.db.jdbc.MySqlConnection;
import data.manager.db.setup.InsertAirlineData;
import data.manager.db.setup.InsertAirportData;
import data.manager.db.setup.InsertCurrencyData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Configuration
public class MysqlConfiguration {
	private final static Logger logger = Logger.getLogger(MysqlConfiguration.class);
    private static Properties props = new Properties();
    static { 
        try {
            props.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
      }
    static @Getter
    private String host = props.getProperty("mysql.host");
    static @Getter
    private String port = props.getProperty("mysql.port");
    static @Getter
    private String database = props.getProperty("mysql.database");
    static @Getter
    private String user = props.getProperty("mysql.user");
    static @Getter
    private String password = props.getProperty("mysql.password"); 
    
    //@EventListener(ApplicationReadyEvent.class)
    public void fillDatabaseWithData() {
        logger.info("Filling up tables with data...");
    	InsertAirlineData.main(new String[0]);
    	InsertAirportData.main(new String[0]);
    	InsertCurrencyData.main(new String[0]);
   }
}
