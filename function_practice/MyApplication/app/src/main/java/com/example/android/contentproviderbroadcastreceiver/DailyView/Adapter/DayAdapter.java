package com.example.android.contentproviderbroadcastreceiver.DailyView.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.DayViewHolder;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHCall;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHGps;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHNotifyGroup;
import com.example.android.contentproviderbroadcastreceiver.GroupView.ViewHolder.VHPhoto;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHPhotoGroup;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHSmsGroup;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHSmsTrade;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Interface.NotifyListener;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.util.ArrayList;
import java.util.logging.LogRecord;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmObject;

import static android.media.CamcorderProfile.get;

/**
 * Created by samsung on 2017-07-26.
 */

public class DayAdapter extends RecyclerView.Adapter<DayViewHolder> implements NotifyListener{

    private RealmList<MyRealmObject> items;
    private Realm realm;

    public DayAdapter(Context context, Realm realm) {
        this.context = context;
        this.realm = realm;
        final Handler handler = new Handler();
//        realm.addChangeListener(new RealmChangeListener<Realm>() {
//            @Override
//            public void onChange(Realm realm) {
//                Thread t = new Thread(new Runnable(){
//
//                    @Override
//                    public void run() {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                notifyDataSetChanged();
//                            }
//                        });
//                    }
//                });
//            }
//        });

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
                return new VHCall(view,context,this);
            case 1: //gps
                layoutIdForListItem = R.layout.gps_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHGps(view,context,this);
            case 2: //photo group
                layoutIdForListItem = R.layout.photo_group_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHPhotoGroup(view,context,this);

                return holder;
            case 3: //notigroup
                Log.d("Notification", "layout");
                layoutIdForListItem = R.layout.notify_group_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHNotifyGroup(view,context,this);

                return holder;
            case 4: //sms group
                layoutIdForListItem = R.layout.sms_group_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHSmsGroup(view,context,this);

                return holder;
            case 5: //photo
                layoutIdForListItem = R.layout.photo_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHPhoto(view,context,this);
//                holder.setmListener(context);
                return holder;
            case 6: //sms trade
                layoutIdForListItem = R.layout.sms_trade_item;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHSmsTrade(view,context,this);

                return holder;
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        RealmObject item = (RealmObject)items.get(position);
        if(item.isValid()){
            holder.bindType((MyRealmObject) item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        MyRealmObject item = items.get(position);
        return item.getType();
    }

    public void updateItem(RealmList<MyRealmObject> item) {
        this.items = item;
        notifyDataSetChanged();
    }


    private Context context;

    @Override
    public void onNotify() {
        notifyDataSetChanged();
    }
}
