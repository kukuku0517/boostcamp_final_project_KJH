package com.example.android.selfns.DailyView.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.selfns.Data.DTO.Group.GpsGroupDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.R;
import com.github.vipulasri.timelineview.LineType;
import com.github.vipulasri.timelineview.TimelineView;

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
    ImageButton highlightBtn;
    @BindView(R.id.item_delete)
    ImageButton deleteBtn;
    @BindView(R.id.item_edit)
    ImageButton editBtn;

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

    //고유 뷰
    @BindView(R.id.gps_group_comment)
    TextView comment;
    @BindView(R.id.gps_group_departure)
    TextView departure;
    @BindView(R.id.gps_group_iv)
    ImageView iv;
    @BindView(R.id.gps_group_moving)
    TextView moving;
    @BindView(R.id.gps_group_place)
    TextView place;

    private Context context;
    private boolean isExpanded = false;

    public VHGpsGroup(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기
        this.context = context;
        setmListener(context, nListener);
        tlv.initLine(LineType.NORMAL);
    }


    @Override
    public void bindType(final BaseDTO item) {
        final GpsGroupDTO callData = (GpsGroupDTO) item;

        date.setText(DateHelper.getInstance().toDateString("hh:mm", callData.getDate()));
        ampm.setText(DateHelper.getInstance().isAm(callData.getDate()));
        comment.setText(callData.getComment());
        place.setText(callData.getPlace());
        if (callData.isHighlight()) {

            highlightBtn.setColorFilter(Color.YELLOW);
        } else {

            highlightBtn.setColorFilter(Color.BLACK);
        }
        if (callData.isStart()) {
            departure.setText("출발");
            if (callData.getEndId() == -1) {
                moving.setText("이동중 ...");
            } else {
                moving.setText("");
            }
        } else {
            departure.setText("도착");
            moving.setText("");
        }


        highlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callData.isHighlight()) {
                    highlightBtn.setColorFilter(Color.BLACK);
                } else {

                    highlightBtn.setColorFilter(Color.YELLOW);
                }
                ItemInteractionUtil.getInstance(context).highlight(callData);
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
                mListener.onGpsGroupItemClick(item);
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).shareItem(callData);
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
}
