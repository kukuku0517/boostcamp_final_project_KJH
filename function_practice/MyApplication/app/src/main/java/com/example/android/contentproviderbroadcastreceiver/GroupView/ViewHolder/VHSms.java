package com.example.android.contentproviderbroadcastreceiver.GroupView.ViewHolder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.SmsUnitData;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.ExtraView.Comment.CommentBtnClickListener;
import com.example.android.contentproviderbroadcastreceiver.Helper.CommentUtil;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmDataHelper;
import com.example.android.contentproviderbroadcastreceiver.GroupView.UnitActivity;
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
                CommentUtil.getInstance().show((AppCompatActivity) context,smsUnitData.getId(), RealmDataHelper.getInstance().SMS_UNIT_DATA);

            }
        });

    }
}
