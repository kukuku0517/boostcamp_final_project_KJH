package com.example.android.selfns.DailyView.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.android.selfns.Data.DTO.Group.NotifyGroupDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.LoginView.UserDTO;
import com.example.android.selfns.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by samsung on 2017-07-29.
 */

public class VHNotifyGroup extends DayViewHolder {

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
    @BindView(R.id.notify_group_cv)
    View view;
    @BindView(R.id.notify_group_pcount)
    TextView pcount;
    @BindView(R.id.notify_group_mcount)
    TextView mcount;


    private Context context;
    public VHNotifyGroup(View view, Context context) {
        super(view);
        this.context = context;
        setmListener(context, nListener);
    }

    @Override
    public void bindType(final BaseDTO item) {
        NotifyGroupDTO notifyData = (NotifyGroupDTO) item;
        date.setText(DateHelper.getInstance().toDateString("hh:mm",notifyData.getDate()));
        ampm.setText(DateHelper.getInstance().isAm(notifyData.getDate()));

        int size = notifyData.getUnits().size();
        if (size != 0) {
            int count = notifyData.getUnits().size() - 1;
            String name = notifyData.getUnits().get(0).getName();
            pcount.setText(name + " 외 " + count + "명");
        } else {
            pcount.setText("");
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNotifyItemClick(item);
            }
        });
    }
    @Override
    public void bindTag(ArrayList<UserDTO> users) {

    }
}
