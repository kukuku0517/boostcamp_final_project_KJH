package com.example.android.contentproviderbroadcastreceiver.Adapter.Expandable;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
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
    Button button;

    public VHNotify(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindType(MyRealmObject item) {
        NotifyUnitData notifyUnitData = (NotifyUnitData) item;
        person.setText(notifyUnitData.getName());
        number.setText(notifyUnitData.getName());
        content.setText(notifyUnitData.getContent());
    }
}
