package com.example.android.selfns.DetailView;

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

import com.example.android.selfns.Data.DTO.Detail.SmsTradeDTO;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.R;
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

import org.parceler.Parcels;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SmsTradeActivity extends AppCompatActivity  implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {


    @BindView(R.id.sms_trade_address)
    TextView address;
    @BindView(R.id.sms_trade_date)
    TextView date;
    @BindView(R.id.sms_trade_iv)
    ImageView iv;
    @BindView(R.id.sms_trade_place)
    TextView place;
    @BindView(R.id.sms_trade_type)
    TextView type;
    @BindView(R.id.sms_trade_comment)
    TextView comment;
    @BindView(R.id.sms_trade_comment_button)
    Button commentBtn;
    @BindView(R.id.sms_trade_date_button)
    Button dateBtn;
    @BindView(R.id.sms_trade_place_button)
    Button placeBtn;

    private Context context = this;
    private GoogleApiClient mGoogleApiClient;
    private SmsTradeDTO smsTradeData;
    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_trade);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        realm = Realm.getDefaultInstance();
        Class c = RealmClassHelper.getInstance().getClass(getIntent().getIntExtra("type", -1));
        long id = getIntent().getLongExtra("id", -1);
        smsTradeData = Parcels.unwrap(getIntent().getParcelableExtra("item"));


        if (smsTradeData != null) {
            initMap();
            bindData();
            initBtnListener();
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void bindData() {
        address.setText(smsTradeData.getPlace());
        date.setText(DateHelper.getInstance().toDateString("HH:mm", smsTradeData.getDate()));
        place.setText(smsTradeData.getPlace());
        comment.setText(smsTradeData.getComment());
        type.setText("asdf");
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
                LatLng latlng = new LatLng(smsTradeData.getLat(), smsTradeData.getLng());
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
                final Calendar cal = DateHelper.getInstance().toDate(smsTradeData.getDate());
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        final long millis = DateHelper.getInstance().toMillis(cal, hourOfDay, minute);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                smsTradeData.setDate(millis);
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
                ItemInteractionUtil.getInstance(context).show((AppCompatActivity) context, smsTradeData.getId(), RealmClassHelper.SMS_TRADE_DATA);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng sydney = new LatLng(smsTradeData.getLat(), smsTradeData.getLng());
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(smsTradeData.getPlace()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();

                ItemInteractionUtil.getInstance(context).setPlace(smsTradeData);
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

        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, smsTradeData.getOriginId())
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

}
