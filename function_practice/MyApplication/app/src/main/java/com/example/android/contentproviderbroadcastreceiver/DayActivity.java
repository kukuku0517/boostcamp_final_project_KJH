package com.example.android.contentproviderbroadcastreceiver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.contentproviderbroadcastreceiver.Adapter.DayAdapter;
import com.example.android.contentproviderbroadcastreceiver.Background.ContentProviderData;
import com.example.android.contentproviderbroadcastreceiver.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Data.RealmHelper;
import com.example.android.contentproviderbroadcastreceiver.Data.SmsData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.PhotoData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DayActivity extends AppCompatActivity {

    @BindView(R.id.rv_day)
    RecyclerView rv;

    @BindView(R.id.progressBar)
    ProgressBar pb;


    private Realm realm;
    private RecyclerView.LayoutManager layoutManager;
    private DayAdapter adapter;
    private ArrayList<MyRealmObject> items = new ArrayList<>();
    private long startMillis, endMillis;

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


        SharedPreferences mPref =PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                getSharedPreferences("setting", Activity.MODE_PRIVATE);
//        mPref.edit().putBoolean("init", false).commit();

        if (!mPref.contains("init")) {
            Log.d("pref", "0");

            new RealmAsync().execute();
        } else if (!mPref.getBoolean("init", false)) {
            Log.d("pref", "1");

            new RealmAsync().execute();
        } else {
            Log.d("pref", "2");

            displayRecyclerView();
        }

    }

    private class RealmAsync extends AsyncTask<Void, Void, Void> {
        Realm realmAsync;

        @Override
        protected Void doInBackground(Void... params) {
            realmAsync = Realm.getDefaultInstance();

            ContentProviderData cp = new ContentProviderData(getApplicationContext(), startMillis, endMillis, realmAsync);
            cp.readSMSMessage();
            cp.readCallLogs();
            cp.readImages();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            mPref.edit().putBoolean("init", true).commit();

displayRecyclerView();
        }
    }

    void displayRecyclerView(){

        pb.setVisibility(View.GONE);

        items.clear();
        getItemFromRealm(realm);
        adapter.updateItem(items);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }
    private void getItemFromRealm(Realm realmAsync) {

        realmAsync = Realm.getDefaultInstance();
        RealmResults<CallData> cData = RealmHelper.callDataLoad("date",startMillis,endMillis);
        RealmResults<SmsData> mData =RealmHelper.smsDataLoad("date",startMillis,endMillis);
        RealmResults<PhotoData> pData = RealmHelper.photoDataLoad("date",startMillis,endMillis);
        //between("date",startMillis,endMillis).

        for (CallData c : cData) {
            items.add(c);
            Log.d("call", String.valueOf(c));
        }
        for (SmsData m : mData) {
            items.add(m);

            Log.d("sms", String.valueOf(m));
        }
        for (PhotoData p : pData) {
            items.add(p);


        }
        Log.d("photo", String.valueOf(startMillis));

        Log.d("photo", String.valueOf(endMillis));

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
        Log.d("item", String.valueOf(items));

    }


}
