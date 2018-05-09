package data.manager;

import data.manager.controller.DataManagerController;
import data.manager.db.setup.StartupTasks;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = DataManagerController.class)
@ComponentScan(basePackageClasses = StartupTasks.class)
public class DataManagerApplication {
    private final static Logger logger = Logger.getLogger(DataManagerApplication.class);
        
    public static void main(String[] args) {
        SpringApplication.run(DataManagerApplication.class, args);
    }
}

