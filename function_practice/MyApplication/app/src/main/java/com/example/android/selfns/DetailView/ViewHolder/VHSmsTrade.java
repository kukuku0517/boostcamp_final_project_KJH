package com.example.android.selfns.DetailView.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.selfns.DailyView.ViewHolder.DayViewHolder;
import com.example.android.selfns.Data.DTO.Detail.SmsTradeDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.R;
import com.github.vipulasri.timelineview.TimelineView;

import butterknife.BindView;

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


    //공통 버튼
    @BindView(R.id.item_highlight)
    ImageButton highlightBtn;
    @BindView(R.id.item_delete)
    ImageButton deleteBtn;
    @BindView(R.id.item_edit)
    ImageButton editBtn;
    @BindView(R.id.item_share)
    ImageButton shareBtn;
    //공통 메뉴
    @BindView(R.id.item_people)
    View peopleView;
    @BindView(R.id.item_hide_menu)
    View hideMenu;

    //고유 레이아웃
    @BindView(R.id.trade_cv)
    View view;
    @BindView(R.id.trade_iv)
    ImageView iv;
    @BindView(R.id.trade_location)
    TextView location;
    @BindView(R.id.trade_place)
    TextView place;
    @BindView(R.id.trade_comment)
    TextView comment;
    private Context context;
    private boolean isExpanded = false;

    public VHSmsTrade(View view, Context context) {
        super(view);
        this.context = context;
        setmListener(context, nListener);
    }

    @Override
    public void bindType(final BaseDTO item) {
        final SmsTradeDTO stData = (SmsTradeDTO) item;
        date.setText(DateHelper.getInstance().toDateString("hh:mm",stData.getDate()));
        ampm.setText(DateHelper.getInstance().isAm(stData.getDate()));

        comment.setText(stData.getComment());
        location.setText(stData.getPlace());

        if (stData.isHighlight()) {

            highlightBtn.setColorFilter(Color.YELLOW);
        } else {

            highlightBtn.setColorFilter(Color.BLACK);
        }
        highlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stData.isHighlight()) {
                    highlightBtn.setColorFilter(Color.BLACK);
                } else {

                    highlightBtn.setColorFilter(Color.YELLOW);
                }

                ItemInteractionUtil.getInstance(context).highlight(stData);
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
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).shareItem(item);
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
