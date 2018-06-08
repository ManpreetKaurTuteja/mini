package com.miniapp.commonUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mkaur on 6/13/17.
 */

public class DateUtility {
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat timeFormat = new SimpleDateFormat("K:mma");
    static long chatAvailabilityTime = 24 * 60 * 60 * 1000;

    public static String getCurrentTime() {

        Date today = Calendar.getInstance().getTime();
        return timeFormat.format(today);
    }

    public static String getCurrentTimeUTC() {
        DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date today = Calendar.getInstance().getTime();
        return timeFormat.format(today);
    }

    public static String getCurrentDate() {

        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }


    public static String getTimeFromDateTime(String dateString) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        date = getDateInFormat(dateString);
        if (date != null)
            return timeFormat.format(date.getTime());
        return "";
    }

    public static Date getDateInFormat(String dateString) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = myFormat.parse(myFormat.format(sdf.parse(dateString)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateWithSecs(String dateString) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = myFormat.parse(myFormat.format(sdf.parse(dateString)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long getLeftMatchTime(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = getDateWithSecs(dateString);
        String todayString = simpleDateFormat.format(Calendar.getInstance().getTime());
        Date today = null;
        try {
            today = simpleDateFormat.parse(todayString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = (chatAvailabilityTime) - (today.getTime() - date.getTime());
        return difference;
    }

    public static String getMsgTimeToDisplay(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = getDateInFormat(dateString);
        String todayString = sdf.format(Calendar.getInstance().getTime());
        Date today = getDateInFormat(todayString);
        String time = null;
        int d = today.getDate();
        long difference = today.getTime() - date.getTime();
        difference = difference / 1000;
        long hours = difference / 60 / 60;
        if (isDateSame(dateString)) {
            long minutes = (difference / 60) - (hours * 60);
            if (hours == 0) {
                if (minutes == 0)
                    time = "Now";
                else if (minutes == 1)
                    time = minutes + " min ago";
                else if (minutes >= 1)
                    time = minutes + " mins ago";
            } else if (hours == 1)
                time = hours + " hour ago";
            else if (hours >= 1)
                time = hours + " hours ago";
        } else if (isDateBefore(dateString)) {
            if ( hours < 48)
                time = "yesterday";
            else if (hours >= 48)
                time = (hours / 24) + " days ago";
        }
        return time;
    }

    public static boolean isDateAfter(String date) {
        Date strDate = null;
        strDate = getDateForCompare(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date today = null;
        String todayString = sdf.format(Calendar.getInstance().getTime());
        today = getDateForCompare(todayString);
        if (today.after(strDate)) {
            return true;
        }
        return false;
    }

    public static boolean isDateBefore(String date) {
        Date strDate = null;
        strDate = getDateForCompare(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date today = null;
        String todayString = sdf.format(Calendar.getInstance().getTime());
        today = getDateForCompare(todayString);
        if (strDate.before(today)) {
            return true;
        }
        return false;
    }

    public static boolean isDateSame(String date) {
        Date strDate = null;
        strDate = getDateForCompare(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date today = null;
        String todayString = sdf.format(Calendar.getInstance().getTime());
        today = getDateForCompare(todayString);
        if (today.compareTo(strDate) == 0) {
            return true;
        }
        return false;
    }

    public static Date getDateForCompare(String dateString) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = myFormat.parse(myFormat.format(sdf.parse(dateString)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getCurrentDateInFormat(String dateString) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String todayString = sdf.format(Calendar.getInstance().getTime());
        Date date = null;
        try {
            date = myFormat.parse(myFormat.format(sdf.parse(dateString)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String getMsgTimeOnly(String dateString) {
        SimpleDateFormat myFormat = new SimpleDateFormat("hh:mma");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        String date = null;
        try {
            date = myFormat.format(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
