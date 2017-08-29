package com.example.android.selfns.DailyView.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.selfns.Data.DTO.Detail.CallDTO;
import com.example.android.selfns.Data.DTO.Group.GlideApp;
import com.example.android.selfns.Data.DTO.Group.PhotoGroupDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.ExtraView.Friend.TagAdapter;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.DateHelper;
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

import static java.lang.System.load;

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
    ShineButton highlightBtn;
    @BindView(R.id.item_delete)
    ImageButton deleteBtn;
    @BindView(R.id.item_edit)
    ImageButton editBtn;
    @BindView(R.id.item_share)
    ImageButton shareBtn;
    @BindView(R.id.item_tag)
    ImageButton tagBtn;
    @BindView(R.id.item_time_icon)
    ImageView icon;
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

    boolean isExpanded = false;

    RecyclerView.LayoutManager layoutManager;
    TagAdapter adapter;
    List<FriendDTO> items = new ArrayList<>();
    private Context context;

    public VHPhotoGroup(View view, Context context) {
        super(view);
        this.context = context;
        setmListener(context, nListener);
        tlv.initLine(LineType.NORMAL);
        tlv.setMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle, null));
        icon.setImageResource(R.drawable.ic_image_black_24dp);

    }

    @Override
    public void bindType(final BaseDTO item) {
        final PhotoGroupDTO callData = (PhotoGroupDTO) item;
        if (callData.getPhotoss().size() > 0) {
            if (callData.getShare() == 1) {
                String path = String.format("https://selfns-taejoonhong.c9users.io:8080/images/photos-%d.jpg",
                        callData.getPhotoss().get(0).get_id());
                GlideApp.with(context).load(path).centerCrop().into(iv);
            } else {
                GlideApp.with(context).load(callData.getPhotoss().get(0).getPath()).centerCrop().into(iv);

            }
        }

        date.setText(DateHelper.getInstance().toDateString("hh:mm", callData.getDate()));
        ampm.setText(DateHelper.getInstance().isAm(callData.getDate()));
        location.setText(callData.getPlace());
        int photoNum = callData.getPhotoss().size();
        number.setText(String.valueOf(photoNum) + "장");

        //commentable
        comment.setText(callData.getComment());

        highlightBtn.init((Activity) context);

        //shareable
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

        highlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callData.getShare() != 1) {
                    ItemInteractionUtil.getInstance(context).highlight(callData);
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
