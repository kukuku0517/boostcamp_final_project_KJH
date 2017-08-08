package com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmHelper;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Helper.CommentUtil;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmDataHelper;
import com.example.android.contentproviderbroadcastreceiver.DailyView.DayActivity;
import com.example.android.contentproviderbroadcastreceiver.Interface.NotifyListener;
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
    @BindView(R.id.gps_delete)
    Button deleteBtn;
    @BindView(R.id.gps_high)
    Button highBtn;
    @BindView(R.id.gps_comment)
    TextView comment;
    @BindView(R.id.gps_cv)
    View view;

    private Context context;

    public VHGps(View view, Context context, NotifyListener nListener) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기
        this.context = context;
        setmListener(context,nListener);
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
        if(gpsData.getHighlight()){
            highBtn.setText("highlight");
        }else{

            highBtn.setText("no");
        }

        switch (gpsData.getMoveState()) {
            case 1:
                place.append(" \nwalking off");
                break;
            case 2:
                place.append(" \nwalking on");
                break;
            case 3:
                place.append(" \nstill off");
                break;
            case 4:
                place.append(" \nstill on");
                break;
            case 5:
                place.append(" \nvehicle off");
                break;
            case 6:
                place.append(" \nvehicle on");
                break;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.getInstance().show((DayActivity) context, gpsData.getId(), RealmDataHelper.getInstance().GPS_DATA);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGpsItemClick(gpsData);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmHelper.DataDelete(gpsData);
            }
        });
        highBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.getInstance().highlight(gpsData);
            }
        });
    }
}
