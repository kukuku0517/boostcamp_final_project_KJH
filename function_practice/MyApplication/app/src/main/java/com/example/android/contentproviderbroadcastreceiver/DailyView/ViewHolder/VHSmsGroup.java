package com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Interface.NotifyListener;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by samsung on 2017-07-26.
 */

public class VHSmsGroup extends DayViewHolder {

    @BindView(R.id.sms_group_date)
    TextView date;
    @BindView(R.id.sms_group_person)
    TextView person;
//    @BindView(R.id.sms_group_number)
//    TextView number;
    @BindView(R.id.sms_group_content)
    TextView content;
    @BindView(R.id.sms_group_button)
    Button button;
    @BindView(R.id.sms_group_cv)
    View view;

    public VHSmsGroup(View view, Context context, NotifyListener nListener) {
        super(view);
        setmListener(context,nListener);
    }

    @Override
    public void bindType(final MyRealmObject item) {
        SmsGroupData callData = (SmsGroupData) item;
        DateFormat sdFormat = new SimpleDateFormat("HH : mm");
        Date d = new Date(callData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText("~"+tempDate);

        int size = callData.getUnits().size();
        if(size!=0){
            int count = callData.getUnits().size()-1;
            String name = callData.getUnits().get(0).getName();
            person.setText(name+" 외 "+count+"명");
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSmsGroupItemClick(item);
            }
        });
    }
}
