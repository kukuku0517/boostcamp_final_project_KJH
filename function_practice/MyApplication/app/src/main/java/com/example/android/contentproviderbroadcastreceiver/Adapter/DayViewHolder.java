package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Interface.CardItemClickListener;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-07-26.
 */

public abstract class DayViewHolder extends RecyclerView.ViewHolder {
    public CardItemClickListener mListener;

    public DayViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    void setmListener(Context context){
        mListener = (CardItemClickListener) context;
    }
    public abstract void bindType(MyRealmObject item);

}
