package com.example.android.selfns.ExtraView.Friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsung on 2017-08-16.
 */

public class TagAdapter extends RecyclerView.Adapter<VHTag> {

    private List<FriendDTO> items=new ArrayList<>();
    private Context context;

    public TagAdapter(Context context) {
        this.context = context;

    }

    @Override
    public VHTag onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem;
        View view;
        layoutIdForListItem = R.layout.item_tag;
        view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
        return new VHTag(view, context);


    }

    @Override
    public void onBindViewHolder(VHTag holder, int position) {
       FriendDTO item = items.get(position);
        holder.bindType(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItem(List<FriendDTO> items){
        this.items=items;
    }
}
