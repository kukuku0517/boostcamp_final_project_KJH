package com.example.android.selfns.GroupView.Adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.android.selfns.Data.DTO.Detail.SmsDTO;
import com.example.android.selfns.Data.DTO.Group.SmsGroupDTO;
import com.example.android.selfns.Data.DTO.Group.SmsUnitDTO;
import com.example.android.selfns.GroupView.ViewHolder.VHSms;
import com.example.android.selfns.GroupView.ViewHolder.VHSmsChild;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmCommentableObject;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmObject;
import com.example.android.selfns.ExtraView.Comment.CommentBtnClickListener;
import com.example.android.selfns.GroupView.UnitActivity;
import com.example.android.selfns.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import io.realm.Realm;

/**
 * Created by samsung on 2017-08-02.
 */

public class SmsAdapter extends AbstractExpandableItemAdapter<VHSms, VHSmsChild> implements CommentBtnClickListener {

    private SmsGroupDTO items;
    private Context context;
    private Realm realm;

    public SmsAdapter(Context context, Realm realm) {
        setHasStableIds(true);
        this.context = context;
        this.realm = realm;
    }

    public void setItems(SmsGroupDTO items) {
        this.items = items;
    }

    @Override
    public int getGroupCount() {
        return items.getUnits().size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return items.getUnits().get(groupPosition).getSmss().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return items.getUnits().get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return items.getUnits().get(groupPosition).getSmss().get(childPosition).getId();
    }

    @Override
    public VHSms onCreateGroupViewHolder(ViewGroup parent, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms, parent, false);
        return new VHSms(v, this, context);
    }

    @Override
    public VHSmsChild onCreateChildViewHolder(ViewGroup parent, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms_child, parent, false);
        return new VHSmsChild(v);
    }

    @Override
    public void onBindGroupViewHolder(VHSms holder, int groupPosition, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        SmsUnitDTO unitdata = items.getUnits().get(groupPosition);
        holder.bindType(unitdata);    Animation animation = AnimationUtils.loadAnimation(context,R.anim.fade_in_item);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return items.getUnits().get(groupPosition).getSmss().get(childPosition).getType();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return items.getUnits().get(groupPosition).getType();
    }

    @Override
    public void onBindChildViewHolder(VHSmsChild holder, int groupPosition, int childPosition, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        SmsDTO child = items.getUnits().get(groupPosition).getSmss().get(childPosition);
        holder.bindType(child);
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(VHSms holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }

    @Override
    public void onClick(final Class c, final MyRealmObject item, final String text) {
        Log.d("comments", "onClick");
        UnitActivity detail =  (UnitActivity)context;
        detail.setEditTextVisibility(View.VISIBLE);


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MyRealmCommentableObject result = (MyRealmCommentableObject) realm.where(c).equalTo("id", item.getId()).findFirst();
                if (result != null) {
                    Log.d("comments", "result");

                    result.setComment(text);

                }
            }
        });

    }
}
