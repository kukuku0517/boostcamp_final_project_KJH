package com.example.android.selfns.DailyView;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.selfns.Data.RealmData.UnitData.GpsData;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.RealmHelper;
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

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class WriteActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.write_title)
    MaterialEditText title;

    @BindView(R.id.write_comment)
    MaterialEditText comment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.write_place)
    TextView place;
    @BindView(R.id.write_tag)
    TextView tag;
    @BindView(R.id.write_fab)
    FloatingActionButton fab;
    @BindView(R.id.write_date)
    TextView date;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    private Realm realm;

    private double lat, lng;
    private long dateMIllis;
    private String placename;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        toolbar.setTitle("새로운 글");
        realm = Realm.getDefaultInstance();

        initMap();
        initBtnListener();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().equals("")){
                    Toast.makeText(context,"제목을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }else if(comment.getText().equals("")){
                    Toast.makeText(context,"내용을 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                RealmHelper.getInstance().customDataSave(title.getText().toString(), comment.getText().toString(),
                        placename, lat, lng, dateMIllis, tag.getText().toString());
                onBackPressed();
            }
        });
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
                        date.setText(DateHelper.getInstance().toDateString("HH:mm", millis));
                        dateMIllis = millis;

                    }
                };
                TimePickerDialog dialog = new TimePickerDialog(context, listener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
                dialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place placepicker = PlacePicker.getPlace(data, this);
                lat = (placepicker.getLatLng().latitude);
                lng = (placepicker.getLatLng().longitude);
                placename = (placepicker.getName().toString());
                place.setText(placename);
                LatLng sydney = new LatLng(lat, lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {


                RealmResults<GpsData> result = realm.where(GpsData.class).findAll();
                if (result.size() != 0) {
                    lat = result.last().getLat();
                    lng = result.last().getLng();
                    placename = result.last().getPlace();
                }


                place.setText(placename);

                LatLng sydney = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(sydney)
                        .title(placename));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));

            }
        });

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
