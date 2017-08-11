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
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHGpsGroup;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHNotifyGroup;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHPhotoGroup;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHPin;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHSmsGroup;
import com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder.VHSmsTrade;
import com.example.android.contentproviderbroadcastreceiver.GroupView.ViewHolder.VHPhoto;
import com.example.android.contentproviderbroadcastreceiver.Helper.PinnedHeaderItemDecoration;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by samsung on 2017-08-09.
 */

public class CalendarPinAdapter  extends RecyclerView.Adapter<DayViewHolder> implements PinnedHeaderItemDecoration.PinnedHeaderAdapter {

    private RealmList<MyRealmObject> items;
    private Realm realm;

    public CalendarPinAdapter(Context context, Realm realm) {
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
                layoutIdForListItem = R.layout.item_call;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHCall(view,context);
//            case 1: //gps
//                layoutIdForListItem = R.layout.item_gps;
//                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
//                return new VHGps(view,context);
            case 1: //gps
                layoutIdForListItem = R.layout.item_gps_group;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHGpsGroup(view,context);

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

            case 7: //pin
                layoutIdForListItem = R.layout.item_pin;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHPin(view,context);

                return holder;
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        MyRealmObject item = (MyRealmObject)items.get(position);

            holder.bindType((MyRealmObject) item);

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
    public boolean isPinnedViewType(int viewType) {
        if (viewType == 7) {
            return true;
        } else {
            return false;
        }
    }
}
