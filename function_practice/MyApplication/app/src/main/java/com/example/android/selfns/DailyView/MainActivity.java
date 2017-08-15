package com.example.android.selfns.DailyView;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.selfns.Background.ContentProviderData;
import com.example.android.selfns.Background.DataUpdateService;
import com.example.android.selfns.Background.GoogleAwareness.GoogleAwarenessService;
import com.example.android.selfns.Background.SNSNotificationService;
import com.example.android.selfns.DailyView.Adapter.CalendarPinAdapter;
import com.example.android.selfns.Data.DTO.Detail.CallDTO;
import com.example.android.selfns.Data.DTO.Detail.CustomDTO;
import com.example.android.selfns.Data.DTO.Detail.DatePinDTO;
import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.DTO.Detail.SmsTradeDTO;
import com.example.android.selfns.Data.DTO.Group.GpsGroupDTO;
import com.example.android.selfns.Data.DTO.Group.PhotoGroupDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.GroupData.GpsGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.PhotoGroupData;
import com.example.android.selfns.Data.RealmData.UnitData.CustomData;
import com.example.android.selfns.DetailView.GpsGroupActivity;
import com.example.android.selfns.DetailView.CallActivity;
import com.example.android.selfns.Data.RealmData.UnitData.CallData;
import com.example.android.selfns.Data.RealmData.UnitData.SmsTradeData;
import com.example.android.selfns.DetailView.GpsStillActivity;
import com.example.android.selfns.DetailView.SmsTradeActivity;
import com.example.android.selfns.GroupView.PhotoActivity;
import com.example.android.selfns.GroupView.UnitActivity;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.Helper.ItemComparator;
import com.example.android.selfns.Helper.PinnedHeaderItemDecoration;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Helper.RealmHelper;
import com.example.android.selfns.Interface.CardItemClickListener;
import com.example.android.selfns.Interface.DatePinClickListener;
import com.example.android.selfns.LoginView.FriendActivity;
import com.example.android.selfns.LoginView.LoginActivity;
import com.example.android.selfns.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.slideup.SlideUp;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements CardItemClickListener, DatePinClickListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.rv_day2)
    RecyclerView rv;
    @BindView(R.id.cv_day2)
    MaterialCalendarView cv;
    @BindView(R.id.cv_cardview)
    CardView cardView;
    @BindView(R.id.main_user_profile_pic)
    ImageView profile;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar pb;
//    @BindView(R.id.drawer_layout)
//    DrawerLayout drawer;
//    @BindView(R.id.nav_view)
//    NavigationView navView;

    private Realm realm;
    private Realm realmAsync;
    private RecyclerView.LayoutManager layoutManager;
    private CalendarPinAdapter adapter;
    private ArrayList<BaseDTO> items = new ArrayList<>();
    private SlideUp slideUp;
    private Context context = this;
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG};

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;

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
                if (cardView.getVisibility() == View.VISIBLE) {
                    slideUp.hide();
                } else {
                    slideUp.show();
                }
                break;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkStateAndLoad();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PERMISSION_ALL) {
            Log.d("checkstate", String.valueOf(resultCode));
            if (resultCode == RESULT_OK) {
                checkStateAndLoad();
            } else {
                checkPermissions(this, PERMISSIONS); //TODO
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("SelfNS");
        slideUp = new SlideUp.Builder(cardView)
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.TOP)
                .build();
        slideUp.hide();
        initService();
        initRealm();
        initNavBar();


        setCalendar();
        initUserState();
        checkPermissionBeforeLoadingData();


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


    }

    private void initNavBar() {
        //activity, drawerLayout,Toolbar,string ,string
        //drawer와 toolbar를 연결시켜주는 객체.

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main_layout);

        //activity, drawerLayout,Toolbar,string ,string
        //drawer와 toolbar를 연결시켜주는 객체.


        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);
    }

    private void initUserState() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getPhotoUrl()!=null) {
            Glide.with(this).load(user.getPhotoUrl()).into(profile);
        } else {
            // No user is signed in
        }

    }

    /**
     * 권한 승인 되어있으면 데이터 로드 한다
     * 아니면 권한 요청한다 ( request code = 1 )
     **/

    private void checkPermissionBeforeLoadingData() {
        if (!checkPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            checkStateAndLoad();
        }
    }

    /**
     * permission check
     * M 미만이면 권한은 승인된것으로 본다 ( check 할필요 x)
     * TODO M 이상일 경우 권한체크를 하고, 취소한 경우 알림 띄워줌
     **/
    public static boolean checkPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                        Toast.makeText(context, "앱 실행을 위해서는 권한을 설정해야 합니다",
                                Toast.LENGTH_LONG).show();
                        return false;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 0 : none
     * 1 : sms
     * 2 : sms + call
     * 3 : sms + call + photo => display호출 가능
     **/
    private void checkStateAndLoad() {
        int state = 0;
        SharedPreferences mPref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        if (!mPref.contains("init")) {
            state = 0;
        } else {
            state = mPref.getInt("init", 0);
        }

        Log.d("checkstate", String.valueOf(state));
        switch (state) {
            case 0://TODO 초기화 중지시 처리
            case 1:
            case 2:
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.deleteAll();
                        new RealmAsync().execute();
                    }
                });
                break;

            case 3:
                items.clear();
                updateItemsFromRealm(System.currentTimeMillis());
                displayRecyclerView();
                break;
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
        if (!isNLServiceRunning()) {
            Intent notifyIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(notifyIntent);
        }
        Intent locationIntent = new Intent(this, GoogleAwarenessService.class);
        startService(locationIntent);
    }

    private boolean isNLServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SNSNotificationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    private void setCalendar() {
        cv.setDateSelected(DateHelper.getInstance().toDate(System.currentTimeMillis()), true);
        cv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent intent = new Intent(MainActivity.this, DayActivity.class);
                intent.putExtra("date", date.getDate().getTime());
                startActivity(intent);
            }
        });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_friend_search:

                FirebaseHelper.getInstance(context).setFriends(currentUser.getUid());
                Toast.makeText(context, "0", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_friend_list:
                Intent intent = new Intent(MainActivity.this, FriendActivity.class);
                Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.nav_edit:
                Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_signout:

                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);

                LoginManager.getInstance().logOut();
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private class RealmAsync extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Integer... params) {

            realmAsync = Realm.getDefaultInstance();
            long today[] = DateHelper.getInstance().getStartEndDate(System.currentTimeMillis());
            ContentProviderData cp = new ContentProviderData(context, today[0], today[1], realmAsync);

            cp.readSMSMessage();
            cp.readCallLogs();
            cp.readImages();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pb.setVisibility(View.GONE);
            SharedPreferences mPref = getSharedPreferences("setting", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putInt("init", 3);
            editor.commit();

            items.clear();
            updateItemsFromRealm(System.currentTimeMillis());
            displayRecyclerView();

        }
    }

    private void updateItemsFromRealm(long millis) {

        long minusDay1 = DateHelper.getInstance().getDayAfter(millis, -1);
        long minusDay2 = DateHelper.getInstance().getDayAfter(millis, -2);
        long minusDay3 = DateHelper.getInstance().getDayAfter(millis, -3);
        items.addAll(getItemFromRealm(minusDay3));
        items.addAll(getItemFromRealm(minusDay2));
        items.addAll(getItemFromRealm(minusDay1));
        items.addAll(getItemFromRealm(millis));
        Collections.sort(items, new ItemComparator());
        getItemFromFB();
    }

    private ArrayList<BaseDTO> getItemFromRealm(long millis) {

        final ArrayList<BaseDTO> items = new ArrayList<>();
        long today[] = DateHelper.getInstance().getStartEndDate(millis);
        long startMillis = today[0];
        long endMillis = today[1];

        RealmResults<RealmObject> cData = RealmHelper.getInstance().DataHighlightLoad(CallData.class, "date", startMillis, endMillis);
        RealmResults<RealmObject> ggData = RealmHelper.getInstance().DataHighlightLoad(GpsGroupData.class, "end", startMillis, endMillis);
        RealmResults<RealmObject> smsTradeDatas = RealmHelper.getInstance().DataHighlightLoad(SmsTradeData.class, "date", startMillis, endMillis);
        RealmResults<RealmObject> pgData = RealmHelper.getInstance().DataHighlightLoad(PhotoGroupData.class, "start", startMillis, endMillis - 1);
//        RealmResults<RealmObject> ngDatas = RealmHelper.getInstance().DataHighlightLoad(NotifyGroupData.class, "start", startMillis, endMillis - 1);
//        RealmResults<RealmObject> smsGroupDatas = RealmHelper.getInstance().DataHighlightLoad(SmsGroupData.class, "start", startMillis, endMillis - 1);
        RealmResults<RealmObject> customData = RealmHelper.getInstance().DataLoad(CustomData.class, "date", startMillis, endMillis);


        DatePinDTO datePinData = new DatePinDTO();
        datePinData.setDate(startMillis);
        items.add(datePinData);

        for (RealmObject cd : customData) {
            items.add(new CustomDTO((CustomData) cd));
        }
        for (RealmObject g : ggData) {
            items.add(new GpsGroupDTO((GpsGroupData) g));
        }
        for (RealmObject c : cData) {
            items.add(new CallDTO((CallData) c));
        }
        for (RealmObject pg : pgData) {
            items.add(new PhotoGroupDTO((PhotoGroupData) pg));
        }
        for (RealmObject std : smsTradeDatas) {
            items.add(new SmsTradeDTO((SmsTradeData) std));
        }


        return items;
    }

    private void getItemFromFB() {
        DatabaseReference myRef = FirebaseHelper.getInstance(context).getCurrentUserRef().child("post");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("firebasesync", snapshot.getValue().toString());
                    DatabaseReference postRef = FirebaseHelper.getInstance(context).getPostsRef().child(snapshot.getValue().toString());
                    postRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            int type = Integer.parseInt(dataSnapshot.child("class").getValue().toString());
                            String uid = dataSnapshot.child("uid").getValue().toString();
                            Log.d("firebasesync", uid);
                            BaseDTO item;
                            switch (type) {
                                case RealmClassHelper.CALL_DATA:
                                    item = dataSnapshot.child("item").getValue(CallDTO.class);
                                    break;
                                case RealmClassHelper.CUSTOM_DATA:
                                    item = dataSnapshot.child("item").getValue(CustomDTO.class);
                                    break;
                                case RealmClassHelper.PHOTO_DATA:
                                    item = dataSnapshot.child("item").getValue(PhotoDTO.class);
                                    break;
                                case RealmClassHelper.PHOTO_GROUP_DATA:
                                    item = dataSnapshot.child("item").getValue(PhotoGroupDTO.class);
                                    break;
                                case RealmClassHelper.SMS_TRADE_DATA:
                                    item = dataSnapshot.child("item").getValue(SmsTradeDTO.class);
                                    break;
                                default:
                                    item = null;
                            }

                            items.add(item);
                            Collections.sort(items, new ItemComparator());
                            adapter.updateItem(items);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayRecyclerView() {

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CalendarPinAdapter(this, realm);
        adapter.updateItem(items);
        rv.addItemDecoration(new PinnedHeaderItemDecoration());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
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
    public void onNotifyItemClick(BaseDTO item) {
        Intent intent = new Intent(this, UnitActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmClassHelper.getInstance().NOTIFY_GROUP_DATA);
        startActivity(intent);
    }

    @Override
    public void onSmsGroupItemClick(BaseDTO item) {
        Intent intent = new Intent(this, UnitActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmClassHelper.getInstance().SMS_GROUP_DATA);
        startActivity(intent);
    }

    @Override
    public void onSmsTradeItemClick(BaseDTO item) {
        Intent intent = new Intent(this, SmsTradeActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmClassHelper.getInstance().SMS_TRADE_DATA);
        startActivity(intent);
    }

    @Override
    public void onDatePinClick(BaseDTO item) {
        Intent intent = new Intent(this, DayActivity.class);
        intent.putExtra("date", item.getDate());
        startActivity(intent);
    }

    @Override
    public void onPhotoGroupItemClick(BaseDTO item) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmClassHelper.getInstance().PHOTO_GROUP_DATA);

        startActivity(intent);
    }

    @Override
    public void onGpsItemClick(BaseDTO item) {
        Intent intent = new Intent(this, GpsStillActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmClassHelper.getInstance().GPS_DATA);
        startActivity(intent);
    }

    @Override
    public void onCallItemClick(BaseDTO item) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmClassHelper.getInstance().CALL_DATA);
        startActivity(intent);
    }

    @Override
    public void onGpsGroupItemClick(BaseDTO item) {

        Intent intent = new Intent(this, GpsGroupActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmClassHelper.getInstance().GPS_GROUP_DATA);
        startActivity(intent);
    }

    @Override
    public void onCustomItemClick(BaseDTO item) {

    }


}
