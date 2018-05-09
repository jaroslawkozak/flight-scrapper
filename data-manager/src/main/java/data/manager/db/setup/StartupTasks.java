package data.manager.db.setup;

import org.apache.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import data.manager.db.jdbc.MySqlConnection;

@Component
public class StartupTasks {
	private final static Logger logger = Logger.getLogger(StartupTasks.class);
	@EventListener(ApplicationReadyEvent.class)
    public void fillDatabaseWithData() {
    	if("true".equals(MySqlConnection.getDBParam("firstRun"))){
    		logger.info("Looks like this is a first run of application. Filling up tables with data...");
        	InsertAirlineData.main(new String[0]);
        	InsertAirportData.main(new String[0]);
        	InsertCurrencyData.main(new String[0]);
        	MySqlConnection.setDBParam("firstRun", "false");
    	}  
   }
}
