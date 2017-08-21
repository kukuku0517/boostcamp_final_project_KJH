package com.example.android.selfns.GroupView.ViewHolder;

import android.view.View;

import com.example.android.selfns.DailyView.ViewHolder.DayViewHolder;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.LoginView.UserDTO;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.expandable.annotation.ExpandableItemStateFlags;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-19.
 */

public abstract class MyExpandableItemViewHolder extends DayViewHolder implements ExpandableItemViewHolder {
    @Override
    public void bindType(BaseDTO item) {
    }

    @Override
    public void bindTag(ArrayList<UserDTO> users) {

    }
    @ExpandableItemStateFlags
    private int mExpandStateFlags;

    public MyExpandableItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setExpandStateFlags(@ExpandableItemStateFlags int flags) {
        mExpandStateFlags = flags;
    }

    @Override
    @ExpandableItemStateFlags
    public int getExpandStateFlags() {
        return mExpandStateFlags;
    }
}
