package com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.contentproviderbroadcastreceiver.DailyView.Data.PhotoGroupData;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmHelper;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Interface.NotifyListener;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by samsung on 2017-08-03.
 */

public class VHPhotoGroup extends DayViewHolder {
    @BindView(R.id.photo_group_iv)
    ImageView iv;
    @BindView(R.id.photo_group_date)
    TextView date;
    @BindView(R.id.photo_group_number)
    TextView number;

    @BindView(R.id.photo_group_location)
    TextView location;
    @BindView(R.id.photo_group_button)
    Button button;
    @BindView(R.id.photo_group_cv)
    View view;


    public VHPhotoGroup(View view, Context context, NotifyListener nListener) {
        super(view);
        setmListener(context,nListener);
    }

    @Override
    public void bindType(final MyRealmObject item) {
        final PhotoGroupData callData = (PhotoGroupData) item;
        DateFormat sdFormat = new SimpleDateFormat("hh : mm");
        Date d = new Date(callData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);
        location.setText(callData.getPlace());
        int photoNum = callData.getPhotoss().size();
        number.setText(String.valueOf(photoNum) + "장");
//        iv.setImageResource(R.drawable.ic_image_black_24dp);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPhotoGroupItemClick(item);
            }
        });
        Glide.with((Context) mListener).load(callData.getPhotoss().get(0).getPath()).into(iv);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmHelper.getInstance().photoGroupDataDelete(callData);
            }
        });
    }
}
