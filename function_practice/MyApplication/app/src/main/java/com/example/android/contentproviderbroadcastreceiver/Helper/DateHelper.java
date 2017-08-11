package com.example.android.contentproviderbroadcastreceiver.Helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.R.attr.format;

/**
 * Created by samsung on 2017-08-05.
 */

public class DateHelper {
    static DateHelper datehelper;
    public static final long QUARTER = 21600000;

    public static DateHelper getInstance() {
        if (datehelper == null) {
            datehelper = new DateHelper();
        }
        return datehelper;
    }

    public String toDateString(String format, long millis) {
        Date d = new Date(millis);
        DateFormat sdFormat = new SimpleDateFormat(format);
        String tempDate = sdFormat.format(d);

        return tempDate;
    }

    public String isAm(long millis) {
        Date d = new Date(millis);
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        if (c.get(Calendar.HOUR_OF_DAY) < 12) {
            return "AM";
        } else {
            return "PM";
        }

    }

    public String toDurationString(long millis) {
        long dur = millis;
        int hour = (int) (dur / 60 / 60);
        int min = (int) (dur / 60 % 60);
        int sec = (int) (dur % 60);
        String data = String.format("%d시간 %d분 %d초", hour, min, sec);

        return data;
    }


    public Calendar toDate(long millis) {
        Date d = new Date(millis);
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        return c;
    }

    public long toMillis(Calendar c, int hour, int minute) {
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        return c.getTimeInMillis();
    }

    public long[] getStartEndDate(long millis) {
        Date date = new Date(millis);
        GregorianCalendar today = new GregorianCalendar();
        today.setTime(date);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        long start = today.getTimeInMillis();
        today.add(Calendar.DATE, 1);
        long end = today.getTimeInMillis();
        return new long[]{start, end};
    }

    public long getDayAfter(long millis, int days) {
        Date date = new Date(millis);
        GregorianCalendar today = new GregorianCalendar();
        today.setTime(date);
        today.add(Calendar.DATE, days);
        return today.getTimeInMillis();
    }


}
