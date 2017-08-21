package com.example.android.selfns.DailyView.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.android.selfns.DailyView.ViewHolder.DayViewHolder;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmObject;
import com.example.android.selfns.DetailView.ViewHolder.VHCall;
import com.example.android.selfns.DetailView.ViewHolder.VHCustom;
import com.example.android.selfns.DetailView.ViewHolder.VHGps;
import com.example.android.selfns.DailyView.ViewHolder.VHGpsGroup;
import com.example.android.selfns.DailyView.ViewHolder.VHNotifyGroup;
import com.example.android.selfns.ExtraView.Comment.CommentBtnClickListener;
import com.example.android.selfns.GroupView.ViewHolder.VHNotify;
import com.example.android.selfns.GroupView.ViewHolder.VHNotifyChild;
import com.example.android.selfns.GroupView.ViewHolder.VHPhoto;
import com.example.android.selfns.DailyView.ViewHolder.VHPhotoGroup;
import com.example.android.selfns.DailyView.ViewHolder.VHSmsGroup;
import com.example.android.selfns.DetailView.ViewHolder.VHSmsTrade;
import com.example.android.selfns.GroupView.ViewHolder.VHSms;
import com.example.android.selfns.GroupView.ViewHolder.VHSmsChild;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.LoginView.UserDTO;
import com.example.android.selfns.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;

import static android.media.CamcorderProfile.get;

/**
 * Created by samsung on 2017-07-26.
 */

public class DayAdapter extends RecyclerView.Adapter<DayViewHolder> implements CommentBtnClickListener {

    private Context context;

    private ArrayList<BaseDTO> items;
    private Realm realm;
    HashMap<Integer, List<UserDTO>> usersHash=new HashMap<>();
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
            case RealmClassHelper.CALL_DATA: //call
                layoutIdForListItem = R.layout.item_call;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHCall(view, context);

            case RealmClassHelper.GPS_GROUP_DATA:// gps group
                layoutIdForListItem = R.layout.item_gps_group;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHGpsGroup(view, context);

            case RealmClassHelper.PHOTO_GROUP_DATA: //photo group
                layoutIdForListItem = R.layout.item_photo_group;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHPhotoGroup(view, context);

                return holder;
            case RealmClassHelper.NOTIFY_GROUP_DATA: //notigroup
                Log.d("Notification", "layout");
                layoutIdForListItem = R.layout.item_notify_group;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHNotifyGroup(view, context);

                return holder;
            case RealmClassHelper.SMS_GROUP_DATA: //sms group
                layoutIdForListItem = R.layout.item_sms_group;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHSmsGroup(view, context);
                return holder;
            case RealmClassHelper.PHOTO_DATA: //photo
                layoutIdForListItem = R.layout.item_photo;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHPhoto(view, context);
//                holder.setmListener(context);
                return holder;
            case RealmClassHelper.SMS_TRADE_DATA: //sms trade
                layoutIdForListItem = R.layout.item_sms_trade;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                holder = new VHSmsTrade(view, context);
                return holder;
            case RealmClassHelper.GPS_DATA: //gps
                layoutIdForListItem = R.layout.item_gps;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHGps(view, context);
            case RealmClassHelper.CUSTOM_DATA: //custom
                layoutIdForListItem = R.layout.item_custom;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHCustom(view, context);
            case RealmClassHelper.SMS_DATA: //custom
                layoutIdForListItem = R.layout.item_sms_child;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHSmsChild(view);
            case RealmClassHelper.NOTIFY_DATA: //custom
                layoutIdForListItem = R.layout.item_sms_child;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHNotifyChild(view);
            case RealmClassHelper.SMS_UNIT_DATA:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms, parent, false);
                return new VHSms(view, this, context);
            case RealmClassHelper.NOTIFY_UNIT_DATA:
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms, parent, false);
                return new VHNotify(view, this, context);

            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        BaseDTO item = (BaseDTO) items.get(position);
        holder.bindType(item);
        if (usersHash.containsKey(position)) {
            holder.bindTag((ArrayList<UserDTO>) usersHash.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        BaseDTO item = items.get(position);
        return item.getType();
    }


    public void updateItem(ArrayList<BaseDTO> item) {
        this.items = item;
        notifyDataSetChanged();
    }

    public void updateHashItem(HashMap<Integer, List<UserDTO>> usersHash){
        this.usersHash = usersHash;
    }

    @Override
    public void onClick(Class c, MyRealmObject item, String text) {

    }
}
