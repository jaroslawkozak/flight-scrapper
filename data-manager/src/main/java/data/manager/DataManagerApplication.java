package data.manager;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = DataManagerController.class)
public class DataManagerApplication {
    private final static Logger logger = Logger.getLogger(DataManagerApplication.class);
        
    public static void main(String[] args) {
        SpringApplication.run(DataManagerApplication.class, args);
    }
}

