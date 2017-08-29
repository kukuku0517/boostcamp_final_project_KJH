package com.example.android.selfns.LoginView;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.selfns.Data.DTO.Retrofit.FriendAddDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;

/**
 * Created by samsung on 2017-08-15.
 */

public abstract class VHFriendBase extends RecyclerView.ViewHolder {
    public VHFriendBase(View itemView) {
        super(itemView);
    }

    public abstract void bindType(FriendDTO item);
}
