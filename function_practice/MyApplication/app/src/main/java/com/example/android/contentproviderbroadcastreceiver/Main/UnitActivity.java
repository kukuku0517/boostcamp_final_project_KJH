package com.example.android.contentproviderbroadcastreceiver.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.EditText;

import com.example.android.contentproviderbroadcastreceiver.Adapter.Expandable.NotifyAdapter;
import com.example.android.contentproviderbroadcastreceiver.Adapter.Expandable.SmsAdapter;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Interface.PhotoItemClickListener;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class UnitActivity extends AppCompatActivity {

    @BindView(R.id.detail_comment)
    EditText etDetail;

    public void setEditTextVisibility(int visibility){
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
                if(!hasFocus){
                    v.setVisibility(View.GONE);
                }
            }
        });
        Long id = getIntent().getLongExtra("id", -1);
        int type = getIntent().getIntExtra("class", -1);

        if (id != -1) {
            Realm realm = Realm.getDefaultInstance();

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sms_rv);
            RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            switch (type) {
                case 0: //notify
                    RealmResults<NotifyGroupData> ngData = realm.where(NotifyGroupData.class).equalTo("id", id).findAll();
                    NotifyAdapter notifyAdapter = new NotifyAdapter(this, realm);
                    notifyAdapter.setItems(ngData.get(0));
                    recyclerView.setAdapter(expMgr.createWrappedAdapter(notifyAdapter));
                    break;
                case 1: //sms

                    RealmResults<SmsGroupData> sgData = realm.where(SmsGroupData.class).equalTo("id", id).findAll();
                    SmsAdapter smsAdapter = new SmsAdapter(this, realm);
                    smsAdapter.setItems(sgData.get(0));
                    recyclerView.setAdapter(expMgr.createWrappedAdapter(smsAdapter));
                    break;

            }

            // NOTE: need to disable change animations to ripple effect work properly
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

            expMgr.attachRecyclerView(recyclerView);
        }
    }


}
