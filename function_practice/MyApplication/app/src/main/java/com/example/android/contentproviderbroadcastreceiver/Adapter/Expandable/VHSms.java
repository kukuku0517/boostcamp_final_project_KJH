package com.example.android.contentproviderbroadcastreceiver.Adapter.Expandable;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-08-02.
 */

public class VHSms extends AbstractExpandableItemViewHolder {
    @BindView(R.id.sms_person)
    TextView person;
    @BindView(R.id.sms_number)
    TextView number;
    @BindView(R.id.sms_content)
    TextView content;
    @BindView(R.id.sms_write)
    Button button;

    public VHSms(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindType(MyRealmObject item) {
        SmsUnitData smsUnitData = (SmsUnitData) item;
        person.setText(smsUnitData.getName());
        number.setText(smsUnitData.getAddress());
        content.setText(smsUnitData.getContent());
    }
}