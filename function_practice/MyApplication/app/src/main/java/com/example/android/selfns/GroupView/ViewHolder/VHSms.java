package com.example.android.selfns.GroupView.ViewHolder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.selfns.Data.DTO.Group.GlideApp;
import com.example.android.selfns.Data.DTO.Group.SmsUnitDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.GroupData.SmsUnitData;
import com.example.android.selfns.ExtraView.Comment.CommentBtnClickListener;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Helper.RealmHelper;
import com.example.android.selfns.R;
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
    @BindView(R.id.sms_count)
    TextView count;

    @BindView(R.id.sms_write)
    ImageButton button;
    @BindView(R.id.sms_delete)
    ImageButton deleteBtn;
    @BindView(R.id.sms_iv)
    ImageView iv;

    CommentBtnClickListener mListener;
    Context context;

    public VHSms(View itemView, CommentBtnClickListener listener, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        this.context = context;
    }

    public void bindType(final BaseDTO item) {
        final SmsUnitDTO smsUnitData = (SmsUnitDTO) item;
        GlideApp.with(context).load(R.drawable.icon_account).into(iv);
        person.setText(smsUnitData.getName());
        number.setText(smsUnitData.getAddress());
        content.setText(smsUnitData.getComment());
        count.setText(String.valueOf(smsUnitData.getCount()));
//        comment.setText(smsUnitData.getComment());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                content.setText(comment.getText());
//                mListener.onClick(NotifyUnitData.class,item, comment.getText().toString());
//                ItemInteractionUtil.getInstance().show((UnitActivity)context,smsUnitData.getId(),SmsUnitData.class);
                ItemInteractionUtil.getInstance(context).show((AppCompatActivity) context, smsUnitData.getId(), RealmClassHelper.getInstance().SMS_UNIT_DATA);

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmHelper.getInstance().smsUnitDataDelete(smsUnitData);
            }
        });


    }
}
