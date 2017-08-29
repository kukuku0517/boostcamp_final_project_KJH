package com.example.android.selfns.DailyView.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.selfns.Data.DTO.Group.GlideApp;
import com.example.android.selfns.Data.DTO.Group.GpsGroupDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.ExtraView.Friend.TagAdapter;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.R;
import com.fmsirvent.ParallaxEverywhere.PEWImageView;
import com.github.vipulasri.timelineview.LineType;
import com.github.vipulasri.timelineview.TimelineView;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-08-11.
 */

public class VHGpsGroup extends DayViewHolder {

    //공통
    @BindView(R.id.item_am_pm)
    TextView ampm;
    @BindView(R.id.item_date)
    TextView date;
    @BindView(R.id.item_time_marker)
    TimelineView tlv;

    //공통 버튼
    @BindView(R.id.item_highlight)
    ShineButton highlightBtn;
    @BindView(R.id.item_delete)
    ImageButton deleteBtn;
    @BindView(R.id.item_edit)
    ImageButton editBtn;
    @BindView(R.id.item_tag)
    ImageButton tagBtn;
    //공통 메뉴
    @BindView(R.id.item_people)
    View peopleView;
    @BindView(R.id.item_hide_menu)
    View hideMenu;
    @BindView(R.id.item_share)
    ImageButton shareBtn;

    //고유 레이아웃
    @BindView(R.id.gps_group_cv)
    View view;
    @BindView(R.id.rv_tag)
    RecyclerView rvTag;
    //고유 뷰
    @BindView(R.id.gps_group_comment)
    TextView comment;
    @BindView(R.id.gps_group_departure)
    TextView departure;
    @BindView(R.id.gps_group_iv)
    PEWImageView iv;
    @BindView(R.id.gps_group_moving)
    TextView moving;
    @BindView(R.id.gps_group_place)
    TextView place;

    RecyclerView.LayoutManager layoutManager;
    TagAdapter adapter;
    List<FriendDTO> items = new ArrayList<>();

    private Context context;
    private boolean isExpanded = false;

    public VHGpsGroup(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기
        this.context = context;
        setmListener(context, nListener);
        tlv.initLine(LineType.NORMAL);

        GlideApp.with(context).load(R.drawable.travel).override(1080,675).into(iv);
    }

    @Override
    public void bindType(final BaseDTO item) {
        final GpsGroupDTO callData = (GpsGroupDTO) item;

        date.setText(DateHelper.getInstance().toDateString("hh:mm", callData.getDate()));
        ampm.setText(DateHelper.getInstance().isAm(callData.getDate()));
        comment.setText(callData.getComment());
        place.setText(callData.getPlace());
        highlightBtn.init((Activity) context);

        //shareable
        if (callData.getShare() == 1) {
            highlightBtn.setBtnFillColor(Color.RED);     highlightBtn.setBtnColor(Color.RED);
            highlightBtn.setChecked(true);
        } else if (callData.getHighlight() == 1) {
            highlightBtn.setChecked(true);
        } else {
            highlightBtn.setChecked(false);
        }
        highlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callData.getShare() != 1) {
                    ItemInteractionUtil.getInstance(context).highlight(callData);
                }
            }
        });

        if (callData.isStart()) {
            departure.setText("출발");
            tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_arrow_drop_down_circle_black_24dp, null));
            if (callData.getEndId() == -1) {
                moving.setText("이동중 ...");
            } else {
                moving.setText("");
            }
        } else {
            departure.setText("도착");
            tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_remove_circle_black_24dp, null));
            moving.setText("");
        }

        tagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callData.getShare() == 1) {
                    ItemInteractionUtil.getInstance(context).tagFriend((AppCompatActivity) context, item);

                }
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callData.getShare() == 0) {
                    ItemInteractionUtil.getInstance(context).shareItem(callData);
                }
            }
        });


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGpsGroupItemClick(item);
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callData.getShare()==0){

                    ItemInteractionUtil.getInstance(context).shareItem(callData);
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    hideMenu.setVisibility(View.GONE);
                } else {
                    hideMenu.setVisibility(View.VISIBLE);
                }
                isExpanded = !isExpanded;
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).deleteGpsGroupItem(callData);
            }
        });

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        adapter = new TagAdapter(context);
        adapter.updateItem(items);
        rvTag.setHasFixedSize(true);
        rvTag.setLayoutManager(layoutManager);
        rvTag.setAdapter(adapter);


    }


    @Override
    public void bindTag(ArrayList<FriendDTO> users) {
        this.items = users;
    }
}
