package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.SmsTradeData;
import com.example.android.contentproviderbroadcastreceiver.Interface.CommentUtil;
import com.example.android.contentproviderbroadcastreceiver.Main.DayActivity;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by samsung on 2017-08-04.
 */

public class VHSmsTrade extends DayViewHolder {

    @BindView(R.id.sms_trade_cost)
    TextView cost;
    @BindView(R.id.sms_trade_date)
    TextView date;
    //    @BindView(R.id.sms_group_number)
//    TextView number;
    @BindView(R.id.sms_trade_place)
    TextView place;
    @BindView(R.id.sms_trade_thing)
    TextView thing;

    @BindView(R.id.sms_trade_write)
    Button button;

    @BindView(R.id.trade_cv)
  View view;

    private Context context;

    public VHSmsTrade(View view, Context context) {
        super(view);
        this.context = context;
        setmListener(context);
    }

    @Override
    public void bindType(final MyRealmObject item) {
        final SmsTradeData callData = (SmsTradeData) item;
        DateFormat sdFormat = new SimpleDateFormat("HH : mm");
        Date d = new Date(callData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.getInstance().show((DayActivity) context, callData);

            }
        });

       view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSmsItemClick(callData);
            }
        });
    }
}
