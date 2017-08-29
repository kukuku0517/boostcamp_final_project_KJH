package com.example.android.selfns.DailyView.ViewHolder;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.selfns.Data.DTO.Group.SmsGroupDTO;
import com.example.android.selfns.Data.DTO.Group.SmsUnitDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.R;
import com.github.vipulasri.timelineview.LineType;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @BindView(R.id.item_am_pm_to)
    TextView ampmTo;
    @BindView(R.id.item_date_to)
    TextView dateTo;
    @BindView(R.id.item_time_marker)
    TimelineView tlv;
    @BindView(R.id.item_time_icon)
    ImageView icon;


    //고유 레이아웃
    @BindView(R.id.sms_group_cv)
    View view;
    @BindView(R.id.sms_group_pcount)
    TextView pcount;
    @BindView(R.id.sms_group_mcount)
    TextView mcount;
    @BindView(R.id.sms_group_title)
    TextView title;

    public VHSmsGroup(View view, Context context) {
        super(view);
        setmListener(context, nListener);  tlv.initLine(LineType.NORMAL);
        tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle, null));
        icon.setImageResource(R.drawable.ic_email_black_24dp);
    }

    @Override
    public void bindType(final BaseDTO item) {
        SmsGroupDTO callData = (SmsGroupDTO) item;
        date.setText(DateHelper.getInstance().toDateString("hh:mm",callData.getStart()));
        ampm.setText(DateHelper.getInstance().isAm(callData.getStart()));

        dateTo.setText(DateHelper.getInstance().toDateString("hh:mm",callData.getEnd()));
        ampmTo.setText(DateHelper.getInstance().isAm(callData.getEnd()));


        switch (DateHelper.getInstance().getRangeOfDay(callData.getStart())) {
            case 0:
                title.setText("새벽 메세지함");
                break;
            case 1:
                title.setText("오전 메세지함");
                break;
            case 2:
                title.setText("오후 메세지함");
                break;
            case 3:
                title.setText("저녁 메세지함");
                break;
        }
        int size = callData.getUnits().size();
        if (size != 0) {

            pcount.setVisibility(View.VISIBLE);
            int count = callData.getUnits().size() - 1;
            String name = callData.getUnits().get(0).getName();
            pcount.setText(name + " 외 " + count + "명");
        }else{

            pcount.setVisibility(View.GONE);
        }

        int count = 0;
        for (SmsUnitDTO sud : callData.getUnits()) {
            count += sud.getSmss().size();
        }
        mcount.setText(String.format("총 %d개의 메세지", count));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSmsGroupItemClick(item);
            }
        });
    }

    @Override
    public void bindTag(ArrayList<FriendDTO> users) {

    }
}
