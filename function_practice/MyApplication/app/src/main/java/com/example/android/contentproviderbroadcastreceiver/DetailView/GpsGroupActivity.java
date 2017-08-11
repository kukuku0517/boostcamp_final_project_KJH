package com.example.android.contentproviderbroadcastreceiver.DetailView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.android.contentproviderbroadcastreceiver.DailyView.Adapter.DayAdapter;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.PhotoData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.GpsGroupData;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmDataHelper;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

public class GpsGroupActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_gps_group)
    RecyclerView rv;

    private RealmList<MyRealmObject> items = new RealmList<>();
    private RecyclerView.LayoutManager layoutManager;
    private DayAdapter adapter;
    GpsGroupData gpsGroupData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_group);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Realm realm = Realm.getDefaultInstance();
        Class c = RealmDataHelper.getInstance().getClass(getIntent().getIntExtra("type", -1));
        long id = getIntent().getLongExtra("id", -1);
        gpsGroupData = (GpsGroupData) realm.where(c).equalTo("id", id).findFirst();
        realm = Realm.getDefaultInstance();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new DayAdapter(this, realm);
        getItemFromRealm();
        adapter.updateItem(items);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }

    private void getItemFromRealm() {
        for (GpsData gps : gpsGroupData.getGpsDatas()) {
            items.add(gps);
        }
    }
}
