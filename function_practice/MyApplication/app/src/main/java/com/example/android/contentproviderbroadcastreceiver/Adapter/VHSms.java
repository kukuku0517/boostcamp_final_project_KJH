package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.SmsData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by samsung on 2017-07-26.
 */

public class VHSms extends DayViewHolder {

    @BindView(R.id.sms_date)
    TextView date;
    @BindView(R.id.sms_person)
    TextView person;
    @BindView(R.id.sms_number)
    TextView number;
    @BindView(R.id.sms_content)
    TextView content;

    @BindView(R.id.sms_button)
    Button button;

    public VHSms(View view) {
        super(view);


    }

    @Override
    public void bindType(MyRealmObject item) {
        SmsData callData = (SmsData) item;
        DateFormat sdFormat = new SimpleDateFormat("hh : mm");
        Date d = new Date(callData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);
        person.setText(callData.getPerson());
        content.setText(String.valueOf(callData.getContent()));
        number.setText(String.valueOf(callData.getId()));
    }
}
