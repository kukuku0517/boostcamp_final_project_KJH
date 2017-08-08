package com.example.android.contentproviderbroadcastreceiver.DetailView;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.Helper.CommentUtil;
import com.example.android.contentproviderbroadcastreceiver.Helper.DateHelper;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmDataHelper;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class GpsStillActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.gps_still_address)
    TextView address;
    @BindView(R.id.gps_still_date)
    TextView date;
    @BindView(R.id.gps_still_iv)
    ImageView iv;
    @BindView(R.id.gps_still_place)
    TextView place;
    @BindView(R.id.gps_still_type)
    TextView type;
    @BindView(R.id.gps_still_comment)
    TextView comment;
    @BindView(R.id.gps_still_comment_button)
    Button commentBtn;
    @BindView(R.id.gps_still_date_button)
    Button dateBtn;
    @BindView(R.id.gps_still_place_button)
    Button placeBtn;

    private Context context = this;
    private GoogleApiClient mGoogleApiClient;
    private GpsData gpsData;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_still);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        realm = Realm.getDefaultInstance();
        Class c = RealmDataHelper.getInstance().getClass(getIntent().getIntExtra("type", -1));
        long id = getIntent().getLongExtra("id", -1);
        gpsData = (GpsData) realm.where(c).equalTo("id", id).findFirst();
        if (gpsData != null) {
            initMap();
            bindData();
            initBtnListener();
        }
    }

    private void initBtnListener() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        placeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latlng = new LatLng(gpsData.getLat(), gpsData.getLng());
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
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = DateHelper.getInstance().toDate(gpsData.getDate());
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        final long millis = DateHelper.getInstance().toMillis(cal, hourOfDay, minute);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                gpsData.setDate(millis);
                            }
                        });
                    }
                };
                TimePickerDialog dialog = new TimePickerDialog(context, listener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
                dialog.show();
            }
        });
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.getInstance().show((AppCompatActivity) context, gpsData.getId(), RealmDataHelper.GPS_DATA);
            }
        });
    }

    private void bindData() {
        address.setText(gpsData.getPlace());
        date.setText(DateHelper.getInstance().toDateString("HH:mm", gpsData.getDate()));
        place.setText(gpsData.getPlace());
        comment.setText(gpsData.getComment());
        type.setText("asdf");
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng sydney = new LatLng(gpsData.getLat(), gpsData.getLng());
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(gpsData.getPlace()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        gpsData.setLat(place.getLatLng().latitude);
                        gpsData.setLng(place.getLatLng().longitude);
                        gpsData.setPlace(place.getName().toString());
                        gpsData.setOriginId(place.getId());
                    }
                });
            }
        }
    }

    private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
            = new ResultCallback<PlacePhotoResult>() {
        @Override
        public void onResult(PlacePhotoResult placePhotoResult) {
            if (!placePhotoResult.getStatus().isSuccess()) {
                return;
            }
            iv.setImageBitmap(placePhotoResult.getBitmap());


        }
    };

    private void placePhotosAsync() {

        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, gpsData.getOriginId())
                .setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {


                    @Override
                    public void onResult(PlacePhotoMetadataResult photos) {
                        if (!photos.getStatus().isSuccess()) {
                            return;
                        }

                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        if (photoMetadataBuffer.getCount() > 0) {
                            // Display the first bitmap in an ImageView in the size of the view
                            photoMetadataBuffer.get(0)
                                    .getScaledPhoto(mGoogleApiClient, iv.getWidth(),
                                            iv.getHeight())
                                    .setResultCallback(mDisplayPhotoResultCallback);
                        }
                        photoMetadataBuffer.release();
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        placePhotosAsync();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
