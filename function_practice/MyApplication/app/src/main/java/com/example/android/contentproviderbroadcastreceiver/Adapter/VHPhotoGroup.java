package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.PhotoGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.NotifyData;
import com.example.android.contentproviderbroadcastreceiver.Data.PhotoData;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by samsung on 2017-08-03.
 */

public class VHPhotoGroup  extends DayViewHolder {
    @BindView(R.id.photo_iv)
    ImageView iv;
    @BindView(R.id.photo_date)
    TextView date;
    @BindView(R.id.photo_number)
    TextView number;

    @BindView(R.id.photo_location)
    TextView location;
    @BindView(R.id.photo_button)
    Button button;

    public VHPhotoGroup(View view) {
        super(view);
    }

    @Override
    public void bindType(final MyRealmObject item) {
        PhotoGroupData callData = (PhotoGroupData) item;
        DateFormat sdFormat = new SimpleDateFormat("hh : mm");
        Date d = new Date(callData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);
        location.setText(callData.getPlace());
        int photoNum = callData.getPhotoss().size();
        number.setText(String.valueOf(photoNum)+"장");
//        iv.setImageResource(R.drawable.ic_image_black_24dp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPhotoItemClick(item);
            }
        });
        Glide.with((Context)mListener).load(callData.getPhotoss().get(0).getPath()).into(iv);
    }
}
