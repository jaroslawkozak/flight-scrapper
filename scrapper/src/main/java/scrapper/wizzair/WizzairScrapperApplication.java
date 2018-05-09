package scrapper.wizzair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import scrapper.wizzair.datamanager.dto.JobDto;

@SpringBootApplication
public class WizzairScrapperApplication {
    public static final String SCRAPPER_AIRLINE_NAME = "Wizzair";    
    public static List<JobDto> jobs = Collections.synchronizedList(new ArrayList<JobDto>());

    public static void main(String[] args) {
        SpringApplication.run(WizzairScrapperApplication.class, args);
    }    
}
