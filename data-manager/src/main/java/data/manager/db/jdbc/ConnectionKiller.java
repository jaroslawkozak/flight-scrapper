package data.manager.db.jdbc;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Accessors(chain = true)
public class ConnectionKiller implements Runnable {
    private final static Logger logger = Logger.getLogger(ConnectionKiller.class);
    private static final long STEP_MILIS = 10;
    @Getter
    private boolean running = false;
    @NonNull
    private Connection connection;
    @NonNull
    private Integer timeoutMilis;
    private AtomicInteger elapsed = new AtomicInteger(0);
    
    @Override
    public void run() {
        logger.error("Starting sql connection monitor.");
        this.running = true;
        while(this.elapsed.get() < this.timeoutMilis) {
            try {
                Thread.sleep(STEP_MILIS);
                this.elapsed.set(this.elapsed.get() + (int)STEP_MILIS);
                if(Thread.currentThread().isInterrupted()) {
                    if(connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                }
            } catch (InterruptedException | SQLException e) {
                e.printStackTrace();             
            }           
        }
        
        try {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.running = false;
        logger.error("Stopping sql connection monitor. Connections closed.");
    }
   
    public void resetTimer() {
        if(!this.running) {
            logger.error("Thread is not running.");
        }
        this.elapsed.set(0);
    }
}
