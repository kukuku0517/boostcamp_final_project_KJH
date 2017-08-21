package com.example.android.selfns.GroupView.ViewHolder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.selfns.Data.DTO.Group.GlideApp;
import com.example.android.selfns.Data.DTO.Group.NotifyUnitDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.GroupData.NotifyUnitData;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.RealmHelper;
import com.example.android.selfns.ExtraView.Comment.CommentBtnClickListener;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-08-02.
 */

public class VHNotify extends  MyExpandableItemViewHolder{
    @BindView(R.id.notify_person)
    TextView person;

    @BindView(R.id.notify_content)
    TextView content;

    @BindView(R.id.notify_count)
    TextView count;

    @BindView(R.id.notify_write)
    ImageButton commentBtn;

    @BindView(R.id.notify_delete)
    ImageButton deleteBtn;
    @BindView(R.id.notify_iv)
    ImageView iv;

    private Context context;
    private CommentBtnClickListener mListener;

    public VHNotify(View itemView, CommentBtnClickListener listener, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        this.context = context;
    }

    public void bindType(final BaseDTO item) {
        final NotifyUnitDTO notifyUnitData = (NotifyUnitDTO) item;
        GlideApp.with(context).load(R.drawable.icon_account).into(iv);
        person.setText(notifyUnitData.getName());
        content.setText(notifyUnitData.getComment());
        count.setText(String.valueOf(notifyUnitData.getCount()));
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                content.setText(comment.getText());
//                mListener.onClick(NotifyUnitData.class,item, comment.getText().toString());
                ItemInteractionUtil.getInstance(context).show((AppCompatActivity) context, notifyUnitData.getId(), RealmClassHelper.getInstance().NOTIFY_UNIT_DATA);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmHelper.getInstance().notifyUnitDataDelete(notifyUnitData);
            }
        });

    }
}
