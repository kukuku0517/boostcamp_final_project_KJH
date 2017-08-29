package com.example.android.selfns.DetailView;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.selfns.Data.DTO.Detail.CallDTO;
import com.example.android.selfns.Data.DTO.Detail.CustomDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.GpsableDTO;
import com.example.android.selfns.Data.RealmData.UnitData.CallData;
import com.example.android.selfns.Data.RealmData.UnitData.CustomData;
import com.example.android.selfns.Data.RealmData.UnitData.GpsData;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.RealmClassHelper;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import org.parceler.Parcels;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CustomActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.custom_edit_title)
    TextView title;

    @BindView(R.id.custom_edit_comment)
    TextView comment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.custom_place)
    TextView place;
    @BindView(R.id.custom_tag)
    TextView tag;
    @BindView(R.id.custom_date)
    TextView date;
    @BindView(R.id.photo_detail_iv)
    ImageView iv;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    private Realm realm;

    private double lat, lng;
    private long dateMIllis;
    private String placename;
    private Context context = this;
    CustomDTO customDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("새로운 글");
        realm = Realm.getDefaultInstance();

        Glide.with(this).load(R.drawable.write).into(iv);

        initVIew();
        initMap();
        initBtnListener();
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                initVIew();
                updateMarker();
            }
        });

    }

    private void initVIew() {

        customDTO = Parcels.unwrap(getIntent().getParcelableExtra("item"));

        CustomData customData = realm.where(CustomData.class).equalTo("id", customDTO.getId()).findFirst();
        if (customData != null) {
            customDTO = new CustomDTO(customData);
        }

        title.setText(customDTO.getTitle());
        comment.setText(customDTO.getComment());
        String dateString = DateHelper.getInstance().toDateString("HH : mm", customDTO.getDate());
        date.setText(dateString);

        placename = customDTO.getPlace();
        place.setText(placename);
        lat = customDTO.getLat();
        lng = customDTO.getLng();

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.custom_map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

    }

    private void initBtnListener() {
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).editTitle((AppCompatActivity) context, customDTO.getId(), RealmClassHelper.CUSTOM_DATA);
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).show((AppCompatActivity) context, customDTO.getId(), RealmClassHelper.CUSTOM_DATA);
            }
        });

        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latlng = new LatLng(lat, lng);
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

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = DateHelper.getInstance().toDate(System.currentTimeMillis());
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        final long millis = DateHelper.getInstance().toMillis(cal, hourOfDay, minute);
                        ItemInteractionUtil.getInstance(context).setDate(customDTO, millis);

                    }
                };
                TimePickerDialog dialog = new TimePickerDialog(context, listener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
                dialog.show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                customDTO.setLat(place.getLatLng().latitude);
                customDTO.setLat(place.getLatLng().longitude);
                customDTO.setPlace(place.getName().toString());
                customDTO.setOriginId(place.getId());
                ItemInteractionUtil.getInstance(context).setPlace(customDTO);

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateMarker();


    }

    private void updateMarker() {
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
