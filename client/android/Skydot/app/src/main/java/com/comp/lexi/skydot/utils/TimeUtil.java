package com.comp.lexi.skydot.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private static String pattern = "yyyy-MM-dd HH:mm:ss Z";

    public static String now() {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(Calendar.getInstance(TimeZone.getDefault()).getTime());
    }

    public static boolean timeout(String timestamp) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        Date nowDate = sdf.parse(now());
        Date timestampDate = sdf.parse(timestamp);
        return TimeUnit.MILLISECONDS.toSeconds(nowDate.getTime() - timestampDate.getTime()) >= Variables.TIMEOUT_TIME;
    }

    public static boolean newer(String timestamp1, String timestamp2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        Date date1 = sdf.parse(timestamp1);
        Date date2 = sdf.parse(timestamp2);
        return TimeUnit.MILLISECONDS.toSeconds(date1.getTime() - date2.getTime()) >= 0;
    }

    public static boolean isTimestamp(String timestamp)  {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            sdf.parse(timestamp);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static int compare(String t1, String t2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        Date t1Date = sdf.parse(t1);
        Date t2Date = sdf.parse(t2);
        return t1Date.compareTo(t2Date);
    }

    public static Calendar parseData(String data) throws ParseException {
        Date date = new SimpleDateFormat(pattern, Locale.getDefault()).parse(data);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        return calendar;
    }

    public static String getTimeAgo(String data) throws ParseException {
        Date date = new SimpleDateFormat(pattern, Locale.getDefault()).parse(data);

        long time = TimeUnit.MILLISECONDS.toSeconds(date.getTime());
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = Calendar.getInstance(TimeZone.getDefault()).getTimeInMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "< 1m";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return "< " + diff / MINUTE_MILLIS + "m";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "< 1h";
        } else if (diff < 24 * HOUR_MILLIS) {
            return "< " +diff / HOUR_MILLIS + "h";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static String parseDataString(String data) throws ParseException {
        DateFormat parser = new SimpleDateFormat(pattern, Locale.getDefault());

        DateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date timeDate = parser.parse(data);
        String time = timeFormatter.format(timeDate);

        DateFormat dayFormatter = new SimpleDateFormat("MMM dd", Locale.getDefault());
        Date dayDate = parser.parse(data);

        String day;
        if (isToday(dayDate)) {
            day = "Today";
        } else if (isTomorrow(parser.parse(data))) {
            day = "Tomorrow";
        } else {
            day = dayFormatter.format(dayDate);
        }
        return day + " @ " + time;
    }

    public static String parseDay(String data) throws ParseException {
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat dayFormatter = new SimpleDateFormat("EE, MMM dd", Locale.getDefault());
        Date dayDate = parser.parse(data);
        return dayFormatter.format(dayDate);
    }

    public static String parseTime(String data) throws ParseException {
        DateFormat parser = new SimpleDateFormat("HH:mm", Locale.getDefault());
        DateFormat timeFormatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
        Date timeDate = parser.parse(data);
        return timeFormatter.format(timeDate);
    }

    public static String parseDateTime(String date, String time) throws ParseException {
        DateFormat dayFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Date dayDate = dayFormatter.parse(date);
        Date timeDate = timeFormatter.parse(time);

        DateFormat parserDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat parserTime = new SimpleDateFormat("HH:mm:ss Z", Locale.getDefault());

        String newDay = parserDay.format(dayDate);
        String newTime = parserTime.format(timeDate);
        return newDay + " " + newTime;
    }

    private static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    private static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance(TimeZone.getDefault()).getTime());
    }

    private static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance(TimeZone.getDefault()));
    }

    private static boolean isTomorrow(Date date) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR, 1);
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTime(date);
        return today.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR)
                && today.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isAfterDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isAfterDay(cal1, cal2);
    }

    private static boolean isAfterDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
    }

}