package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.animation.ObjectAnimator;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.NotifyData;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by samsung on 2017-07-29.
 */

public class VHNotifyGroup extends DayViewHolder {
    @BindView(R.id.notify_group_date)
    TextView date;
    @BindView(R.id.notify_group_person)
    TextView person;
    @BindView(R.id.notify_group_content)
    TextView content;
    @BindView(R.id.notify_group_button)
    Button button;
    @BindView(R.id.notify_cv)
    CardView cv;

    public VHNotifyGroup(View view) {
        super(view);
    }

    @Override
    public void bindType(final MyRealmObject item) {
        NotifyGroupData notifyData = (NotifyGroupData) item;
        DateFormat sdFormat = new SimpleDateFormat("HH : mm");
        Date d = new Date(notifyData.getDate());
        String tempDate = sdFormat.format(d);

        date.setText("~"+tempDate);

        int size = notifyData.getUnits().size();
        if(size!=0){
            int count =  notifyData.getUnits().size()-1;
            String name =  notifyData.getUnits().get(0).getName();
            person.setText(name+" 외 "+count+"명");
        }else{
            person.setText("");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNotifyItemClick(item);
            }
        });
    }
//
//    private void onClickButton() {
//
//        if (content.getVisibility() == View.VISIBLE) {
//            Log.d("smsAni", "on");
//            createRotateAnimator(content, 1f, 0f).start();
//            createRotateAnimator(iv, 1f, 0f).start();
//            createRotateAnimator(button, 1f, 0f).start();
//            content.setVisibility(View.GONE);
//            iv.setVisibility(View.GONE);
//            button.setVisibility(View.GONE);
////            expandState.put(i, false);
//        } else {
//            Log.d("smsAni", "off");
//            createRotateAnimator(content, 0f, 1f).start();
//            createRotateAnimator(iv, 0f, 1f).start();
//            createRotateAnimator(button, 0f, 1f).start();
//            content.setVisibility(View.VISIBLE);
//            iv.setVisibility(View.VISIBLE);
//            button.setVisibility(View.VISIBLE);
////            expandState.put(i, true);
//        }
//    }

    //Code to rotate button
    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "alpha", from, to, 1).ofInt(target, "bottom", 1, 0);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }
}
