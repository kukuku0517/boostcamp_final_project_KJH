package com.example.android.contentproviderbroadcastreceiver;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.contentproviderbroadcastreceiver.Adapter.DayAdapter;
import com.example.android.contentproviderbroadcastreceiver.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Data.SmsData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.PhotoData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
private long startMillis,endMillis;
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
        end.add(Calendar.DATE,1);
      startMillis = start.getTimeInMillis();
 endMillis = end.getTimeInMillis();


        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm r = Realm.getDefaultInstance();
//        r.close();
//
//        Realm.deleteRealm(Realm.getDefaultConfiguration());

//        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();


        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new DayAdapter(this,realm);

        pb.setVisibility(View.VISIBLE);
        new RealmAsync().execute();

    }
    private class RealmAsync extends AsyncTask<Void,Void,Void>{
        Realm realmAsync;
        @Override
        protected Void doInBackground(Void... params) {
            realmAsync = Realm.getDefaultInstance();
            readSMSMessage(startMillis, endMillis,realmAsync);
            readCallLogs(startMillis, endMillis,realmAsync);
            readImages(startMillis, endMillis,realmAsync);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            pb.setVisibility(View.GONE);
            getItemFromRealm(realm);
            adapter.updateItem(items);

            rv.setHasFixedSize(true);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(adapter);
        }
    }
    private void getItemFromRealm(Realm realmAsync) {

        realmAsync= Realm.getDefaultInstance();
        RealmResults<CallData> cData = realmAsync.where(CallData.class).between("date",startMillis,endMillis).findAll();
        RealmResults<SmsData> mData = realmAsync.where(SmsData.class).between("date",startMillis,endMillis).findAll();
        RealmResults<PhotoData> pData =realmAsync.where(PhotoData.class).between("date",startMillis,endMillis).findAll();

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
        return (int) (o1.getDate()-o2.getDate());
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
});
        Log.d("item", String.valueOf(items));

    }



    public void readSMSMessage(long start, long end, Realm realm) {
        SmsData smsData = new SmsData();
        Log.d("start", String.valueOf(start));
        Log.d("start", String.valueOf(end));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = getContentResolver();
        String[] projection = {"_id", "thread_id", "address", "person", "date", "body"};
        String selection = "date > " + start + " and date < " + end;
        String sortOrder = "date DESC";
        Cursor c = cr.query(allMessage, projection, selection, null, sortOrder);

        while (c.moveToNext()) {
            long messageId = c.getLong(0);
            long threadId = c.getLong(1);
            String address = c.getString(2);
            long contactId = c.getLong(3);
            String contactId_string = String.valueOf(contactId);
            long timestamp = c.getLong(4);
            String body = c.getString(5);
            Log.d("add", address);

            realm.beginTransaction();
            smsData.setDate(timestamp);
            smsData.setContent(body);
            smsData.setPerson(contactId_string);
            realm.copyToRealm(smsData);
            realm.commitTransaction();


        }

    }

    void readCallLogs(long start, long end, Realm realm) {

        CallData callData = new CallData();

        String[] projection = {CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION};
        String selection = CallLog.Calls.DATE + " < " + end + " and " + CallLog.Calls.DATE + ">" + start;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, selection,
                null, CallLog.Calls.DEFAULT_SORT_ORDER);

        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String phone = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
            String duration = c.getString(c.getColumnIndex(CallLog.Calls.DURATION));
            long datetimeMillis = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));

            realm.beginTransaction();
            callData.setPerson(name);
            callData.setDate(datetimeMillis);
            callData.setDuration(duration);
            callData.setNumber(phone);

            realm.copyToRealm(callData);
            realm.commitTransaction();


        }

    }

    void readImages(long start, long end, Realm realm) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        PhotoData photo = new PhotoData();
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.DATE_TAKEN};

        String selection = MediaStore.Images.Media.DATE_ADDED + ">" + start/1000 + " and " + MediaStore.Images.Media.DATE_ADDED + "<" + end/1000;
        Cursor imageCursor = getApplicationContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                selection,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함


        while (imageCursor.moveToNext()) {
            String filePath = imageCursor.getString(imageCursor.getColumnIndex(projection[0]));
            Uri imageUri = Uri.parse(filePath);
            long date = imageCursor.getLong(imageCursor.getColumnIndex(projection[1]))*1000;

            String lat = imageCursor.getString(imageCursor.getColumnIndex(projection[2]));
            String lng = imageCursor.getString(imageCursor.getColumnIndex(projection[3]));
            Log.d("photo", String.valueOf(date));

            realm.beginTransaction();
            photo.setDate(date);
            photo.setLat(lat);
            photo.setLng(lng);
            photo.setPath(filePath);

            realm.copyToRealm(photo);
            realm.commitTransaction();


        }


    }

}
