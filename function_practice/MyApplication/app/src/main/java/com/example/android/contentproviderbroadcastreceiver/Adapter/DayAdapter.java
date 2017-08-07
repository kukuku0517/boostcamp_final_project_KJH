package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Interface.CardItemClickListener;
import com.example.android.contentproviderbroadcastreceiver.Interface.PhotoItemClickListener;
import com.example.android.contentproviderbroadcastreceiver.Main.PhotoActivity;
import com.example.android.contentproviderbroadcastreceiver.Main.UnitActivity;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.util.ArrayList;

import io.realm.Realm;

import static android.media.CamcorderProfile.get;

/**
 * Created by samsung on 2017-07-26.
 */

public class DayAdapter extends RecyclerView.Adapter<DayViewHolder>  {

    private ArrayList<MyRealmObject> items;
    private Realm realm;

    public DayAdapter(Context context, Realm realm) {
        this.context = context;
        this.realm = realm;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem;
        View view;
        DayViewHolder holder;
        switch (viewType) {
            case 0: //call
                layoutIdForListItem = R.layout.call_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHCall(view,context);
            case 1: //gps
                layoutIdForListItem = R.layout.gps_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHGps(view,context);
            case 2: //photo group
                layoutIdForListItem = R.layout.photo_group_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHPhotoGroup(view,context);

                return holder;
            case 3: //notigroup
                Log.d("Notification", "layout");
                layoutIdForListItem = R.layout.notify_group_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHNotifyGroup(view,context);

                return holder;
            case 4: //sms group
                layoutIdForListItem = R.layout.sms_group_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHSmsGroup(view,context);

                return holder;
            case 5: //photo
                layoutIdForListItem = R.layout.photo_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHPhoto(view ,context);
//                holder.setmListener(context);
                return holder;
            case 6: //sms trade
                layoutIdForListItem = R.layout.sms_trade_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHSmsTrade(view,context);

                return holder;
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        MyRealmObject item = items.get(position);
        holder.bindType(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    public void updateItem(ArrayList<MyRealmObject> item) {
        this.items = item;
        notifyDataSetChanged();
    }


    private Context context;

}
