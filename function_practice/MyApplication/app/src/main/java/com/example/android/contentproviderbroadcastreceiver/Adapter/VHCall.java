package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.telecom.Call;
import android.view.View;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-07-26.
 */

public class VHCall extends DayViewHolder {

    public VHCall(View view) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기

    }

    @Override
    public void bindType(MyRealmObject item) {
        CallData callData = (CallData) item;
        tv2.setText(String.valueOf(callData.getDate()));
        tv3.setText(callData.getPerson());
        tv4.setText(callData.getDuration());
        tv5.setText(callData.getNumber());
        iv.setImageResource(R.drawable.ic_call_black_24dp);

    }
}
