package data.manager.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class MysqlConfiguration {
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
