package kidouchi.chronobook.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by iuy407 on 2/11/16.
 */
public class TimeConverterUtil {

    /**
     * Converts date and time string to long
     *
     * @param date string
     * @param time string
     * @return long representing date and time in milliseconds
     * @throws ParseException
     */
    public static long convertDateTimeToLong(String date, String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a MM/dd/yyyy", Locale.US);
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.parse(time + " " + date).getTime();
    }

    /**
     * Converts milliseconds (long) to formatted date or time string representation
     *
     * @param dateTimeMilli a long representing date and time in milliseconds
     * @param pattern pattern to determine how returned date or time is represented
     * @return formatted date or time in string form
     */
    public static String convertMilliToString(long dateTimeMilli, String pattern) {
        Date date = new Date(dateTimeMilli);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * Convert string to date format
     * @param dateStr string format of date
     * @return Date object representing string in date format
     * @throws ParseException
     */
    public static Date convertStringToDate(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        format.setTimeZone(TimeZone.getDefault());
        return format.parse(dateStr);
    }

    /**
     * Convert string to time format
     * @param timeStr string format of time
     * @return Date object representing string in time format
     * @throws ParseException
     */
    public static Date convertStringToTime(String timeStr) throws  ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.US);
        format.setTimeZone(TimeZone.getDefault());
        return format.parse(timeStr);
    }

}
