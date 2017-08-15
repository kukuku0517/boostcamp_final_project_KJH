package com.example.android.selfns.DetailView.ViewHolder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.selfns.DailyView.ViewHolder.DayViewHolder;
import com.example.android.selfns.Data.DTO.Detail.GpsDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.R;

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

    public VHGps(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기
        this.context = context;
//        setmListener(context,nListener);
    }

    @Override
    public void bindType(final BaseDTO item) {
        final GpsDTO gpsData = (GpsDTO) item;
        DateFormat sdFormat = new SimpleDateFormat("HH : mm");
        Date d = new Date(gpsData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);
        place.setText(gpsData.getPlace() + " 도착");

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
                ItemInteractionUtil.getInstance(context).show((AppCompatActivity)  context, gpsData.getId(), RealmClassHelper.getInstance().GPS_DATA);
            }
        });
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onGpsItemClick(gpsData);
//            }
//        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).deleteItem(gpsData);
            }
        });

    }
}
