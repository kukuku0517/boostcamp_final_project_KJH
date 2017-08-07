package com.example.android.contentproviderbroadcastreceiver.Main;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.PhotoData;
import com.example.android.contentproviderbroadcastreceiver.Interface.CommentBtnClickListener;
import com.example.android.contentproviderbroadcastreceiver.Interface.CommentUtil;
import com.example.android.contentproviderbroadcastreceiver.Interface.DateHelper;
import com.example.android.contentproviderbroadcastreceiver.Interface.PhotoItemClickListener;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class DetailPhotoActivity extends AppCompatActivity {

    @BindView(R.id.photo_detail_iv)
    ImageView iv;
    @BindView(R.id.photo_detail_comment)
    TextView comment;
    @BindView(R.id.photo_detail_date)
    TextView date;
    @BindView(R.id.photo_detail_place)
    TextView place;


    @BindView(R.id.photo_detail_place_button)
    Button placeBtn;

    @BindView(R.id.photo_detail_date_button)
    Button dateBtn;

    @BindView(R.id.photo_detail_comment_button)
    Button commentBtn;

    private Context context = this;
    private PhotoData photoDataGlobal;
private Realm realmGlobal;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                realmGlobal.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        photoDataGlobal.setLat(place.getLatLng().latitude);

                        photoDataGlobal.setLng(place.getLatLng().longitude);
                        photoDataGlobal.setPlace(place.getName().toString());


                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_photo);
        ButterKnife.bind(this);

        Long id = getIntent().getLongExtra("id", -1);
        int type = getIntent().getIntExtra("class", -1);

        final Realm realm = Realm.getDefaultInstance();
        final PhotoData photoData = realm.where(PhotoData.class).equalTo("id", id).findFirst();
        photoDataGlobal=photoData;
        realmGlobal=realm;
        if (photoData != null) {
            Glide.with(this).load(photoData.getPath()).into(iv);

            comment.setText(photoData.getComment());
            String dateString = DateHelper.getInstance().toDateString("HH : mm", photoData.getDate());


            date.setText(dateString);
            place.setText(photoData.getPlace());
        }


        placeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                        getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//                    @Override
//                    public void onPlaceSelected(Place place) {
//                        // TODO: Get info about the selected place.
//                        Log.i("placeauto", "Place: " + place.getName());
//                    }
//
//                    @Override
//                    public void onError(Status status) {
//                        // TODO: Handle the error.
//                        Log.i("placeauto", "An error occurred: " + status);
//                    }
//                });

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

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        photoData.setDate(millis);
                    }
                });
            }
        };
        final TimePickerDialog dialog = new TimePickerDialog(this, listener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }


        });

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.getInstance().show((AppCompatActivity) context, photoData);
            }
        });
    }


}
