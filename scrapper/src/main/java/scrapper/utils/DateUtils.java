package scrapper.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    
    /**
     * Provides a string with date that is year ahead of today.
     * @return date string in default yyyy-MM-dd format
     */
    public static String getDateYearFromNow() {
        return getDateYearFromNow("yyyy-MM-dd");
    }
    
    /**
     * Provides a string with date that is year ahead of today.
     * @param format
     * @return date string in specified format
     */
    public static String getDateYearFromNow(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 1);
        return sdf.format(cal.getTime());
    }
    
    public static String getFormatedTodayDateTime() {
        return getFormatedTodayDate("yyyy-MM-dd'T'HH:mm:ss");
    }
    
    public static String getFormatedTodayDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);    
        return sdf.format(new Date());
    }
}
