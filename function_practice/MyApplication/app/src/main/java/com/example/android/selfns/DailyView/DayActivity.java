package com.example.android.selfns.DailyView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.android.selfns.DailyView.Adapter.DayAdapter;
import com.example.android.selfns.Background.ContentProviderData;
import com.example.android.selfns.Data.DTO.Detail.CallDTO;
import com.example.android.selfns.Data.DTO.Detail.CustomDTO;
import com.example.android.selfns.Data.DTO.Detail.GpsDTO;
import com.example.android.selfns.Data.DTO.Detail.SmsTradeDTO;
import com.example.android.selfns.Data.DTO.Group.GpsGroupDTO;
import com.example.android.selfns.Data.DTO.Group.NotifyGroupDTO;
import com.example.android.selfns.Data.DTO.Group.PhotoGroupDTO;
import com.example.android.selfns.Data.DTO.Group.SmsGroupDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.GroupData.GpsGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.NotifyGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.PhotoGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.SmsGroupData;
import com.example.android.selfns.DetailView.CallActivity;
import com.example.android.selfns.Data.RealmData.UnitData.CustomData;
import com.example.android.selfns.DetailView.GpsGroupActivity;
import com.example.android.selfns.DetailView.GpsStillActivity;
import com.example.android.selfns.DetailView.SmsTradeActivity;
import com.example.android.selfns.GroupView.PhotoActivity;
import com.example.android.selfns.Data.RealmData.UnitData.CallData;
import com.example.android.selfns.Data.RealmData.UnitData.GpsData;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.ItemComparator;
import com.example.android.selfns.Helper.RealmHelper;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmGpsObject;
import com.example.android.selfns.Data.RealmData.UnitData.SmsTradeData;
import com.example.android.selfns.GroupView.UnitActivity;
import com.example.android.selfns.Interface.CardItemClickListener;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class DayActivity extends AppCompatActivity implements CardItemClickListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    @BindView(R.id.rv_day)
    RecyclerView rv;
    @BindView(R.id.fab_day)
    FloatingActionButton fab;
    @BindView(R.id.progressBar)
    ProgressBar pb;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.day_maplayout)
    View mapLayout;

    Realm realmAsync;
    private Realm realm;
    private RecyclerView.LayoutManager layoutManager;
    private DayAdapter adapter;
    private ArrayList<BaseDTO> items = new ArrayList<>();
    private List<MapItem> mapItems = new ArrayList<>();
    private long startMillis, endMillis, quarter;
    private boolean isMapOpen = false;

    private View marker_root_view;
    private ImageView iv_marker;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_day, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_down);
                Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up);
                slide_up.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mapLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                if (isMapOpen) {
                    mapLayout.startAnimation(slide_up);
                } else {
                    mapLayout.setVisibility(View.VISIBLE);
                    mapLayout.startAnimation(slide_down);
                }
                isMapOpen = !isMapOpen;
                break;
            case R.id.action_1:
                rv.smoothScrollToPosition(1);
                break;
            case R.id.action_2:
                rv.smoothScrollToPosition(10);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        long millis = getIntent().getLongExtra("date", -1);
        long[] today = DateHelper.getInstance().getStartEndDate(millis);

        startMillis = today[0];
        endMillis = today[1];
        quarter = (endMillis - startMillis) / 4;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
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
//
//        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        getSharedPreferences("setting", Activity.MODE_PRIVATE);
////        mPref.edit().putBoolean("init", true).commit();
//
//        if (!mPref.contains("init")) {
//            Log.d("pref", "0");
//            new RealmAsync().execute(0);
//            new RealmAsync().execute(1);
//            new RealmAsync().execute(2);
//        } else if (!mPref.getBoolean("init", false)) {
//            Log.d("pref", "1");
//            new RealmAsync().execute(0);
//            new RealmAsync().execute(1);
//            new RealmAsync().execute(2);
//        } else {
//            Log.d("pref", "2");
//            displayRecyclerView();
//        }
        displayRecyclerView();
    }

    GoogleMap map;

    @Override
    public void onMapReady(GoogleMap mMap) {
        double lat = 33, lng = 125;
        if (!mapItems.isEmpty()) {
            lat = mapItems.get(0).getItem().getLat();
            lng = mapItems.get(0).getItem().getLng();
        }
        map = mMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14.0f));
        map.setOnInfoWindowClickListener(this);

        setMarkerData(map);
//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                MapItem mapData = (MapItem) marker.getTag();
//                MyRealmGpsObject data = mapData.getItem();
//                marker_root_view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.map_marker_item, null);
//                tv_marker = (TextView) marker_root_view.findViewById(R.id.marker_tv);
//                tv_marker.setText(data.getPlace());
//                Log.d("googleMarker", data.getPlace());
//                if (data instanceof PhotoGroupData) {
//                    PhotoGroupData photoData = (PhotoGroupData) data;
//                    iv_marker = (ImageView) marker_root_view.findViewById(R.id.marker_iv);
//                    iv_marker.setVisibility(View.VISIBLE);
//                    Bitmap myBitmap = BitmapFactory.decodeFile(photoData.getPhotoss().get(0).getPath());
//                    iv_marker.setImageBitmap(myBitmap);
//                }
//                return marker_root_view;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                return null;
//            }
//        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                MapItem item = (MapItem) marker.getTag();
                rv.smoothScrollToPosition(item.getPosition());

            }
        });
    }

    private void setMarkerData(GoogleMap mMap) {
        mMap.clear();
        PolylineOptions rectOptions = new PolylineOptions();

        for (int i = 0; i < mapItems.size(); i++) {
            MyRealmGpsObject data = mapItems.get(i).getItem();

            LatLng position = new LatLng(data.getLat(), data.getLng());
            rectOptions.add(position);

            MarkerOptions marker = new MarkerOptions()
                    .position(position)
                    .title(data.getPlace())
                    .snippet(data.getPlace());

            if (data instanceof PhotoGroupData) {
                Bitmap icon = createIconFromData(data);
                marker.icon(BitmapDescriptorFactory.fromBitmap(icon));
            }
            Marker mark = mMap.addMarker(marker);
            mark.setTag(mapItems.get(i));
        }
        mMap.addPolyline(rectOptions);
    }

    @NonNull
    private Bitmap createIconFromData(MyRealmGpsObject data) {
        IconGenerator generator = new IconGenerator(this);
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.map_marker_item, null);
        iv_marker = (ImageView) marker_root_view.findViewById(R.id.marker_iv);
        PhotoGroupData photoData = (PhotoGroupData) data;
        iv_marker = (ImageView) marker_root_view.findViewById(R.id.marker_iv);
        iv_marker.setVisibility(View.VISIBLE);
        Bitmap myBitmap = BitmapFactory.decodeFile(photoData.getPhotoss().get(0).getPath());
        iv_marker.setImageBitmap(myBitmap);
        generator.setContentView(marker_root_view);

        return generator.makeIcon();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.setSnippet(marker.getTitle());
    }

    private class RealmAsync extends AsyncTask<Integer, Void, Void> {

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

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Log.d("dayscroll", "a");
//            rv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                @Override
//                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                    Log.d("dayscroll", String.valueOf(rv.getLayoutManager().getPosition(v)));
//                }
//            });
//        } else {
//            Log.d("dayscroll", "b");
//            rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//
//                }
//
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
////                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
////                    layoutManager.findFirstVisibleItemPosition();
////
////                    View view = recyclerView.getFocusedChild();
////                    Log.d("scrollchild", "scroll");
////                    if (view != null) {
////                        Log.d("scrollchild", String.valueOf(recyclerView.indexOfChild(view)));
////                    }
//                }
//            });
//        }
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                getItemFromRealm();
                adapter.updateItem(items);
                adapter.notifyDataSetChanged();
//                setMarkerData(map);
            }
        });
    }

    private void getItemFromRealm() {
        items.clear();
        RealmResults<RealmObject> cData = RealmHelper.getInstance().DataLoad(CallData.class, "date", startMillis, endMillis);
        RealmResults<RealmObject> pgData = RealmHelper.getInstance().DataLoad(PhotoGroupData.class, "start", startMillis, endMillis);
        RealmResults<RealmObject> smsTradeDatas = RealmHelper.getInstance().DataLoad(SmsTradeData.class, "date", startMillis, endMillis);
//        RealmResults<GpsData> gData = RealmHelper.getInstance().gpsDataLoad("date", startMillis, endMillis);
        RealmResults<RealmObject> ggData = RealmHelper.getInstance().DataLoad(GpsGroupData.class, "end", startMillis, endMillis);
        RealmResults<RealmObject> customData = RealmHelper.getInstance().DataLoad(CustomData.class, "date", startMillis, endMillis);

        RealmResults<NotifyGroupData> ngDatas = RealmHelper.getInstance().notifyGroupDataLoad("end", startMillis, endMillis - 1);
        RealmResults<SmsGroupData> smsGroupDatas = RealmHelper.getInstance().smsGroupDataLoad("date", startMillis, endMillis - 1);

//        for (GpsData g : gData) {
//            items.add(g);
//        }
        for (RealmObject cd : customData) {
            items.add(new CustomDTO((CustomData) cd));
        }
        for (RealmObject gg : ggData) {
            items.add(new GpsGroupDTO((GpsGroupData) gg));
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
        for (SmsGroupData m : smsGroupDatas) {
            items.add(new SmsGroupDTO(m));
        }
        for (NotifyGroupData nn : ngDatas) {

            items.add(new NotifyGroupDTO((nn)));
        }

        Collections.sort(items,  new ItemComparator());

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof GpsData) {
                mapItems.add(new MapItem(i, RealmClassHelper.GPS_DATA, (MyRealmGpsObject) items.get(i)));
            } else if (items.get(i) instanceof SmsTradeData) {
                mapItems.add(new MapItem(i, RealmClassHelper.SMS_TRADE_DATA, (MyRealmGpsObject) items.get(i)));
            } else if (items.get(i) instanceof PhotoGroupData) {
                mapItems.add(new MapItem(i, RealmClassHelper.PHOTO_GROUP_DATA, (MyRealmGpsObject) items.get(i)));
            }
        }
    }


    @Override
    public void onNotifyItemClick(BaseDTO item) {
        Intent intent = new Intent(this, UnitActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().NOTIFY_GROUP_DATA);
        intent.putExtra("item", Parcels.wrap((NotifyGroupDTO)item));
        startActivity(intent);
    }

    @Override
    public void onSmsGroupItemClick(BaseDTO item) {
        Intent intent = new Intent(this, UnitActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().SMS_GROUP_DATA);
        intent.putExtra("item", Parcels.wrap((SmsGroupDTO)item));
        startActivity(intent);
    }


    @Override
    public void onSmsTradeItemClick(BaseDTO item) {
        Intent intent = new Intent(this, SmsTradeActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().SMS_TRADE_DATA);
        intent.putExtra("item", Parcels.wrap((SmsTradeDTO)item));
        startActivity(intent);
    }

    @Override
    public void onPhotoGroupItemClick(BaseDTO item) {
        Intent intent = new Intent(this, PhotoActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().PHOTO_GROUP_DATA);
        intent.putExtra("item", Parcels.wrap((PhotoGroupDTO)item));
        startActivity(intent);
    }



    @Override
    public void onGpsItemClick(BaseDTO item) {
        Intent intent = new Intent(this, GpsStillActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().GPS_DATA);
        intent.putExtra("item", Parcels.wrap((GpsDTO)item));
        startActivity(intent);
    }

    @Override
    public void onCallItemClick(BaseDTO item) {
        Intent intent = new Intent(this, CallActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().CALL_DATA);
        intent.putExtra("item", Parcels.wrap((CallDTO)item));
        startActivity(intent);
    }

    @Override
    public void onCustomItemClick(BaseDTO item) {
        Intent intent = new Intent(this, CallActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().CALL_DATA);
        intent.putExtra("item", Parcels.wrap((CustomDTO)item));
        startActivity(intent);
    }
    @Override
    public void onGpsGroupItemClick(BaseDTO item) {
        Intent intent = new Intent(this, GpsGroupActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().GPS_GROUP_DATA);
        intent.putExtra("item", Parcels.wrap((GpsGroupDTO)item));

        startActivity(intent);

    }

    class MapItem {
        private int position;
        private int type;

        public MyRealmGpsObject getItem() {
            return item;
        }

        public void setItem(MyRealmGpsObject item) {
            this.item = item;
        }

        private MyRealmGpsObject item;

        public MapItem(int position, int type, MyRealmGpsObject item) {
            this.position = position;
            this.type = type;
            this.item = item;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }


    }
}
