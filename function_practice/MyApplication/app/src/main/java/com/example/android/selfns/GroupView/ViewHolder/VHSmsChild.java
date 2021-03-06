package com.example.android.selfns.GroupView.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.android.selfns.Data.DTO.Detail.SmsDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-08-02.
 */

public class VHSmsChild extends MyExpandableItemViewHolder {

    @BindView(R.id.sms_child_content)
    TextView content;
    @BindView(R.id.sms_child_time)
    TextView date;

    public VHSmsChild(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindType(BaseDTO base) {
        SmsDTO child = (SmsDTO) base;
        content.setText(child.getContent());
        DateFormat sdFormat = new SimpleDateFormat("HH:mm");
        Date d = new Date(child.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);
        if (child.isSent()) {
            date.append("보냄");
        } else {
            date.append("받음");
        }
    }
}
