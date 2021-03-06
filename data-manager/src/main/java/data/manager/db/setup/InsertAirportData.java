package data.manager.db.setup;

import data.manager.model.dao.AirportDao;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InsertAirportData implements Runnable {
    private final static Logger logger = Logger.getLogger(InsertAirportData.class);
    private final static String AIRPORTS_DATA_FILE_PATH = "src/main/resources/airportData/airports.dat";
    
    @Override
    public void run() {
        logger.debug("Insertng airport data to database");
        try(BufferedReader br = new BufferedReader(new FileReader(AIRPORTS_DATA_FILE_PATH))){
            String line = null;
            
            while((line = br.readLine()) != null ) {
                String[] lineArr = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                AirportDao airportData = new AirportDao()
                        .setAirportId(Integer.parseInt(parseInput(lineArr[0])))
                        .setName(parseInput(lineArr[1]))
                        .setCity(parseInput(lineArr[2]))
                        .setCountry(parseInput(lineArr[3]))
                        .setIATA(parseInput(lineArr[4]))
                        .setICAO(parseInput(lineArr[5]))
                        .setLatitude(parseInput(lineArr[6]))
                        .setLongitude(parseInput(lineArr[7]))
                        .setAltitude(parseInput(lineArr[8]))
                        .setTimezone(parseInput(lineArr[9]))
                        .setDST(parseInput(lineArr[10]));
                airportData.insert();
            }
        } catch (FileNotFoundException e) {
            logger.error("Airport data file not found!");
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("There is an issue with airport data file!");
            e.printStackTrace();
        }
    }
    private static String parseInput(String input) {
        return "\\N".equals(input) ? "\\N" : input.replaceAll("\"", "");
    }
}
