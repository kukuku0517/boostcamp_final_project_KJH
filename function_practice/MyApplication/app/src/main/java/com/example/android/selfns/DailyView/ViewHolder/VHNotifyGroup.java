package com.example.android.selfns.DailyView.ViewHolder;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.selfns.Data.DTO.Detail.CallDTO;
import com.example.android.selfns.Data.DTO.Detail.NotifyDTO;
import com.example.android.selfns.Data.DTO.Group.NotifyGroupDTO;
import com.example.android.selfns.Data.DTO.Group.NotifyUnitDTO;
import com.example.android.selfns.Data.DTO.Group.SmsUnitDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.ExtraView.Friend.TagAdapter;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.Helper.RetrofitHelper;
import com.example.android.selfns.Interface.DataReceiveListener;
import com.example.android.selfns.R;
import com.github.vipulasri.timelineview.LineType;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by samsung on 2017-07-29.
 */

public class VHNotifyGroup extends DayViewHolder {

    //공통
    @BindView(R.id.item_am_pm)
    TextView ampm;
    @BindView(R.id.item_date)
    TextView date;
    @BindView(R.id.item_am_pm_to)
    TextView ampmTo;
    @BindView(R.id.item_date_to)
    TextView dateTo;
    @BindView(R.id.item_time_marker)
    TimelineView tlv;
    @BindView(R.id.item_time_icon)
    ImageView icon;
    @BindView(R.id.notify_group_title)
    TextView title;
    //공통 메뉴


//    @BindView(R.id.rv_tag)
//    RecyclerView rvTag;

    //고유 레이아웃
    @BindView(R.id.notify_group_cv)
    View view;
    @BindView(R.id.notify_group_pcount)
    TextView pcount;
    @BindView(R.id.notify_group_mcount)
    TextView mcount;

    RecyclerView.LayoutManager layoutManager;
    TagAdapter adapter;
    DataReceiveListener<ArrayList<FriendDTO>> listener;
    List<FriendDTO> items = new ArrayList<>();
    private Context context;

    public VHNotifyGroup(View view, Context context) {
        super(view);
        this.context = context;
        setmListener(context, nListener);
        tlv.initLine(LineType.NORMAL);
        tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle, null));
        icon.setImageResource(R.drawable.ic_message_black_24dp);
//
//        listener = new DataReceiveListener<ArrayList<FriendDTO>>() {
//            @Override
//            public void onReceive(ArrayList<FriendDTO> response) {
//
//                for (FriendDTO friend : response) {
//                    UserDTO user = new UserDTO();
//                    user.setId(friend.getId());
//                    user.setName(friend.getName());
//                    user.setPhotoUrl(friend.getPhotoUrl());
//                    items.add(friend);
//                }
//
//                adapter.updateItem(items);
//                adapter.notifyDataSetChanged();
//            }
//        };
    }

    @Override
    public void bindType(final BaseDTO item) {
        NotifyGroupDTO notifyData = (NotifyGroupDTO) item;
        date.setText(DateHelper.getInstance().toDateString("hh:mm", notifyData.getStart()));
        ampm.setText(DateHelper.getInstance().isAm(notifyData.getStart()));

        dateTo.setText(DateHelper.getInstance().toDateString("hh:mm", notifyData.getEnd()));
        ampmTo.setText(DateHelper.getInstance().isAm(notifyData.getEnd()));


        switch (DateHelper.getInstance().getRangeOfDay(notifyData.getStart())) {
            case 0:
                title.setText("새벽 메세지함");
                break;
            case 1:
                title.setText("오전 메세지함");
                break;
            case 2:
                title.setText("오후 메세지함");
                break;
            case 3:
                title.setText("저녁 메세지함");
                break;
        }


        int size = notifyData.getUnits().size();
        if (size != 0) {

            pcount.setVisibility(View.VISIBLE);
            int count = notifyData.getUnits().size() - 1;
            String name = notifyData.getUnits().get(0).getName();
            pcount.setText(name + " 외 " + count + "명");
        } else {
            pcount.setVisibility(View.GONE);
        }

        int count = 0;
        for (NotifyUnitDTO sud : notifyData.getUnits()) {
            count += sud.getNotifys().size();
        }
        mcount.setText(String.format("총 %d개의 메세지", count));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNotifyItemClick(item);
            }
        });
//
//        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//        adapter = new TagAdapter(context);
//        adapter.updateItem(items);
//        rvTag.setHasFixedSize(true);
//        rvTag.setLayoutManager(layoutManager);
//        rvTag.setAdapter(adapter);

//        RetrofitHelper.getInstance(context).getTaggedFriends((item).get_id(), listener);
    }

    @Override
    public void bindTag(ArrayList<FriendDTO> users) {

    }
}
