package com.example.android.contentproviderbroadcastreceiver.DailyView.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.DayViewHolder;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHCall;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHGps;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHNotifyGroup;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHPhotoGroup;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHSmsGroup;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHSmsTrade;
import com.example.android.contentproviderbroadcastreceiver.GroupView.ViewHolder.VHPhoto;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.gvillani.pinnedlist.GroupListWrapper;
import com.gvillani.pinnedlist.PinnedAdapter;
import com.gvillani.pinnedlist.PinnedListLayout;
import com.gvillani.pinnedlist.PinnedViewHolder;

import io.realm.RealmObject;

/**
 * Created by samsung on 2017-08-09.
 */

public class CalendarPinnedAdapter extends PinnedAdapter  {
Context context;
    public CalendarPinnedAdapter(Context context, GroupListWrapper listGroupWrapper, PinnedListLayout layout) {
        super(listGroupWrapper, layout);
        this.context=context;

    }

    @Override
    public PinnedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutIdForListItem;
        View view;
        DayViewHolder holder;
        switch (viewType) {
            case 0: //call
                layoutIdForListItem = R.layout.item_call;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHCall(view,context);
            case 1: //gps
                layoutIdForListItem = R.layout.item_gps;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHGps(view,context);
            case 2: //photo group
                layoutIdForListItem = R.layout.item_photo_group;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHPhotoGroup(view,context);

                return holder;
            case 3: //notigroup
                Log.d("Notification", "layout");
                layoutIdForListItem = R.layout.item_notify_group;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHNotifyGroup(view,context);

                return holder;
            case 4: //sms group
                layoutIdForListItem = R.layout.item_sms_group;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHSmsGroup(view,context);

                return holder;
            case 5: //photo
                layoutIdForListItem = R.layout.item_photo;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHPhoto(view,context);
//                holder.setmListener(context);
                return holder;
            case 6: //sms trade
                layoutIdForListItem = R.layout.item_sms_trade;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHSmsTrade(view,context);

                return holder;
            default:
                return null;
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        RealmObject item = (RealmObject) mListWrapper.getItem(position);
            DayViewHolder dayHolder = (DayViewHolder) holder;
            dayHolder.bindType((MyRealmObject) item);
        }



    @Override
    public int getItemViewType(int position) {
        MyRealmObject item = (MyRealmObject) mListWrapper.getItem(position);
        return item.getType();
    }

}
