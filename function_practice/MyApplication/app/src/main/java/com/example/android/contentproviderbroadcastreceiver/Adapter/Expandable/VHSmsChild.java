package com.example.android.contentproviderbroadcastreceiver.Adapter.Expandable;

import android.view.View;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.SmsData;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.name;

/**
 * Created by samsung on 2017-08-02.
 */

public class VHSmsChild extends AbstractExpandableItemViewHolder {

    @BindView(R.id.sms_child_content)
    TextView content;
    @BindView(R.id.sms_child_time)
    TextView date;

    public VHSmsChild(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindType(SmsData child) {

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
