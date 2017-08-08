package com.example.android.contentproviderbroadcastreceiver.GroupView.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.NotifyUnitData;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmHelper;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.ExtraView.Comment.CommentBtnClickListener;
import com.example.android.contentproviderbroadcastreceiver.Helper.CommentUtil;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmDataHelper;
import com.example.android.contentproviderbroadcastreceiver.GroupView.UnitActivity;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-08-02.
 */

public class VHNotify extends AbstractExpandableItemViewHolder {
    @BindView(R.id.notify_person)
    TextView person;
    @BindView(R.id.notify_number)
    TextView number;
    @BindView(R.id.notify_content)
    TextView content;


    @BindView(R.id.notify_write)
    Button commentBtn;
    @BindView(R.id.notify_delete)
    Button deleteBtn;
    private Context context;
    private CommentBtnClickListener mListener;

    public VHNotify(View itemView, CommentBtnClickListener listener, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        this.context=context;
    }

    public void bindType(final MyRealmObject item) {
        final NotifyUnitData notifyUnitData = (NotifyUnitData) item;
        person.setText(notifyUnitData.getName());
        number.setText(notifyUnitData.getName());
        content.setText(notifyUnitData.getComment());

       commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                content.setText(comment.getText());
//                mListener.onClick(NotifyUnitData.class,item, comment.getText().toString());
                CommentUtil.getInstance().show((UnitActivity) context, notifyUnitData.getId(), RealmDataHelper.getInstance().NOTIFY_UNIT_DATA);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmHelper.notifyUnitDataDelete(notifyUnitData);
            }
        });

    }
}
