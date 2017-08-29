package com.example.android.selfns.LoginView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.selfns.Data.DTO.Retrofit.FriendAddDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.R;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by samsung on 2017-08-14.
 */

public class FriendAdapter extends RecyclerView.Adapter<VHFriendBase> {

    private List<FriendDTO> items=new ArrayList<>();
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

        int layoutIdForListItem;
        View view;
        switch (viewType) {
            case 0: //friendList
                layoutIdForListItem = R.layout.item_friend;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHFriend(view, context);
            case 1: //friendTagList
                layoutIdForListItem = R.layout.item_friend_add;
                view = LayoutInflater.from(context).inflate(layoutIdForListItem, parent, false);
                return new VHFriendAdd(view, context,item);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(VHFriendBase holder, int position) {
        FriendDTO item = items.get(position);

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

    public void updateItem(ArrayList<FriendDTO> items) {
        this.items = items;

    }

}
