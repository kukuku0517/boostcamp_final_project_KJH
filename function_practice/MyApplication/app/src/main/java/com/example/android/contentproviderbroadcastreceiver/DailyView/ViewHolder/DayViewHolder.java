package com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.contentproviderbroadcastreceiver.DailyView.Adapter.DayAdapter;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Interface.CardItemClickListener;
import com.example.android.contentproviderbroadcastreceiver.Interface.NotifyListener;
import com.gvillani.pinnedlist.PinnedViewHolder;

import butterknife.ButterKnife;
import io.realm.RealmObject;

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
