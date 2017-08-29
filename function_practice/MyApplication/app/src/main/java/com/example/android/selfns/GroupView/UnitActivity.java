package com.example.android.selfns.GroupView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.android.selfns.Data.DTO.Group.NotifyGroupDTO;
import com.example.android.selfns.Data.DTO.Group.NotifyUnitDTO;
import com.example.android.selfns.Data.DTO.Group.SmsGroupDTO;
import com.example.android.selfns.Data.DTO.Group.SmsUnitDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.GroupData.NotifyGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.SmsGroupData;
import com.example.android.selfns.GroupView.Adapter.NotifyAdapter;
import com.example.android.selfns.GroupView.Adapter.SmsAdapter;
import com.example.android.selfns.R;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;

import static android.R.attr.data;

public class UnitActivity extends AppCompatActivity {

    @BindView(R.id.detail_comment)
    EditText etDetail;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sms_rv)
    RecyclerView recyclerView;
    public static final int NOTIFY_GROUP_DATA = 4;
    public static final int SMS_GROUP_DATA = 7;
    Realm realm;
    BaseDTO data;
    RecyclerViewExpandableItemManager expMgr;
    int type;
    NotifyAdapter notifyAdapter;
    SmsAdapter smsAdapter;

    public void setEditTextVisibility(int visibility) {
        etDetail.setVisibility(visibility);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();
        etDetail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    v.setVisibility(View.GONE);
                }
            }
        });

        notifyAdapter = new NotifyAdapter(this, realm);
        smsAdapter = new SmsAdapter(this, realm);

        data = Parcels.unwrap(getIntent().getParcelableExtra("item"));
        type = data.getType();

        expMgr = new RecyclerViewExpandableItemManager(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        updateData(type);
        initRecyclerview(type);

        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        expMgr.attachRecyclerView(recyclerView);
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                updateData(type);
            }
        });

    }

    void updateData(int type) {
        switch (type) {
            case NOTIFY_GROUP_DATA: //notify
                NotifyGroupDTO ngData = (NotifyGroupDTO) data;
                NotifyGroupData notifyGroupData = realm.where(NotifyGroupData.class).equalTo("id", ngData.getId()).findFirst();
                if (notifyGroupData != null) {
                    ngData = new NotifyGroupDTO(notifyGroupData);
                }

                ArrayList<NotifyUnitDTO> items = ngData.getUnits();
                Collections.sort(items, new Comparator<NotifyUnitDTO>() {
                    @Override
                    public int compare(NotifyUnitDTO o1, NotifyUnitDTO o2) {
                        return o2.getCount() - o1.getCount();
                    }
                });
                ngData.setUnits(items);
                notifyAdapter.setItems(ngData);

                notifyAdapter.notifyDataSetChanged();
                break;
            case SMS_GROUP_DATA:
                SmsGroupDTO sgData = (SmsGroupDTO) data;
                SmsGroupData smsGroupData = realm.where(SmsGroupData.class).equalTo("id", sgData.getId()).findFirst();
                if (smsGroupData != null) {
                    sgData = new SmsGroupDTO(smsGroupData);
                }

                ArrayList<SmsUnitDTO> items2 = (ArrayList<SmsUnitDTO>) sgData.getUnits();
                Collections.sort(items2, new Comparator<SmsUnitDTO>() {
                    @Override
                    public int compare(SmsUnitDTO o1, SmsUnitDTO o2) {
                        return o2.getCount() - o1.getCount();
                    }
                });
                sgData.setUnits(items2);
                smsAdapter.setItems(sgData);
                smsAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void initRecyclerview(int type) {
        updateData(type);
        switch (type) {
            case NOTIFY_GROUP_DATA: //notify
                recyclerView.setAdapter(expMgr.createWrappedAdapter(notifyAdapter));
                break;
            case SMS_GROUP_DATA: //sms
                recyclerView.setAdapter(expMgr.createWrappedAdapter(smsAdapter));
                break;

        }
    }
}
