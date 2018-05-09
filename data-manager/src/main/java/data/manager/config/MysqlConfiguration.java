package data.manager.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import data.manager.db.jdbc.MySqlConnection;
import data.manager.db.setup.InsertAirlineData;
import data.manager.db.setup.InsertAirportData;
import data.manager.db.setup.InsertCurrencyData;
import lombok.Getter;

@Configuration
@Component
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
}
