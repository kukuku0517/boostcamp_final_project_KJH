package com.example.android.selfns.GroupView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.EditText;

import com.example.android.selfns.Data.DTO.Group.NotifyGroupDTO;
import com.example.android.selfns.Data.DTO.Group.SmsGroupDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.GroupView.Adapter.NotifyAdapter;
import com.example.android.selfns.GroupView.Adapter.SmsAdapter;
import com.example.android.selfns.R;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class UnitActivity extends AppCompatActivity {

    @BindView(R.id.detail_comment)
    EditText etDetail;

    public static final int NOTIFY_GROUP_DATA = 4;
    public static final int SMS_GROUP_DATA = 7;

    public void setEditTextVisibility(int visibility) {
        etDetail.setVisibility(visibility);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        ButterKnife.bind(this);

        etDetail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    v.setVisibility(View.GONE);
                }
            }
        });

        BaseDTO data = Parcels.unwrap(getIntent().getParcelableExtra("item"));
        int type = data.getType();
        Realm realm = Realm.getDefaultInstance();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sms_rv);
        RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        switch (type) {
            case NOTIFY_GROUP_DATA: //notify
                NotifyGroupDTO ngData = (NotifyGroupDTO) data;
                NotifyAdapter notifyAdapter = new NotifyAdapter(this, realm);
                notifyAdapter.setItems(ngData);
                recyclerView.setAdapter(expMgr.createWrappedAdapter(notifyAdapter));
                break;
            case SMS_GROUP_DATA: //sms
                SmsGroupDTO sgData = (SmsGroupDTO) data;
                SmsAdapter smsAdapter = new SmsAdapter(this, realm);
                smsAdapter.setItems(sgData);
                recyclerView.setAdapter(expMgr.createWrappedAdapter(smsAdapter));
                break;

        }

        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        expMgr.attachRecyclerView(recyclerView);

    }


}
