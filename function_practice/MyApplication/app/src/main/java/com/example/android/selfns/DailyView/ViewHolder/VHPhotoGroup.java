package com.example.android.selfns.DailyView.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.selfns.Data.DTO.Group.PhotoGroupDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.ExtraView.Friend.TagAdapter;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.LoginView.UserDTO;
import com.example.android.selfns.R;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by samsung on 2017-08-03.
 */

public class VHPhotoGroup extends DayViewHolder {

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

    @BindView(R.id.photo_group_cv)
    View view;
    //고유 뷰
    @BindView(R.id.photo_group_iv)
    ImageView iv;
    @BindView(R.id.photo_group_number)
    TextView number;
    @BindView(R.id.photo_group_place)
    TextView location;
    @BindView(R.id.photo_group_comment)
    TextView comment;

    boolean isExpanded=false;

    RecyclerView.LayoutManager layoutManager;
    TagAdapter adapter;

    private Context context;
    public VHPhotoGroup(View view, Context context) {
        super(view);
        this.context=context;
        setmListener(context, nListener);
    }

    @Override
    public void bindType(final BaseDTO item) {
        final PhotoGroupDTO callData = (PhotoGroupDTO) item;

        date.setText(DateHelper.getInstance().toDateString("hh:mm",callData.getDate()));
        ampm.setText(DateHelper.getInstance().isAm(callData.getDate()));

        location.setText(callData.getPlace());
        int photoNum = callData.getPhotoss().size();
        number.setText(String.valueOf(photoNum) + "장");
        comment.setText(callData.getComment());
        if (callData.isHighlight()) {

            highlightBtn.setColorFilter(Color.YELLOW);
        } else {

            highlightBtn.setColorFilter(Color.BLACK);
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
        Glide.with((Context) mListener).load(callData.getPhotoss().get(0).getPath()).into(iv);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).deletePhotoGroupItem(callData);
            }
        });


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPhotoGroupItemClick(callData);
            }
        });
        tagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).tagFriend((AppCompatActivity) context, item);
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).shareItem(callData);
            }
        });
        final List<UserDTO> items = new ArrayList<>();
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        adapter = new TagAdapter(context);
        adapter.updateItem(items);
        rvTag.setHasFixedSize(true);
        rvTag.setLayoutManager(layoutManager);
        rvTag.setAdapter(adapter);

        try {
            JSONArray friends = new JSONArray(callData.getFriends());
            for (int i = 0; i < friends.length(); i++) {
                JSONObject friend = friends.getJSONObject(i);
                String uid=friend.get("id").toString();
                DatabaseReference fRef =  FirebaseHelper.getInstance(context).getUserRef(uid);
                fRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        UserDTO friend = dataSnapshot.child("userDTO").getValue(UserDTO.class);
                        items.add(friend);
                        adapter.updateItem(items);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
