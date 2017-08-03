package com.example.android.contentproviderbroadcastreceiver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.android.contentproviderbroadcastreceiver.Adapter.DayAdapter;
import com.example.android.contentproviderbroadcastreceiver.Adapter.Expandable.NotifyAdapter;
import com.example.android.contentproviderbroadcastreceiver.Adapter.Expandable.SmsAdapter;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.PhotoGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.PhotoData;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class PhotoActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private DayAdapter adapter;
    private ArrayList<MyRealmObject> items = new ArrayList<>();
   @BindView(R.id.photo_rv)
   RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        Long id = getIntent().getLongExtra("id", -1);

        if (id != -1) {
            Realm realm = Realm.getDefaultInstance();

            layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            adapter = new DayAdapter(this,realm);
            PhotoGroupData pgData = realm.where(PhotoGroupData.class).equalTo("id",id).findFirst();

            items.clear();
            for(PhotoData p:pgData.getPhotoss()){
                items.add(p);
            }
            adapter.updateItem(items);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(adapter);
        }
    }
}
