package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.view.View;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.duration;

/**
 * Created by samsung on 2017-08-02.
 */

public class VHGps extends DayViewHolder {
    @BindView(R.id.gps_date)
    TextView date;
    @BindView(R.id.gps_place)
    TextView place;

    public VHGps(View view) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기

    }

    @Override
    public void bindType(MyRealmObject item) {
        GpsData gpsData = (GpsData)item;
        DateFormat sdFormat = new SimpleDateFormat("HH : mm");
        Date d = new Date(gpsData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);
        place.setText(gpsData.getPlace()+" 도착");
    }
}
