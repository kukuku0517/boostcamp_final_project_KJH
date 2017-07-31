package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.util.ArrayList;

import io.realm.Realm;

import static android.media.CamcorderProfile.get;

/**
 * Created by samsung on 2017-07-26.
 */

public class DayAdapter extends RecyclerView.Adapter<DayViewHolder>{


    private ArrayList<MyRealmObject> items;
    private Context context;
    private Realm realm;

    public DayAdapter(Context context, Realm realm) {
        this.context = context;
        this.realm=realm;
    }
    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem;
        View view;
        switch(viewType){
            case 0:
                layoutIdForListItem = R.layout.call_item;
                view =  LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHCall(view);
            case 1:
                layoutIdForListItem = R.layout.photo_item;
                view =  LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHPhoto(view);
            case 2:
                layoutIdForListItem = R.layout.sms_item;
                view =  LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHSms(view);
            case 3:
                Log.d("Notification","layout");
                layoutIdForListItem = R.layout.notify_item;
                view =  LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHNotify(view);

            default:
                return null;


        }

    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        MyRealmObject item=items.get(position);
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



}
