package com.mifos.apache.fineract.ui.base;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Rajan Maurya
 *         On 10/07/17.
 */
public class DateUtils {

    public static final String LOG_TAG = DateUtils.class.getSimpleName();

    public static final String STANDARD_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_TIME_FORMAT = "yyyy MMM dd HH:mm:ss";

    /**
     * Format date time string into "2013 Feb 28 13:24:56" format.
     * @param dateString Standard Date Time String from server
     * @return String of Date time format 2013 Feb 28 13:24:56
     */
    public static String getDateTime(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat(STANDARD_DATE_TIME_FORMAT, Locale.ENGLISH);
        Date date = new Date();
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            Log.d(LOG_TAG, e.getLocalizedMessage());
        }
        format = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return format.format(calendar.getTime());
    }
}
