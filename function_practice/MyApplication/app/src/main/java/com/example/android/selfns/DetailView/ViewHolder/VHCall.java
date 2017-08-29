package com.example.android.selfns.DetailView.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.selfns.DailyView.ViewHolder.DayViewHolder;
import com.example.android.selfns.Data.DTO.Detail.CallDTO;
import com.example.android.selfns.Data.DTO.Group.GlideApp;
import com.example.android.selfns.Data.DTO.Retrofit.FriendAddDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.ExtraView.Friend.TagAdapter;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.Helper.RetrofitHelper;
import com.example.android.selfns.Interface.DataReceiveListener;
import com.example.android.selfns.R;
import com.github.vipulasri.timelineview.LineType;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-07-26.
 */

public class VHCall extends DayViewHolder {

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
    @BindView(R.id.rv_tag)
    RecyclerView rvTag;

    //고유 레이아웃
    @BindView(R.id.call_cv)
    View view;

    //고유 뷰
//    @BindView(R.id.call_iv)
//    ImageView iv;
    @BindView(R.id.call_person)
    TextView person;
    @BindView(R.id.call_duration)
    TextView duration;
    @BindView(R.id.call_number)
    TextView number;
    @BindView(R.id.call_comment)
    TextView comment;

    private Context context;
    private boolean isExpanded = false;

    //for shareables
    RecyclerView.LayoutManager layoutManager;
    TagAdapter adapter;
    DataReceiveListener<ArrayList<FriendDTO>> listener;
    List<FriendDTO> items = new ArrayList<>();

    public VHCall(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기
        this.context = context;
        setmListener(context, nListener);
        tlv.initLine(LineType.NORMAL);
        tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle, null));
        icon.setImageResource(R.drawable.ic_call_black_24dp);
//        listener = new DataReceiveListener<ArrayList<FriendDTO>>() {
//            @Override
//            public void onReceive(ArrayList<FriendDTO> response) {
//                items.clear();
//                for (FriendDTO friend : response) {
//                    UserDTO user = new UserDTO();
//                    user.setId(friend.getId());
//                    user.setName(friend.getName());
//                    user.setPhotoUrl(friend.getPhotoUrl());
//                    items.add(user);
//                }
//
//                adapter.updateItem(items);
//                adapter.notifyDataSetChanged();
//            }
//        };
    }

    @Override
    public void bindType(final BaseDTO item) {
        final CallDTO callData = (CallDTO) item;

        date.setText(DateHelper.getInstance().toDateString("hh:mm", callData.getDate()));
        ampm.setText(DateHelper.getInstance().isAm(callData.getDate()));

        person.setText(callData.getPerson());
        comment.setText(callData.getComment());
        duration.setText(DateHelper.getInstance().toDurationString(callData.getDuration()));
        switch (callData.getCallState()) {
            case 1: //incoming
                duration.append(" 수신");
                break;
            case 2: //outgoing
                duration.append(" 발신");
                break;
            default:
                break;
        }

        highlightBtn.init((Activity) context);
        number.setText(callData.getNumber());

        if (callData.getShare() == 1) {
            highlightBtn.setBtnFillColor(Color.RED);
            highlightBtn.setChecked(true);

        } else if (callData.getHighlight() == 1) {

            highlightBtn.setChecked(true);
        } else {
            highlightBtn.setChecked(false);

        }


        highlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                mListener.onCallItemClick(item);
            }
        });

        tagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).tagFriend((AppCompatActivity) context, item);
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
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).shareItem(callData);
            }
        });
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        adapter = new TagAdapter(context);
        adapter.updateItem(items);
        rvTag.setHasFixedSize(true);
        rvTag.setLayoutManager(layoutManager);
        rvTag.setAdapter(adapter);
//
//        if (callData.getShare() == 1) {
//
//            RetrofitHelper.getInstance(context).getTaggedFriends(((CallDTO) item).get_id(), listener);
//
//        }

//        try {
//            JSONArray friends = new JSONArray(callData.getFriends());
//            for (int i = 0; i < friends.length(); i++) {
//                JSONObject friend = friends.getJSONObject(i);
//                String uid = friend.get("id").toString();
//                DatabaseReference fRef = FirebaseHelper.getInstance(context).getUserRef(uid);
//                fRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        UserDTO friend = dataSnapshot.child("userDTO").getValue(UserDTO.class);
//                        items.add(friend);
//                        adapter.updateItem(items);
//                        adapter.notifyDataSetChanged();
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }

    @Override
    public void bindTag(ArrayList<FriendDTO> users) {
        this.items = users;
    }
}
