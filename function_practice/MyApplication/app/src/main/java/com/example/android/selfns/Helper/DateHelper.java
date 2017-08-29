package com.example.android.selfns.Helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public String toWeekdayString(long millis) {
        Calendar c = toDate(millis);
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "";
        }
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

    public int getRangeOfDay(long millis) {
        Date d = new Date(millis);
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        if (c.get(Calendar.HOUR_OF_DAY) < 6) {
            return 0;
        } else if (c.get(Calendar.HOUR_OF_DAY) < 12) {
            return 1;
        } else if (c.get(Calendar.HOUR_OF_DAY) < 18) {
            return 2;
        } else {
            return 3;
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

    public boolean isToday(long millis) {
        long start = getStartEndDate(System.currentTimeMillis())[0];
        long start2 = getStartEndDate(millis)[0];

        if (start == start2) {
            return true;
        } else {
            return false;
        }
    }

    public long getDayAfter(long millis, int days) {
        Date date = new Date(millis);
        GregorianCalendar today = new GregorianCalendar();
        today.setTime(date);
        today.add(Calendar.DATE, days);
        return today.getTimeInMillis();
    }

    public Calendar getMonth(long today) {
        Date date = new Date(today);
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c;
    }

    public int getMonthNow(long today) {
        Date date = new Date(today);
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        return c.get(Calendar.MONTH);

    }


}
