package com.example.android.contentproviderbroadcastreceiver.Adapter.Expandable;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Interface.CommentBtnClickListener;
import com.example.android.contentproviderbroadcastreceiver.Interface.CommentUtil;
import com.example.android.contentproviderbroadcastreceiver.Main.UnitActivity;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

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
    Button button;
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                content.setText(comment.getText());
//                mListener.onClick(NotifyUnitData.class,item, comment.getText().toString());
                CommentUtil.getInstance().show((UnitActivity) context, notifyUnitData);
            }
        });

    }
}
