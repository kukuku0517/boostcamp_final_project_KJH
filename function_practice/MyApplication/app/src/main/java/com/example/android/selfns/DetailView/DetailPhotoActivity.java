package com.example.android.selfns.DetailView;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.RealmData.UnitData.PhotoData;
import com.example.android.selfns.ExtraView.Friend.TagAdapter;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class DetailPhotoActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.photo_detail_iv)
    ImageView iv;
    @BindView(R.id.photo_detail_comment)
    TextView comment;
    @BindView(R.id.photo_detail_date)
    TextView date;
    @BindView(R.id.photo_detail_place)
    TextView place;

    @BindView(R.id.photo_detail_people)
    TextView tag;

    @BindView(R.id.photo_detail_delete)
    TextView deleteBtn;

    private Context context = this;
    PhotoDTO photoData;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private double lat, lng;
    Realm realm;
    private String placename;

    @BindView(R.id.rv_tag)
    RecyclerView rvTag;
    RecyclerView.LayoutManager layoutManager;
    TagAdapter adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                photoData.setLat(place.getLatLng().latitude);
                photoData.setLat(place.getLatLng().longitude);
                photoData.setPlace(place.getName().toString());
                photoData.setOriginId(place.getId());
                ItemInteractionUtil.getInstance(context).setPlace(photoData);

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_photo);
        ButterKnife.bind(this);


        realm = Realm.getDefaultInstance();
//        final Realm realm = Realm.getDefaultInstance();
//        Class c = RealmClassHelper.getInstance().getClass(getIntent().getIntExtra("type", -1));
//        long id = getIntent().getLongExtra("id", -1);
//        final PhotoData photoData = (PhotoData) realm.where(c).equalTo("id", id).findFirst();
        photoData = Parcels.unwrap(getIntent().getParcelableExtra("item"));

        initView();

        initMap();
        initBtnListener();

        final List<FriendDTO> items = new ArrayList<>();
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        adapter = new TagAdapter(context);
        adapter.updateItem(items);
        rvTag.setHasFixedSize(true);
        rvTag.setLayoutManager(layoutManager);
        rvTag.setAdapter(adapter);

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                initView();
            }
        });
    }

    private void initView() {
        PhotoData pd = realm.where(PhotoData.class).equalTo("id",photoData.getId()).findFirst();
        if(pd!=null){
            photoData=new PhotoDTO(pd);
        }

        if (photoData != null) {
            Glide.with(this).load(photoData.getPath()).into(iv);
            comment.setText(photoData.getComment());
            String dateString = DateHelper.getInstance().toDateString("HH : mm", photoData.getDate());
            date.setText(dateString);
            place.setText(photoData.getPlace());
        }

        lat = photoData.getLat();
        lng = photoData.getLng();
        placename = photoData.getPlace();
        place.setText(placename);
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

    }


    private void initBtnListener() {
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latlng = new LatLng(photoData.getLat(), photoData.getLng());
                Intent intent = null;
                try {
                    intent = new PlacePicker.IntentBuilder().setLatLngBounds(new LatLngBounds(latlng, latlng)).build((Activity) context);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        final Calendar cal = DateHelper.getInstance().toDate(photoData.getDate());
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                final long millis = DateHelper.getInstance().toMillis(cal, hourOfDay, minute);
                ItemInteractionUtil.getInstance(context).setDate(photoData, millis);
            }
        };
        final TimePickerDialog dialog = new TimePickerDialog(this, listener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }


        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).show((AppCompatActivity) context, photoData.getId(), RealmClassHelper.PHOTO_DATA);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).deletePhotoItem(photoData);
            }
        });
        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).tagFriend((AppCompatActivity) context, photoData);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sydney)
                .title(placename));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
