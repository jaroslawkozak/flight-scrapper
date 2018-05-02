package scrapper.wizzair;

import scrapper.db.dao.AirportDao;

import java.sql.SQLException;

public class airporttest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            System.out.println(AirportDao.select("KTW").toString());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
