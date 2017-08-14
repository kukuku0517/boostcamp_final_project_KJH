package com.example.android.selfns.DailyView.ViewHolder;

import android.content.Context;
import android.view.View;

import com.example.android.selfns.Interface.MyRealmObject;
import com.example.android.selfns.Interface.CardItemClickListener;
import com.example.android.selfns.Interface.NotifyListener;
import com.gvillani.pinnedlist.PinnedViewHolder;

import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-07-26.
 */

public abstract class DayViewHolder extends PinnedViewHolder{
    public CardItemClickListener mListener;
    public NotifyListener nListener;

    public DayViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setmListener(Context context, NotifyListener nListener){
        mListener = (CardItemClickListener) context;
        this.nListener=nListener;
    }
    public abstract void bindType(MyRealmObject item);




}
