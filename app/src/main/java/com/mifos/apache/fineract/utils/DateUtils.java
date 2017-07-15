package com.mifos.apache.fineract.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Rajan Maurya
 *         On 06/07/17.
 */
public class DateUtils {

    public static final String LOG_TAG = DateUtils.class.getSimpleName();

    public static final String STANDARD_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_TIME_FORMAT = "yyyy MMM dd HH:mm:ss";
    public static final String OUTPUT_DATE_FORMAT = "dd MMM yyyy";
    public static final String INPUT_DATE_FORMAT = "yyyy-MM-dd'Z'";

    /**
     * Format date time string into "2013 Feb 28 13:24:56" format.
     *
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

    public static boolean isTokenExpired(String tokenExpiration) {
        SimpleDateFormat format = new SimpleDateFormat(STANDARD_DATE_TIME_FORMAT, Locale.ENGLISH);
        Date date;
        try {
            date = format.parse(tokenExpiration);
            if (System.currentTimeMillis() > date.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            Log.d(LOG_TAG, e.getLocalizedMessage());
        }
        return false;
    }

    public static String getDate(String dateString, String inputFormat, String outFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            Log.d(LOG_TAG, e.getLocalizedMessage());
        }
        dateFormat = new SimpleDateFormat(outFormat, Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return dateFormat.format(calendar.getTime());
    }

    public static String getDateInUTC(Calendar time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STANDARD_DATE_TIME_FORMAT,
                Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(time.getTime());
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OUTPUT_DATE_FORMAT,
                Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime());
    }
}
