package com.example.android.selfns.DetailView.ViewHolder;

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

import com.example.android.selfns.DailyView.ViewHolder.DayViewHolder;
import com.example.android.selfns.Data.DTO.Detail.CustomDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.ExtraView.Friend.TagAdapter;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.R;
import com.github.vipulasri.timelineview.LineType;
import com.github.vipulasri.timelineview.TimelineView;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-08-12.
 */

public class VHCustom extends DayViewHolder {

    //공통
    @BindView(R.id.item_am_pm)
    TextView ampm;
    @BindView(R.id.item_date)
    TextView date;
    @BindView(R.id.item_time_marker)
    TimelineView tlv;
    @BindView(R.id.item_time_icon)
    ImageView icon;
    //공통 버튼
    @BindView(R.id.item_highlight)
    ShineButton highlightBtn;
    @BindView(R.id.item_delete)
    ImageButton deleteBtn;
    @BindView(R.id.item_edit)
    ImageButton editBtn;
    @BindView(R.id.item_share)
    ImageButton shareBtn;
    @BindView(R.id.item_tag)
    ImageButton tagBtn;
    //공통 메뉴
    @BindView(R.id.item_people)
    View peopleView;
    @BindView(R.id.item_hide_menu)
    View hideMenu;

    @BindView(R.id.custom_cv)
    View view;
    @BindView(R.id.custom_title)
    TextView title;
    @BindView(R.id.custom_comment)
    TextView comment;
//    @BindView(R.id.custom_tag)
//    ImageView tag;
    @BindView(R.id.rv_tag)
    RecyclerView rvTag;




    private Context context;
    private boolean isExpanded = false;

    RecyclerView.LayoutManager layoutManager;
    TagAdapter adapter;
    List<FriendDTO> items = new ArrayList<>();
    public VHCustom(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기
        this.context = context;
        setmListener(context, nListener);
        tlv.initLine(LineType.NORMAL);
        tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle, null));
        icon.setImageResource(R.drawable.ic_edit_black_24dp);

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        adapter = new TagAdapter(context);
        rvTag.setHasFixedSize(true);
        rvTag.setLayoutManager(layoutManager);
    }


    @Override
    public void bindType(final BaseDTO item) {
        final CustomDTO callData = (CustomDTO) item;

        date.setText(DateHelper.getInstance().toDateString("hh:mm",callData.getDate()));
        ampm.setText(DateHelper.getInstance().isAm(callData.getDate()));
        title.setText(callData.getTitle());
        comment.setText(callData.getComment());
        if (callData.getShare() == 1) {
            highlightBtn.setBtnFillColor(Color.RED);
            highlightBtn.setBtnColor(Color.RED);
            highlightBtn.setChecked(true);
        } else if (callData.getHighlight() == 1) {
            highlightBtn.setBtnFillColor(Color.rgb(249, 168, 37));
            highlightBtn.setBtnColor(Color.rgb(189, 189, 189));
            highlightBtn.setChecked(true);
        } else {
            highlightBtn.setBtnFillColor(Color.rgb(249, 168, 37));
            highlightBtn.setBtnColor(Color.rgb(189, 189, 189));
            highlightBtn.setChecked(false);
        }


        adapter.updateItem(items);
        rvTag.setAdapter(adapter);


        highlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callData.getShare() != 1) {
                    ItemInteractionUtil.getInstance(context).highlight(callData);
                }
            }
        });
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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).deleteItem(callData);
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCustomItemClick(item);
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




    }
    @Override
    public void bindTag(ArrayList<FriendDTO> users) {
        this.items = users;
    }
}
