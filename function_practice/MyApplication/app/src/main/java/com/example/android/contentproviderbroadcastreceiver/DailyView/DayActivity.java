package com.example.android.contentproviderbroadcastreceiver.DailyView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.contentproviderbroadcastreceiver.DailyView.Adapter.DayAdapter;
import com.example.android.contentproviderbroadcastreceiver.Background.ContentProviderData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.CallActivity;
import com.example.android.contentproviderbroadcastreceiver.DetailView.GpsStillActivity;
import com.example.android.contentproviderbroadcastreceiver.DetailView.SmsTradeActivity;
import com.example.android.contentproviderbroadcastreceiver.GroupView.PhotoActivity;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.DailyView.Data.PhotoGroupData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmHelper;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.SmsTradeData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.UnitActivity;
import com.example.android.contentproviderbroadcastreceiver.Interface.CardItemClickListener;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmDataHelper;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static android.media.CamcorderProfile.get;

public class DayActivity extends AppCompatActivity implements CardItemClickListener {

    @BindView(R.id.rv_day)
    RecyclerView rv;
    @BindView(R.id.fab_day)
    FloatingActionButton fab;
    @BindView(R.id.progressBar)
    ProgressBar pb;

    private Realm realm;
    private RecyclerView.LayoutManager layoutManager;
    private DayAdapter adapter;
    private RealmList<MyRealmObject> items = new RealmList<>();
    private long startMillis, endMillis, quarter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int year = intent.getIntExtra(getString(R.string.year), 2017);
        int month = intent.getIntExtra(getString(R.string.month), 1);
        int day = intent.getIntExtra(getString(R.string.dayofmonth), 1);

        Calendar start = new GregorianCalendar(year, month, day);
        Calendar end = new GregorianCalendar(year, month, day);
        end.add(Calendar.DATE, 1);
        startMillis = start.getTimeInMillis();
        endMillis = end.getTimeInMillis();
        quarter = (endMillis - startMillis) / 4;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
//        Realm.deleteRealm(Realm.getDefaultConfiguration());

        realm = Realm.getDefaultInstance();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new DayAdapter(this, realm);
        pb.setVisibility(View.VISIBLE);

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        getSharedPreferences("setting", Activity.MODE_PRIVATE);
        mPref.edit().putBoolean("init", true).commit();

        if (!mPref.contains("init")) {
            Log.d("pref", "0");
//            new RealmAsync().execute(0);
//            new RealmAsync().execute(1);
            new RealmAsync().execute(2);
        } else if (!mPref.getBoolean("init", false)) {
            Log.d("pref", "1");
//            new RealmAsync().execute(0);
//            new RealmAsync().execute(1);
            new RealmAsync().execute(2);
        } else {
            Log.d("pref", "2");
            displayRecyclerView();
        }
    }


    @Override
    public void onNotifyItemClick(MyRealmObject item) {
        Intent intent = new Intent(this, UnitActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmDataHelper.getInstance().NOTIFY_GROUP_DATA);
        startActivity(intent);
    }

    @Override
    public void onSmsGroupItemClick(MyRealmObject item) {
        Intent intent = new Intent(this, UnitActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmDataHelper.getInstance().SMS_GROUP_DATA);
        startActivity(intent);
    }


    @Override
    public void onSmsTradeItemClick(MyRealmObject item) {
        Intent intent = new Intent(this, SmsTradeActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmDataHelper.getInstance().SMS_TRADE_DATA);
        startActivity(intent);
    }


    @Override
    public void onPhotoGroupItemClick(MyRealmObject item) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmDataHelper.getInstance().PHOTO_GROUP_DATA);
        startActivity(intent);
    }

    @Override
    public void onGpsItemClick(GpsData item) {
        Intent intent = new Intent(this, GpsStillActivity.class);
        intent.putExtra("id",item.getId());
        intent.putExtra("type", RealmDataHelper.getInstance().GPS_DATA);
        startActivity(intent);
    }

    @Override
    public void onCallItemClick(MyRealmObject item) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmDataHelper.getInstance().CALL_DATA);
        startActivity(intent);
    }


    private class RealmAsync extends AsyncTask<Integer, Void, Void> {
        Realm realmAsync;

        @Override
        protected Void doInBackground(Integer... params) {
            realmAsync = Realm.getDefaultInstance();
            ContentProviderData cp = new ContentProviderData(getApplicationContext(), startMillis, endMillis, realmAsync);
            switch (params[0]) {
                case 0:
                    cp.readSMSMessage();
                    break;
                case 1:
                    cp.readCallLogs();
                    break;
                case 2:
                    cp.readImages();
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            mPref.edit().putBoolean("init", true).commit();
            displayRecyclerView();
        }
    }

    void displayRecyclerView() {
        pb.setVisibility(View.GONE);
        getItemFromRealm();
        adapter.updateItem(items);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                Log.d("realmupdate","asfd");

                getItemFromRealm();
                adapter.updateItem(items);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getItemFromRealm() {
        items.clear();
        RealmResults<CallData> cData = RealmHelper.getInstance().callDataLoad("date", startMillis, endMillis);
//        RealmResults<PhotoData> pData = RealmHelper.photoDataLoad("date", startMillis, endMillis);

        RealmResults<RealmObject> pgData = RealmHelper.getInstance().DataLoad(PhotoGroupData.class, "start", startMillis, endMillis);

        RealmResults<GpsData> gData = RealmHelper.getInstance().gpsDataLoad("date", startMillis, endMillis);
        RealmResults<NotifyGroupData> ngDatas = RealmHelper.getInstance().notifyGroupDataLoad("end", startMillis, endMillis - 1);
        RealmResults<SmsGroupData> smsGroupDatas = RealmHelper.getInstance().smsGroupDataLoad("date", startMillis, endMillis - 1);
        RealmResults<RealmObject> smsTradeDatas = RealmHelper.getInstance().DataLoad(SmsTradeData.class, "date", startMillis, endMillis);
        for (GpsData g : gData) {
            items.add(g);
        }
        for (CallData c : cData) {
            items.add(c);
            Log.d("call", String.valueOf(c));
        }
//        for (PhotoData p : pData) {
//            items.add(p);
//        }
        for (RealmObject pg : pgData) {
            items.add((PhotoGroupData) pg);
        }
        for (RealmObject std : smsTradeDatas) {
            items.add((SmsTradeData) std);
        }


        for (SmsGroupData m : smsGroupDatas) {
            items.add(m);

            Log.d("sms", String.valueOf(m));
        }
        for (NotifyGroupData nn : ngDatas) {
            items.add(nn);
        }


        Collections.sort(items, new Comparator<MyRealmObject>() {
            @Override
            public int compare(MyRealmObject o1, MyRealmObject o2) {
                return (int) (o1.getDate() - o2.getDate());
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
    }
}
