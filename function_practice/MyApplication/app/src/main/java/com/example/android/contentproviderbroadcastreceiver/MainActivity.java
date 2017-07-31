package com.example.android.contentproviderbroadcastreceiver;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Background.DataUpdateService;
import com.example.android.contentproviderbroadcastreceiver.Background.SNSNotificationService;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.media.CamcorderProfile.get;
import static com.example.android.contentproviderbroadcastreceiver.R.string.month;
import static com.example.android.contentproviderbroadcastreceiver.R.string.year;

public class MainActivity extends AppCompatActivity {
    private CalendarView cv;

private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
tv= (TextView) findViewById(R.id.textView);
        Intent i = new Intent(this, DataUpdateService.class);
        startService(i);
        Intent notifyIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(notifyIntent);
//
//        cv = (CalendarView) findViewById(R.id.calendarView);
//        cv.setDate(System.currentTimeMillis());
//
//        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                Intent intent = new Intent(MainActivity.this, DayActivity.class);
//                intent.putExtra(getString(R.string.year), year);
//                intent.putExtra(getString(R.string.month), month);
//                intent.putExtra(getString(R.string.dayofmonth), dayOfMonth);
//                startActivity(intent);
//            }
//        });
        final String TAG = "calendarView";
        final CompactCalendarView compactCalendar = (CompactCalendarView) findViewById(R.id.calendarView);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        Event ev1 = new Event(Color.GREEN, 1433701251000L, "Some extra data that I want to store.");
        compactCalendar.addEvent(ev1);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.GREEN, 1433704251000L);
        compactCalendar.addEvent(ev2);

        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        List<Event> events = compactCalendar.getEvents(1433701251000L); // can also take a Date object

        // events has size 2 with the 2 events inserted previously
        Log.d(TAG, "Events: " + events);

        // define a listener to receive callbacks when certain events happen.
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Calendar c = Calendar.getInstance();
                c.setTime(dateClicked);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);


//                List<Event> events = compactCalendar.getEvents(dateClicked);
//                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                Intent intent = new Intent(MainActivity.this, DayActivity.class);
                intent.putExtra(getString(R.string.year), year);
                intent.putExtra(getString(R.string.month), month);
                intent.putExtra(getString(R.string.dayofmonth), dayOfMonth);
                startActivity(intent);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);

            }
        });
    }


}
