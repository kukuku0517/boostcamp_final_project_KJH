package com.example.android.contentproviderbroadcastreceiver;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static android.R.attr.duration;
import static android.R.attr.name;
import static android.R.attr.start;
import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity {
    private CalendarView cv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
