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
        	Thread t1 = new Thread(new InsertAirlineData());
        	Thread t2 = new Thread(new InsertAirportData());
        	Thread t3 = new Thread(new InsertCurrencyData());
        	
        	t1.run();
        	t2.run();
        	t3.run();
        	
        	try {
                t1.join();
                t2.join();
                t3.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        	
        	MySqlConnection.setDBParam("firstRun", "false");
    	}  
   }
}
