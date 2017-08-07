package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.contentproviderbroadcastreceiver.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Interface.CommentUtil;
import com.example.android.contentproviderbroadcastreceiver.Main.DayActivity;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by samsung on 2017-08-02.
 */

public class VHGps extends DayViewHolder {
    @BindView(R.id.gps_date)
    TextView date;
    @BindView(R.id.gps_place)
    TextView place;

    @OnClick(R.id.stepper)
    void click() {
        place.setText("asd");
    }

    @BindView(R.id.gps_button)
    Button button;
    @BindView(R.id.gps_comment)
    TextView comment;
    @BindView(R.id.gps_cv)
    View view;

    private Context context;

    public VHGps(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기
        this.context = context;
        setmListener(context);
    }

    @Override
    public void bindType(final MyRealmObject item) {
        final GpsData gpsData = (GpsData) item;
        DateFormat sdFormat = new SimpleDateFormat("HH : mm");
        Date d = new Date(gpsData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);
        place.setText(gpsData.getPlace() + " 도착");
        comment.setText(gpsData.getComment());
        switch (gpsData.getMoveState()) {
            case 0:
                place.append(" \nwalking on");
                break;
            case 1:
                place.append(" \nwalking off");
                break;
            case 2:
                place.append(" \nstill on");
                break;
            case 3:
                place.append(" \nstill off");
                break;
            case 4:
                place.append(" \nvehicle on");
                break;
            case 5:
                place.append(" \nvehicle off");
                break;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.getInstance().show((DayActivity) context, gpsData);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGpsItemClick(item);
            }
        });
    }
}
