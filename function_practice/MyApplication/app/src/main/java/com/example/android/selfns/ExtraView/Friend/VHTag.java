package com.example.android.selfns.ExtraView.Friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.selfns.Data.DTO.Group.GlideApp;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by samsung on 2017-08-16.
 */

public class VHTag extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_tag)
    CircleImageView tag;
    @BindView(R.id.tv_tag)
    TextView tv;

    RecyclerView.LayoutManager layoutManager;
    TagAdapter adapter;
    List<UserDTO> items;

    private Context context;
    private boolean isExpanded = false;

    public VHTag(View view, Context context) {
        super(view);
        ButterKnife.bind(this,view);
        this.context = context;
    }

    public void setItems(List<UserDTO> items) {
        this.items = items;
    }

    public void bindType(final FriendDTO user) {


        tv.setText(user.getId());
        if(user.getPhotoUrl()!=null){

//            GlideApp.with(context).load(user.getPhotoUrl()).error(R.drawable.ic_person_black_24dp).override(40,40).transform(new CropCircleTransformation(context)).into(tag);
//            GlideApp.with(context).load(user.getPhotoUrl()).transform(new CropCircleTransformation(context)).into(tag);
            GlideApp.with(context).load(user.getPhotoUrl()).into(tag);
        }else{
            GlideApp.with(context).load(R.drawable.com_facebook_button_icon).transform(new CropCircleTransformation(context)).into(tag);
        }


    }

}