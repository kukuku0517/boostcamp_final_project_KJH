package com.example.android.selfns.DetailView;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.selfns.DailyView.Adapter.DayAdapter;
import com.example.android.selfns.Data.DTO.Detail.CustomDTO;
import com.example.android.selfns.Data.DTO.Group.GpsGroupDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.GroupData.GpsGroupData;
import com.example.android.selfns.Data.RealmData.UnitData.CustomData;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mancj.slideup.SlideUp;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class GpsGroupActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_gps_group)
    RecyclerView rv;


    @BindView(R.id.rv_tag)
    RecyclerView rvTag;
    @BindView(R.id.gps_group_menu)
    View view;
    @BindView(R.id.fab_gps_group)
    FloatingActionButton fab;
    @BindView(R.id.gps_group_delete)
    TextView delete;
    @BindView(R.id.gps_group_comment)
    TextView comment;


    private ArrayList<BaseDTO> items = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private DayAdapter adapter;
    Context context = this;
    GpsGroupDTO gpsGroupDTO;
    SlideUp slideUp;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_group);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
getSupportActionBar().setTitle("이동 경로");
        realm = Realm.getDefaultInstance();
//        Class c = RealmClassHelper.getInstance().getClass(getIntent().getIntExtra("type", -1));
//        long id = getIntent().getLongExtra("id", -1);


        initVIew();
        initSlideup();
        initBtnListener();

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {

                items.clear();
                initVIew();
            }
        });
    }

    private void initVIew() {

        gpsGroupDTO = Parcels.unwrap(getIntent().getParcelableExtra("item"));

        if (gpsGroupDTO.getStart() == -1) {
            GpsGroupData customData = realm.where(GpsGroupData.class).equalTo("id", gpsGroupDTO.getStartId()).findFirst();
            if (customData != null) {
                gpsGroupDTO = new GpsGroupDTO(customData);
            }
        } else {
            GpsGroupData customData = realm.where(GpsGroupData.class).equalTo("id", gpsGroupDTO.getId()).findFirst();
            if (customData != null) {
                gpsGroupDTO = new GpsGroupDTO(customData);
            }
        }
        comment.setText(gpsGroupDTO.getComment());

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new DayAdapter(this, realm);
        items.addAll(gpsGroupDTO.getGpsDatas());
        adapter.updateItem(items);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

    }


    private void initSlideup() {
        slideUp = new SlideUp.Builder(view)
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.BOTTOM)
                .build();
        slideUp.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getVisibility() == View.VISIBLE) {
                    slideUp.hide();
                } else {
                    slideUp.show();
                }
            }
        });
    }

    private void initBtnListener() {

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).show((AppCompatActivity) context, gpsGroupDTO.getId(), RealmClassHelper.GPS_GROUP_DATA);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).deleteGpsGroupItem(gpsGroupDTO);
            }
        });
    }


}
