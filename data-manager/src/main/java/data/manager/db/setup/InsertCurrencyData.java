package data.manager.db.setup;

import data.manager.model.dao.CurrencyCodeDao;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InsertCurrencyData implements Runnable{
    private final static Logger logger = Logger.getLogger(InsertCurrencyData.class);
    private final static String CURRENCY_DATA_FILE_PATH = "src/main/resources/currencyData/currencies.dat";
    
    @Override
    public void run() {
        logger.debug("Insertng currency data to database");
        try(BufferedReader br = new BufferedReader(new FileReader(CURRENCY_DATA_FILE_PATH))){
            String line = null;
            br.readLine(); //skip header line
            while((line = br.readLine()) != null ) {
                String[] lineArr = line.split(";");
                CurrencyCodeDao currencyData = new CurrencyCodeDao()
                        .setCurrencyId(lineArr[2])
                        .setCurrencyCode(lineArr[1])
                        .setCurrencyName(lineArr[0]);
                currencyData.insert();
            }
        } catch (FileNotFoundException e) {
            logger.error("Currency data file not found!");
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("There is an issue with currency data file!");
            e.printStackTrace();
        } 
    }
}
