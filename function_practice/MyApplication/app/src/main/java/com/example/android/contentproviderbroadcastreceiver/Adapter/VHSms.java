package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.view.View;

import com.example.android.contentproviderbroadcastreceiver.Data.SmsData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;

/**
 * Created by samsung on 2017-07-26.
 */

public class VHSms extends DayViewHolder {

    public VHSms(View view) {
        super(view);


    }

    @Override
    public void bindType(MyRealmObject item) {
       SmsData callData = (SmsData) item;
        tv2.setText(String.valueOf(callData.getDate()) );
        tv3.setText(callData.getPerson());
        tv4.setText(callData.getContent());

        iv.setImageResource(R.drawable.ic_message_black_24dp);
    }
}
