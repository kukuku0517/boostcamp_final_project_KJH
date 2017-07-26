package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-07-26.
 */

public abstract class DayViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textView2)
    TextView tv2;
    @BindView(R.id.textView3)
    TextView tv3;
    @BindView(R.id.textView4)
    TextView tv4;
    @BindView(R.id.textView5)
    TextView tv5;
    @BindView(R.id.imageView)
    ImageView iv;

    public DayViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
    public abstract void bindType(MyRealmObject item);

}
