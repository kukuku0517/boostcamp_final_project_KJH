package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.SmsData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static android.R.attr.button;

/**
 * Created by samsung on 2017-07-26.
 */

public class VHSmsGroup extends DayViewHolder {

    @BindView(R.id.sms_group_date)
    TextView date;
    @BindView(R.id.sms_group_person)
    TextView person;
    @BindView(R.id.sms_group_number)
    TextView number;
    @BindView(R.id.sms_group_content)
    TextView content;

    //    @BindView(R.id.sms_button)
//    Button button;
    @BindView(R.id.sms_group_button)
Button button;


    public VHSmsGroup(View view) {
        super(view);


    }

    @Override
    public void bindType(final MyRealmObject item) {
        SmsGroupData callData = (SmsGroupData) item;
        DateFormat sdFormat = new SimpleDateFormat("hh : mm");
        Date d = new Date(callData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);
//        person.setText(callData.getPerson());
//        content.setText(String.valueOf(callData.getContent()));
        number.setText(String.valueOf(callData.getId()));
        Log.d("####", "on");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSmsItemClick(item);
            }
        });
    }

    private void onClickButton() {

        //Simply set View to Gone if not expanded
        //Not necessary but I put simple rotation on button layout
//        if (content.getVisibility() == View.VISIBLE) {
//            Log.d("smsAni", "on");
//            createRotateAnimator(content, 180f, 0f).start();
//            content.setVisibility(View.GONE);
////            expandState.put(i, false);
//        } else {
//            Log.d("smsAni", "off");
//            createRotateAnimator(content, 0f, 180f).start();
//            content.setVisibility(View.VISIBLE);
////            expandState.put(i, true);
//        }


    }

    //Code to rotate button
    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }
}
