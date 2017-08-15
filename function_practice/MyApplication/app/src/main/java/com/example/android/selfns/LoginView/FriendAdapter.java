package com.example.android.selfns.LoginView;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsung on 2017-08-14.
 */

public class FriendAdapter extends RecyclerView.Adapter<VHFriendBase> {

    private List<UserDTO> items;
    private Context context;
    private int type;
    ShareableDTO item;

    public FriendAdapter(Context context,int type) {
        this.context = context;
        this.type = type;
    }
    public FriendAdapter(Context context, int type, ShareableDTO item) {
        this.context = context;
        this.type = type;
        this.item=item;
    }

    @Override
    public VHFriendBase onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem;
        View view;
        switch (viewType) {
            case 0: //call
                layoutIdForListItem = R.layout.item_friend;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHFriend(view, context);
            case 1: //call
                layoutIdForListItem = R.layout.item_friend_add;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHFriendAdd(view, context,item);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(VHFriendBase holder, int position) {
        UserDTO item = items.get(position);

        holder.bindType(item);

    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
//        UserDTO item = items.get(position);
//        return item.getType();
        return type;
    }

    public void updateItem(ArrayList<UserDTO> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}
