package com.example.android.selfns.DetailView.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.selfns.DailyView.ViewHolder.DayViewHolder;
import com.example.android.selfns.Data.DTO.Detail.GpsDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.R;
import com.github.vipulasri.timelineview.LineType;
import com.github.vipulasri.timelineview.TimelineView;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by samsung on 2017-08-02.
 */

public class VHGps extends DayViewHolder {
    //공통
    @BindView(R.id.item_am_pm)
    TextView ampm;
    @BindView(R.id.item_date)
    TextView date;
    @BindView(R.id.item_time_marker)
    TimelineView tlv;
    @BindView(R.id.item_time_icon)
    ImageView icon;
    @BindView(R.id.gps_place)
    TextView place;

    @BindView(R.id.gps_delete)
    ImageView deleteBtn;

    @BindView(R.id.gps_comment)
    TextView comment;
    @BindView(R.id.gps_cv)
    View view;

    private Context context;

    public VHGps(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기
        this.context = context;
        tlv.initLine(LineType.NORMAL);
        tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle, null));
        icon.setImageResource(R.drawable.ic_location_on_black_24dp);
    }

    @Override
    public void bindType(final BaseDTO item) {
        final GpsDTO gpsData = (GpsDTO) item;
        DateFormat sdFormat = new SimpleDateFormat("HH : mm");
        Date d = new Date(gpsData.getDate());
        String tempDate = sdFormat.format(d);

        date.setText(DateHelper.getInstance().toDateString("hh:mm", gpsData.getDate()));
        ampm.setText(DateHelper.getInstance().isAm(gpsData.getDate()));
        place.setText(gpsData.getPlace());

        switch (gpsData.getMoveState()) {
            case 1:
                comment.setText("정지");
                tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_accessibility_black_24dp, null));
                break;
            case 2:
                comment.setText("이동");
                tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_directions_walk_black_24dp, null));
                break;
            case 3:
                comment.setText("이동");
                tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_directions_walk_black_24dp, null));
                break;
            case 4:
                comment.setText("정지");

                tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_accessibility_black_24dp, null));
                break;
            case 5:
                comment.setText("정지");
                tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_accessibility_black_24dp, null));
                break;
            case 6:
                comment.setText("이동");
                tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_directions_car_black_24dp, null));
                break;
        }


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).deleteItem(gpsData);
            }
        });



    }

    @Override
    public void bindTag(ArrayList<FriendDTO> users) {

    }
}
