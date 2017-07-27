package com.example.android.contentproviderbroadcastreceiver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import com.example.android.contentproviderbroadcastreceiver.Background.DataUpdateService;

import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity {
    private CalendarView cv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, DataUpdateService.class);
        startService(i);

        cv = (CalendarView) findViewById(R.id.calendarView);
        cv.setDate(System.currentTimeMillis());

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Intent intent = new Intent(MainActivity.this, DayActivity.class);
                intent.putExtra(getString(R.string.year), year);
                intent.putExtra(getString(R.string.month), month);
                intent.putExtra(getString(R.string.dayofmonth), dayOfMonth);
                startActivity(intent);
            }
        });


    }


}
