package scrapper.config;

import lombok.Getter;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ScrapperConfiguration {
    private final static Logger logger = Logger.getLogger(ScrapperConfiguration.class);
    private static Properties props = new Properties();
    static { 
        try {
            props.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }       
    }
    static @Getter
    private String dataManagerHost = props.getProperty("datamanager.host");
    static @Getter
    private String dataManagerPort = props.getProperty("datamanager.port");
    static @Getter
    private String wizzairApiVersion = props.getProperty("wizzair.api.version");
}
