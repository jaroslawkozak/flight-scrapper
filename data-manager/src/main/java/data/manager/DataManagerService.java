package data.manager;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = DataManagerController.class)
public class DataManagerService {
    private final static Logger logger = Logger.getLogger(DataManagerService.class);
        
    public static void main(String[] args) {
        SpringApplication.run(DataManagerService.class, args);
    }
}

