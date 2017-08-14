package com.example.android.selfns.DailyView.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.android.selfns.GroupView.Data.SmsGroupData;
import com.example.android.selfns.GroupView.Data.SmsUnitData;
import com.example.android.selfns.Interface.MyRealmObject;
import com.example.android.selfns.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by samsung on 2017-07-26.
 */

public class VHSmsGroup extends DayViewHolder {

    //공통
    @BindView(R.id.item_am_pm)
    TextView ampm;
    @BindView(R.id.item_date)
    TextView date;
    @BindView(R.id.item_time_marker)
    TimelineView tlv;


    //공통 메뉴
    @BindView(R.id.item_people)
    View peopleView;

    //고유 레이아웃
    @BindView(R.id.sms_group_cv)
    View view;
    @BindView(R.id.sms_group_pcount)
    TextView pcount;
    @BindView(R.id.sms_group_mcount)
    TextView mcount;


    public VHSmsGroup(View view, Context context) {
        super(view);
        setmListener(context, nListener);
    }

    @Override
    public void bindType(final MyRealmObject item) {
        SmsGroupData callData = (SmsGroupData) item;
        DateFormat sdFormat = new SimpleDateFormat("hh:mm");
        Date d = new Date(callData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);

        int size = callData.getUnits().size();
        if (size != 0) {
            int count = callData.getUnits().size() - 1;
            String name = callData.getUnits().get(0).getName();
            pcount.setText(name + " 외 " + count + "명");
        }

        int count = 0;
        for (SmsUnitData sud : callData.getUnits()) {
            count += sud.getSmss().size();
        }
        mcount.setText(String.format("총 %d개의 메세지",count));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSmsGroupItemClick(item);
            }
        });
    }
}