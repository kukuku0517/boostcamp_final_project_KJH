package com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Helper.RealmHelper;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.SmsTradeData;
import com.example.android.contentproviderbroadcastreceiver.Helper.CommentUtil;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmDataHelper;
import com.example.android.contentproviderbroadcastreceiver.DailyView.DayActivity;
import com.example.android.contentproviderbroadcastreceiver.Interface.NotifyListener;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

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
    @BindView(R.id.sms_trade_comment)
    TextView comment;

    @BindView(R.id.sms_trade_write)
    Button button;

    @BindView(R.id.sms_trade_delete)
    Button deleteBtn;

    @BindView(R.id.trade_cv)
    View view;

    private Context context;

    public VHSmsTrade(View view, Context context) {
        super(view);
        this.context = context;
        setmListener(context,nListener);
    }

    @Override
    public void bindType(final MyRealmObject item) {
        final SmsTradeData stData = (SmsTradeData) item;
        DateFormat sdFormat = new SimpleDateFormat("HH : mm");
        Date d = new Date(stData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);
        comment.setText(stData.getComment());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.getInstance().show((AppCompatActivity) context, stData.getId(), RealmDataHelper.getInstance().SMS_TRADE_DATA);

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmHelper.DataDelete(stData);

            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSmsTradeItemClick(stData);
            }
        });

    }

}
