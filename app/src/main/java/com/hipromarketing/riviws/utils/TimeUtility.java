package com.hipromarketing.riviws.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtility {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static String dateFormat = "dd-M-yyyy";
    private static String time = "hh:mm a";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    private static SimpleDateFormat timeFormat = new SimpleDateFormat(time);


    public static String getTimeAgo(long time,long currentTime) {

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = currentTime;
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " min";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hr";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else if (diff < 72 * HOUR_MILLIS) {
            return "2 days ago";
        } else {
            return ConvertMilliSecondsToFormattedDate(time);
        }
    }


    public static String ConvertMilliSecondsToFormattedDate(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleDateFormat.format(calendar.getTime());
    }


    public static String ConvertMilliSecondsTime(long milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return timeFormat.format(calendar.getTime());
    }
}
