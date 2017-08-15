package com.example.android.selfns.LoginView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-08-14.
 */

public class VHFriendAdd extends VHFriendBase {

    @BindView(R.id.friend_name)
    TextView name;
    @BindView(R.id.friend_title)
    TextView title;
    @BindView(R.id.friend_add)
    Button add;

    @BindView(R.id.friend_profile)
    ImageView profile;


    private Context context;
    private boolean isExpanded = false;
    private ShareableDTO item;

    public VHFriendAdd(View view, Context context, ShareableDTO item) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기
        this.context = context;
        this.item = item;
    }

    public void bindType(final UserDTO user) {
        name.setText(user.getName());
        title.setText(user.getId());
        Glide.with(context).load(user.getPhotoUrl()).into(profile);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).addFriend(item,user);
            }
        });

    }
}
