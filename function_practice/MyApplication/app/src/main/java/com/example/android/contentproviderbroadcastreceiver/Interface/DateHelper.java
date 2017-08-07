package com.example.android.contentproviderbroadcastreceiver.Interface;

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


}
