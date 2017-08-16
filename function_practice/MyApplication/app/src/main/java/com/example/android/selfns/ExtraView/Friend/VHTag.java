package com.example.android.selfns.ExtraView.Friend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.selfns.DailyView.Adapter.DayAdapter;
import com.example.android.selfns.DailyView.ViewHolder.DayViewHolder;
import com.example.android.selfns.Data.DTO.Detail.SmsTradeDTO;
import com.example.android.selfns.Data.DTO.Group.GlideApp;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.LoginView.UserDTO;
import com.example.android.selfns.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static java.lang.System.load;

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

    public void bindType(final UserDTO user) {


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