package com.example.android.selfns.LoginView;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.selfns.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsung on 2017-08-14.
 */

public class FriendAdapter extends RecyclerView.Adapter<VHFriend> {

    private List<UserDTO> items;
    private Context context;

    public FriendAdapter(Context context) {
        this.context = context;
    }

    @Override
    public VHFriend onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem;
        View view;
        switch (viewType) {
            case 0: //call
                layoutIdForListItem = R.layout.item_friend;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHFriend(view, context);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(VHFriend holder, int position) {
        UserDTO item = items.get(position);

        holder.bindType(item);

    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        UserDTO item = items.get(position);
        return item.getType();
    }

    public void updateItem(ArrayList<UserDTO> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}
