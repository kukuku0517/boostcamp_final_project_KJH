package com.example.android.selfns.DetailView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.android.selfns.DailyView.Adapter.DayAdapter;
import com.example.android.selfns.Data.DTO.Group.GpsGroupDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.R;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class GpsGroupActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_gps_group)
    RecyclerView rv;

    private ArrayList<BaseDTO> items = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private DayAdapter adapter;
    GpsGroupDTO gpsGroupData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_group);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Realm realm = Realm.getDefaultInstance();
//        Class c = RealmClassHelper.getInstance().getClass(getIntent().getIntExtra("type", -1));
//        long id = getIntent().getLongExtra("id", -1);

        gpsGroupData = Parcels.unwrap(getIntent().getParcelableExtra("item"));

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new DayAdapter(this, realm);
        items.addAll(gpsGroupData.getGpsDatas());
        adapter.updateItem(items);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }


}
