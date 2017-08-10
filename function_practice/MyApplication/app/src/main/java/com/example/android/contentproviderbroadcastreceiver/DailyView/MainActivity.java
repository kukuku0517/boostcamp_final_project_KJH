package com.example.android.contentproviderbroadcastreceiver.DailyView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.android.contentproviderbroadcastreceiver.Background.DataUpdateService;
import com.example.android.contentproviderbroadcastreceiver.Background.GoogleAwareness.GoogleAwarenessService;
import com.example.android.contentproviderbroadcastreceiver.DailyView.Adapter.CalendarPinnedAdapter;
import com.example.android.contentproviderbroadcastreceiver.DailyView.Adapter.DayAdapter;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.PhotoGroupData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.CallActivity;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.SmsTradeData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.GpsStillActivity;
import com.example.android.contentproviderbroadcastreceiver.DetailView.SmsTradeActivity;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.PhotoActivity;
import com.example.android.contentproviderbroadcastreceiver.GroupView.UnitActivity;
import com.example.android.contentproviderbroadcastreceiver.Helper.DateHelper;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmDataHelper;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmHelper;
import com.example.android.contentproviderbroadcastreceiver.Interface.CardItemClickListener;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.gvillani.pinnedlist.Group;
import com.gvillani.pinnedlist.GroupListWrapper;
import com.gvillani.pinnedlist.ItemPinned;
import com.gvillani.pinnedlist.PinnedListLayout;
import com.gvillani.pinnedlist.TextItemPinned;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity implements CardItemClickListener {
    @BindView(R.id.calendarView)
    View cv;

    //    @BindView(R.id.main_rv)
    RecyclerView rv;
    @BindView(R.id.main_pl)
    PinnedListLayout pl;
    RecyclerView.LayoutManager layoutManager;
    DayAdapter adapter;
    List<MyRealmObject> items = new ArrayList<>();

    private RecyclerView.Adapter mListAdapter;
    Realm realm;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        startServices();

        initRealm();



        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new DayAdapter(this, realm);
//        getItemFromRealm(System.currentTimeMillis());
//        GroupListWrapper listGroup = GroupListWrapper.createAlphabeticList(items, GroupListWrapper.ASCENDING);
        GroupListWrapper listGroup = createItems(System.currentTimeMillis());
        rv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });
        rv = pl.getRecyclerView();
        mListAdapter = new CalendarPinnedAdapter(this, listGroup, pl);
//        rv.setHasFixedSize(true);
//        rv.setLayoutManager(layoutManager);
        rv.setAdapter(mListAdapter);

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
//                getItemFromRealm(System.currentTimeMillis());

                mListAdapter.notifyDataSetChanged();
            }
        });

        setCalendar();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
    }

    private void startServices() {
        Intent i = new Intent(this, DataUpdateService.class);
        startService(i);
        Intent notifyIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(notifyIntent);
        Intent locationIntent = new Intent(this, GoogleAwarenessService.class);
        startService(locationIntent);
    }

    private List<ItemPinned> getItemFromRealm(long millis) {

        long today[] = DateHelper.getInstance().getStartEndDate(millis);
        long startMillis = today[0];
        long endMillis = today[1];

        items.clear();
        RealmResults<RealmObject> cData = RealmHelper.getInstance().DataHighlightLoad(CallData.class, "date", startMillis, endMillis);
        RealmResults<RealmObject> gData = RealmHelper.getInstance().DataHighlightLoad(GpsData.class, "date", startMillis, endMillis);
        RealmResults<RealmObject> smsTradeDatas = RealmHelper.getInstance().DataHighlightLoad(SmsTradeData.class, "date", startMillis, endMillis);
        RealmResults<RealmObject> pgData = RealmHelper.getInstance().DataHighlightLoad(PhotoGroupData.class, "start", startMillis, endMillis);
        RealmResults<RealmObject> ngDatas = RealmHelper.getInstance().DataHighlightLoad(NotifyGroupData.class, "start", startMillis, endMillis - 1);
        RealmResults<RealmObject> smsGroupDatas = RealmHelper.getInstance().DataHighlightLoad(SmsGroupData.class, "start", startMillis, endMillis - 1);

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
        List<ItemPinned> list = new ArrayList<>();
        ;
        for (GroupListWrapper.Selector item : items) {

            list.add(new TextItemPinned(item));
        }
        return list;


    }

    private GroupListWrapper createItems(long millis) {

        long yesterday = DateHelper.getInstance().getDayAfter(millis, -1);
        long tommorrow = DateHelper.getInstance().getDayAfter(millis, 1);


        GroupListWrapper listWrapper = new GroupListWrapper();
        Group group1 = new Group(getItemFromRealm(yesterday), DateHelper.getInstance().toDateString("MM dd", yesterday));
        String s = DateHelper.getInstance().toDateString("MM dd", yesterday);
        s = DateHelper.getInstance().toDateString("MM dd", tommorrow);
        listWrapper.addGroup(group1);
        Group group2 = new Group(getItemFromRealm(millis), DateHelper.getInstance().toDateString("MM dd", millis));
//        Group group2 = new Group(getItemFromRealm(millis), DateHelper.getInstance().toDateString("MM dd", millis));
        listWrapper.addGroup(group2);
        Group group3 = new Group(getItemFromRealm(tommorrow), DateHelper.getInstance().toDateString("MM dd", tommorrow));
        listWrapper.addGroup(group3);


//        GroupListWrapper groups = new GroupListWrapper();
//
//        SortedSet<String> keys = new TreeSet<>(new CompareItem());
//
//
//        keys.addAll(hashMap.keySet());
//
//        for (String key : keys) {
//            List<ItemPinned> values = hashMap.get(key);
//            groups.addGroup(new Group(values, key));
//        }
//
//        return groups;

        return listWrapper;
    }

    private void setCalendar() {
        final String TAG = "calendarView";
        final CompactCalendarView compactCalendar = (CompactCalendarView) findViewById(R.id.calendarView);
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
                Calendar c = Calendar.getInstance();
                c.setTime(dateClicked);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

                Intent intent = new Intent(MainActivity.this, DayActivity.class);
                intent.putExtra(getString(R.string.year), year);
                intent.putExtra(getString(R.string.month), month);
                intent.putExtra(getString(R.string.dayofmonth), dayOfMonth);
                startActivity(intent);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
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
