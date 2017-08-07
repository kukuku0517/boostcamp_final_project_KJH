package com.example.android.contentproviderbroadcastreceiver.Adapter.Expandable;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Interface.CommentBtnClickListener;
import com.example.android.contentproviderbroadcastreceiver.Interface.CommentUtil;
import com.example.android.contentproviderbroadcastreceiver.Main.CommentDialogFragment;
import com.example.android.contentproviderbroadcastreceiver.Main.UnitActivity;
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
    @BindView(R.id.sms_comment)
    TextView comment;
    @BindView(R.id.sms_write)
    Button button;

    CommentBtnClickListener mListener;
    Context context;

    public VHSms(View itemView, CommentBtnClickListener listener, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        this.context = context;
    }

    public void bindType(final MyRealmObject item) {
        final SmsUnitData smsUnitData = (SmsUnitData) item;
        person.setText(smsUnitData.getName());
        number.setText(smsUnitData.getAddress());
        content.setText(smsUnitData.getContent());

        comment.setText(smsUnitData.getComment());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                content.setText(comment.getText());
//                mListener.onClick(NotifyUnitData.class,item, comment.getText().toString());
//                CommentUtil.getInstance().show((UnitActivity)context,smsUnitData.getId(),SmsUnitData.class);
                CommentUtil.getInstance().show((UnitActivity)context,smsUnitData);

            }
        });

    }
}
