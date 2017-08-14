package com.example.android.selfns.GroupView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.EditText;

import com.example.android.selfns.GroupView.Adapter.NotifyAdapter;
import com.example.android.selfns.GroupView.Adapter.SmsAdapter;
import com.example.android.selfns.GroupView.Data.NotifyGroupData;
import com.example.android.selfns.GroupView.Data.SmsGroupData;
import com.example.android.selfns.R;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class UnitActivity extends AppCompatActivity {

    @BindView(R.id.detail_comment)
    EditText etDetail;

   public  static final int NOTIFY_GROUP_DATA = 4;
    public  static final int SMS_GROUP_DATA =7;

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
        Long id = getIntent().getLongExtra("id", -1);
        int type = getIntent().getIntExtra("type", -1);

        if (id != -1) {
            Realm realm = Realm.getDefaultInstance();

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sms_rv);
            RecyclerViewExpandableItemManager expMgr = new RecyclerViewExpandableItemManager(null);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            switch (type) {
                case NOTIFY_GROUP_DATA: //notify
                    NotifyGroupData ngData = realm.where(NotifyGroupData.class).equalTo("id", id).findFirst();
                    NotifyAdapter notifyAdapter = new NotifyAdapter(this, realm);
                    notifyAdapter.setItems(ngData);
                    recyclerView.setAdapter(expMgr.createWrappedAdapter(notifyAdapter));
                    break;
                case SMS_GROUP_DATA: //sms
                   SmsGroupData sgData = realm.where(SmsGroupData.class).equalTo("id", id).findFirst();
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


}
