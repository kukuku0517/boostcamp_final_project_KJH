package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.view.View;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.PhotoData;
import com.example.android.contentproviderbroadcastreceiver.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-07-26.
 */

public class VHPhoto extends DayViewHolder {

    public VHPhoto(View view) {
        super(view);


    }

    @Override
    public void bindType(MyRealmObject item) {
       PhotoData callData = (PhotoData) item;
        tv2.setText(String.valueOf(callData.getDate()));
        tv3.setText(callData.getPath());

        iv.setImageResource(R.drawable.ic_image_black_24dp);
    }
}
