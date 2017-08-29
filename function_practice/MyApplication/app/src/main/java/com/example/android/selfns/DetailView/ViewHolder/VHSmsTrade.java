package com.example.android.selfns.DetailView.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.selfns.DailyView.ViewHolder.DayViewHolder;
import com.example.android.selfns.Data.DTO.Detail.SmsTradeDTO;
import com.example.android.selfns.Data.DTO.Group.GlideApp;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.ExtraView.Friend.TagAdapter;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.R;
import com.github.vipulasri.timelineview.LineType;
import com.github.vipulasri.timelineview.TimelineView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by samsung on 2017-08-04.
 */

public class VHSmsTrade extends DayViewHolder {

    //공통
    @BindView(R.id.item_am_pm)
    TextView ampm;
    @BindView(R.id.item_date)
    TextView date;
    @BindView(R.id.item_time_marker)
    TimelineView tlv;
    @BindView(R.id.rv_tag)
    RecyclerView rvTag;
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

    //고유 레이아웃
    @BindView(R.id.trade_cv)
    View view;
    @BindView(R.id.trade_iv)
    RoundedImageView iv;
    @BindView(R.id.trade_location)
    TextView location;
    @BindView(R.id.trade_place)
    TextView place;
    @BindView(R.id.trade_comment)
    TextView comment;
    private Context context;
    private boolean isExpanded = false;

    RecyclerView.LayoutManager layoutManager;
    TagAdapter adapter;
    List<FriendDTO> items = new ArrayList<>();
    public VHSmsTrade(View view, Context context) {
        super(view);
        this.context = context;
        setmListener(context, nListener);
        tlv.initLine(LineType.NORMAL);
        tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle, null));
        icon.setImageResource(R.drawable.ic_attach_money_black_24dp);
        GlideApp.with(context).load(R.drawable.finance).into(iv);
    }

    @Override
    public void bindType(final BaseDTO item) {
        final SmsTradeDTO stData = (SmsTradeDTO) item;
        date.setText(DateHelper.getInstance().toDateString("hh:mm", stData.getDate()));
        ampm.setText(DateHelper.getInstance().isAm(stData.getDate()));

        comment.setText(stData.getComment());
        location.setText(stData.getPlace());
        if (stData.getShare() == 1) {
            highlightBtn.setBtnFillColor(Color.RED);
            highlightBtn.setBtnColor(Color.RED);
            highlightBtn.setChecked(true);
        } else if (stData.getHighlight() == 1) {
            highlightBtn.setBtnFillColor(Color.rgb(249, 168, 37));
            highlightBtn.setBtnColor(Color.rgb(189, 189, 189));
            highlightBtn.setChecked(true);
        } else {
            highlightBtn.setBtnFillColor(Color.rgb(249, 168, 37));
            highlightBtn.setBtnColor(Color.rgb(189, 189, 189));
            highlightBtn.setChecked(false);
        }

        highlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stData.getShare() != 1) {
                    ItemInteractionUtil.getInstance(context).highlight(stData);
                }
            }
        });
        tagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stData.getShare() == 1) {
                    ItemInteractionUtil.getInstance(context).tagFriend((AppCompatActivity) context, item);

                }
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stData.getShare() == 0) {
                    ItemInteractionUtil.getInstance(context).shareItem(stData);
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).deleteItem(stData);
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSmsTradeItemClick(item);
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

    }

}
