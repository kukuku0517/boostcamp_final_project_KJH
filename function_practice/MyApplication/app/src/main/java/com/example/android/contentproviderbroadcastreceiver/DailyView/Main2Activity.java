package com.example.android.contentproviderbroadcastreceiver.DailyView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Background.ContentProviderData;
import com.example.android.contentproviderbroadcastreceiver.Background.DataUpdateService;
import com.example.android.contentproviderbroadcastreceiver.Background.GoogleAwareness.GoogleAwarenessService;
import com.example.android.contentproviderbroadcastreceiver.DailyView.Adapter.CalendarPinAdapter;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.PhotoGroupData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.CallActivity;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.DatePinData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.SmsTradeData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.GpsStillActivity;
import com.example.android.contentproviderbroadcastreceiver.DetailView.SmsTradeActivity;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.PhotoActivity;
import com.example.android.contentproviderbroadcastreceiver.GroupView.UnitActivity;
import com.example.android.contentproviderbroadcastreceiver.Helper.DateHelper;
import com.example.android.contentproviderbroadcastreceiver.Helper.PinnedHeaderItemDecoration;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmDataHelper;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmHelper;
import com.example.android.contentproviderbroadcastreceiver.Interface.CardItemClickListener;
import com.example.android.contentproviderbroadcastreceiver.Interface.DatePinClickListener;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class Main2Activity extends AppCompatActivity implements CardItemClickListener, DatePinClickListener {

    @BindView(R.id.rv_day2)
    RecyclerView rv;
    @BindView(R.id.tv_day2)
    TextView tv;
    @BindView(R.id.cv_day2)
    CompactCalendarView cv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Realm realm;
    private Realm realmAsync;
    private RecyclerView.LayoutManager layoutManager;
    private CalendarPinAdapter adapter;
    private RealmList<MyRealmObject> items = new RealmList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendar:
                if (cv.getVisibility() == View.VISIBLE) {
                    cv.setVisibility(View.GONE);
                } else {
                    cv.setVisibility(View.VISIBLE);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SelfNS");


        initService();

        initRealm();
        setCalendar();

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        getSharedPreferences("setting", Activity.MODE_PRIVATE);
        mPref.edit().putBoolean("init", true).commit();

        if (!mPref.contains("init")) {
            Log.d("pref", "0");
//            new RealmAsync().execute(0);
//            new RealmAsync().execute(1);
//            new RealmAsync().execute(2);
        } else if (!mPref.getBoolean("init", false)) {
            Log.d("pref", "1");
            new RealmAsync().execute(0);
            new RealmAsync().execute(1);
//            new RealmAsync().execute(2);
        } else {
            Log.d("pref", "2");
            displayRecyclerView();
        }


    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);


        realm = Realm.getDefaultInstance();
    }

    private void initService() {
        Intent i = new Intent(this, DataUpdateService.class);
        startService(i);
        Intent notifyIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(notifyIntent);
        Intent locationIntent = new Intent(this, GoogleAwarenessService.class);
        startService(locationIntent);
    }

    private void setCalendar() {
        final String TAG = "calendarView";
        final CompactCalendarView compactCalendar = (CompactCalendarView) findViewById(R.id.cv_day2);
        compactCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        Event ev1 = new Event(Color.GREEN, 1433701251000L, "Some extra data that I want to store.");
        compactCalendar.addEvent(ev1);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.GREEN, 1433704251000L);
        compactCalendar.addEvent(ev2);

        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        List<Event> events = compactCalendar.getEvents(1433701251000L); // can also take a Date object

        // events has size 2 with the 2 events inserted previously
        Log.d(TAG, "Events: " + events);

        // define a listener to receive callbacks when certain events happen.
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Intent intent = new Intent(Main2Activity.this, DayActivity.class);
                intent.putExtra("date", dateClicked.getTime());
                startActivity(intent);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });
    }

    private class RealmAsync extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            realmAsync = Realm.getDefaultInstance();
            long today[] = DateHelper.getInstance().getStartEndDate(System.currentTimeMillis());
            ContentProviderData cp = new ContentProviderData(getApplicationContext(), today[0], today[1], realmAsync);
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
            SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            mPref.edit().putBoolean("init", true).commit();
            updateItemsFromRealm(System.currentTimeMillis());
            displayRecyclerView();
        }

    }


    void updateItemsFromRealm(long millis) {
        long minusDay1 = DateHelper.getInstance().getDayAfter(millis, -1);
        long minusDay2 = DateHelper.getInstance().getDayAfter(millis, -2);
        long minusDay3 = DateHelper.getInstance().getDayAfter(millis, -3);
        items.addAll(getItemFromRealm(minusDay3));
        items.addAll(getItemFromRealm(minusDay2));
        items.addAll(getItemFromRealm(minusDay1));
        items.addAll(getItemFromRealm(millis));
        Collections.sort(items, new CompareItem());

    }

    private RealmList<MyRealmObject> getItemFromRealm(long millis) {

        long today[] = DateHelper.getInstance().getStartEndDate(millis);
        long startMillis = today[0];
        long endMillis = today[1];

        RealmList<MyRealmObject> items = new RealmList<>();

        RealmResults<RealmObject> cData = RealmHelper.getInstance().DataHighlightLoad(CallData.class, "date", startMillis, endMillis);
        RealmResults<RealmObject> gData = RealmHelper.getInstance().DataHighlightLoad(GpsData.class, "date", startMillis, endMillis);
        RealmResults<RealmObject> smsTradeDatas = RealmHelper.getInstance().DataHighlightLoad(SmsTradeData.class, "date", startMillis, endMillis);
        RealmResults<RealmObject> pgData = RealmHelper.getInstance().DataHighlightLoad(PhotoGroupData.class, "start", startMillis, endMillis - 1);
        RealmResults<RealmObject> ngDatas = RealmHelper.getInstance().DataHighlightLoad(NotifyGroupData.class, "start", startMillis, endMillis - 1);
        RealmResults<RealmObject> smsGroupDatas = RealmHelper.getInstance().DataHighlightLoad(SmsGroupData.class, "start", startMillis, endMillis - 1);

        DatePinData datePinData = new DatePinData();
        datePinData.setDate(startMillis);
        items.add(datePinData);

        for (RealmObject g : gData) {
            items.add((GpsData) g);
        }

        for (RealmObject c : cData) {
            items.add((CallData) c);
            Log.d("call", String.valueOf(c));
        }

        for (RealmObject pg : pgData) {
            items.add((PhotoGroupData) pg);
        }
        for (RealmObject std : smsTradeDatas) {
            items.add((SmsTradeData) std);
        }


        for (RealmObject m : smsGroupDatas) {
            items.add((SmsGroupData) m);

            Log.d("sms", String.valueOf(m));
        }

        for (RealmObject nn : ngDatas) {
            items.add((NotifyGroupData) nn);
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

        return items;
    }

    void displayRecyclerView() {

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CalendarPinAdapter(this, realm);
        adapter.updateItem(items);

        rv.addItemDecoration(new PinnedHeaderItemDecoration());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rv.setOnScrollChangeListener(new View.OnScrollChangeListener() {//TODO infinite scroll
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });
        } else {
            rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                items.clear();
                updateItemsFromRealm(System.currentTimeMillis());
                adapter.updateItem(items);
                adapter.notifyDataSetChanged();
            }
        });
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
    public void onDatePinClick(MyRealmObject item) {
        Intent intent = new Intent(this, DayActivity.class);
        intent.putExtra("date", item.getDate());
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
        intent.putExtra("id", item.getId());
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

    private class CompareItem implements Comparator<MyRealmObject> {
        @Override
        public int compare(MyRealmObject o1, MyRealmObject o2) {
            return 0;
        }
    }
}
