package com.example.android.selfns.LoginView;

import android.app.DialogFragment;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.selfns.Data.DTO.Detail.DatePinDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendAddDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.RetrofitHelper;
import com.example.android.selfns.R;

import org.json.JSONException;

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
    @BindView(R.id.friend_tag)
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

    public void bindType(final FriendDTO user) {
        name.setText(user.getName());
        title.setText(user.getId());
        Glide.with(context).load(user.getPhotoUrl()).into(profile);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendAddDTO friendAddDTO = new FriendAddDTO();
                friendAddDTO.setId(user.getId());
                RetrofitHelper.getInstance(context).tagFriend(item.get_id(), friendAddDTO);

            }
        });

    }
}
