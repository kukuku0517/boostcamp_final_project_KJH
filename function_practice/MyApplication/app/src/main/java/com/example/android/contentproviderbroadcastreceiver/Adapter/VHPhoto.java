package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.PhotoData;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-07-26.
 */

public class VHPhoto extends DayViewHolder {
    @BindView(R.id.photo_iv)
    ImageView iv;
    @BindView(R.id.photo_date)
    TextView date;
    @BindView(R.id.photo_location)
    TextView location;

    @BindView(R.id.photo_button)
    Button button;


    public VHPhoto(View view) {
        super(view);


    }

    @Override
    public void bindType(MyRealmObject item) {
       PhotoData callData = (PhotoData) item;
        DateFormat sdFormat = new SimpleDateFormat("hh : mm");
        Date d = new Date(callData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);
        location.setText(callData.getPath());

        iv.setImageResource(R.drawable.ic_image_black_24dp);
    }
}
