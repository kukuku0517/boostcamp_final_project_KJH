package com.example.android.contentproviderbroadcastreceiver;

import android.app.Activity;
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
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.NotifyData;
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
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static android.R.attr.start;
import static android.media.CamcorderProfile.get;

public class DayActivity extends AppCompatActivity {

    @BindView(R.id.rv_day)
    RecyclerView rv;

    @BindView(R.id.progressBar)
    ProgressBar pb;

    private Realm realm;
    private RecyclerView.LayoutManager layoutManager;
    private DayAdapter adapter;
    private ArrayList<MyRealmObject> items = new ArrayList<>();
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
//        mPref.edit().putBoolean("init", true).commit();

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
//            SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            mPref.edit().putBoolean("init", true).commit();

            displayRecyclerView();
        }
    }

    void displayRecyclerView() {

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

        RealmResults<CallData> cData = RealmHelper.callDataLoad("date", startMillis, endMillis);
        RealmResults<SmsData> mData = RealmHelper.smsDataLoad("date", startMillis, endMillis);
        RealmResults<PhotoData> pData = RealmHelper.photoDataLoad("date", startMillis, endMillis);

        RealmResults<NotifyData> nData = RealmHelper.notifyDataLoad("date", startMillis, endMillis);
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


        //get today group datas
        RealmResults<NotifyGroupData> ngDatas = RealmHelper.notifyGroupDataLoad("date", startMillis, endMillis);
//
//        //if first, make new
//        if (ngDatas.size() == 0) {
//            realmAsync.beginTransaction();
//            NotifyGroupData ngData1 = new NotifyGroupData();
//            NotifyGroupData ngData2 = new NotifyGroupData();
//            NotifyGroupData ngData3 = new NotifyGroupData();
//            NotifyGroupData ngData4 = new NotifyGroupData();
//
//            ngData1.setTime(startMillis, startMillis + quarter);
//            ngData2.setTime(startMillis + quarter, startMillis + quarter * 2);
//            ngData3.setTime(startMillis + quarter * 2, startMillis + quarter * 3);
//            ngData4.setTime(startMillis + quarter * 3, startMillis + quarter * 4);
//
//            realmAsync.copyToRealm(ngData1);
//            realmAsync.copyToRealm(ngData2);
//            realmAsync.copyToRealm(ngData3);
//            realmAsync.copyToRealm(ngData4);
//            realmAsync.commitTransaction();
//            ngDatas = RealmHelper.notifyGroupDataLoad("date", startMillis, endMillis);
//
//        }
//
//
//        for (NotifyData n : nData) {
//            //get a group by time
//            realmAsync.beginTransaction();
//            int index;
//            if (n.getDate() < startMillis + quarter) {
//                index = 0;
//            } else if (n.getDate() < startMillis + quarter * 2) {
//                index = 1;
//            } else if (n.getDate() < startMillis + quarter * 3) {
//                index = 2;
//            } else {
//                index = 3;
//            }
//            NotifyGroupData ngData = ngDatas.get(index);
//
//            //get a unit by name, if exist add n to the unit else create one and put it in group
//            int i = ngData.checkName(n.getPerson());
//Log.d("grouping int i ", String.valueOf(i));
//            if (i != -1) {
//                NotifyUnitData nuData =  ngData.getUnits().get(i);
//                nuData.setCount(nuData.getCount()+1);
//                nuData.setStart(n.getDate());
//                nuData.setEnd(n.getDate());
//                nuData.getNotifys().add(n);
//
//            } else {
//                NotifyUnitData nuData=new NotifyUnitData();
//                nuData.setCount(1);
//                nuData.setStart(n.getDate());
//                nuData.setEnd(n.getDate());
//                nuData.setName(n.getPerson());
//                RealmList<NotifyData> notiList= new RealmList<>();
//                notiList.add(n);
//                nuData.setNotifys(notiList);
//
//                ngData.getUnits().add(nuData);
//
//            }
//
//            realmAsync.commitTransaction();
//            ngDatas = RealmHelper.notifyGroupDataLoad("date", startMillis, endMillis);
//
//
//
//        }

        int j=0;
        for(NotifyGroupData nn:ngDatas){
            items.add(nn);
            Log.d("grouping", String.valueOf(j++));
        }
        Log.d("photo", String.valueOf(startMillis));

        Log.d("photo", String.valueOf(endMillis));

        Collections.sort(items, new Comparator<MyRealmObject>()

        {
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
