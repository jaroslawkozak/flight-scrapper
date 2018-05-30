package data.manager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DMDateUtils {
    
    public static String addMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDateObj = sdf.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDateObj);
        cal.add(Calendar.MONTH, 1);
        return sdf.format(cal.getTime());
    }
    
    public static int getDaysBetween(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
