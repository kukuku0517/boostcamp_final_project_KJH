package com.example.android.selfns.DetailView;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.selfns.Data.DTO.Detail.CallDTO;
import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.RealmData.UnitData.CallData;
import com.example.android.selfns.Data.RealmData.UnitData.PhotoData;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.parceler.Parcels;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;

import static android.R.attr.id;
import static android.R.attr.tag;

public class CallActivity extends AppCompatActivity {

    @BindView(R.id.call_detail_comment)
    TextView comment;
    @BindView(R.id.call_date)
    TextView date;
    @BindView(R.id.call_delete)
    TextView deleteBtn;
    @BindView(R.id.call_tag)
    TextView tag;

    private Context context = this;

    CallDTO callDTO;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();
        callDTO = Parcels.unwrap(getIntent().getParcelableExtra("item"));

        initVIew();
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                initVIew();
            }
        });
        initBtnListener();
    }

    private void initVIew() {
        CallData callData = realm.where(CallData.class).equalTo("id", callDTO.getId()).findFirst();
        if (callData != null) {
            callDTO = new CallDTO(callData);
        }

        comment.setText(callDTO.getComment());
        String dateString = DateHelper.getInstance().toDateString("HH : mm", callDTO.getDate());
        date.setText(dateString);
    }

    private void initBtnListener() {

        final Calendar cal = DateHelper.getInstance().toDate(callDTO.getDate());
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                final long millis = DateHelper.getInstance().toMillis(cal, hourOfDay, minute);
                ItemInteractionUtil.getInstance(context).setDate(callDTO, millis);
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
                ItemInteractionUtil.getInstance(context).show((AppCompatActivity) context, callDTO.getId(), RealmClassHelper.CALL_DATA);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).deleteItem(callDTO);
            }
        });
        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).tagFriend((AppCompatActivity) context, callDTO);
            }
        });
    }


}
