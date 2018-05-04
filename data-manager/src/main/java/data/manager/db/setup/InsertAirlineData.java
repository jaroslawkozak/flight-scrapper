package data.manager.db.setup;

import data.manager.db.dao.AirlineDao;
import data.manager.db.jdbc.MySqlConnection;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertAirlineData {
    private final static Logger logger = Logger.getLogger(InsertAirlineData.class);
    private final static String AIRPORTS_DATA_FILE_PATH = "src/main/resources/airlinesData/airlines.dat";
    
    public static void main(String[] args) {
        logger.debug("Insertng airline data to database");
        try(BufferedReader br = new BufferedReader(new FileReader(AIRPORTS_DATA_FILE_PATH));
                Connection connection = MySqlConnection.getConnection()){
            String line = null;           
            while((line = br.readLine()) != null ) {     
                String[] lineArr = line.split(";");
                AirlineDao airportData = new AirlineDao()
                        .setAirlineId(Integer.parseInt(lineArr[0]))
                        .setAirlineName(lineArr[1].replaceAll("\"", ""));
                airportData.insert(connection);
            }
        } catch (FileNotFoundException e) {
            logger.error("Airline data file not found!");
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("There is an issue with airline data file!");
            e.printStackTrace();
        } catch (SQLException sqlEx) {
            logger.error(sqlEx.getMessage());
        }
    }
}
