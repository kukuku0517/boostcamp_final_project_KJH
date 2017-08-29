package com.example.android.selfns.GroupView;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.selfns.DailyView.Adapter.DayAdapter;
import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.DTO.Group.PhotoGroupDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.GroupData.PhotoGroupData;
import com.example.android.selfns.DetailView.DetailPhotoActivity;
import com.example.android.selfns.ExtraView.Friend.TagAdapter;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.Helper.ItemInteractionUtil;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Helper.RetrofitHelper;
import com.example.android.selfns.Interface.DataReceiveListener;
import com.example.android.selfns.Interface.PhotoItemClickListener;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mancj.slideup.SlideUp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class PhotoActivity extends AppCompatActivity implements PhotoItemClickListener {

    @BindView(R.id.photo_rv)
    RecyclerView rv;

    @BindView(R.id.rv_tag)
    RecyclerView rvTag;
    @BindView(R.id.photo_menu)
    View view;
    @BindView(R.id.fab_photo)
    FloatingActionButton fab;

    @BindView(R.id.photo_date)
    TextView date;
    @BindView(R.id.photo_place)
    TextView place;
    @BindView(R.id.photo_delete)
    TextView delete;
    @BindView(R.id.photo_comment)
    TextView comment;

    Context context = this;
    Realm realm;
    private RecyclerView.LayoutManager layoutManager;
    private DayAdapter adapter;
    private ArrayList<BaseDTO> items = new ArrayList<>();

    HashMap<Integer, List<FriendDTO>> usersHash = new HashMap<>();

    private RecyclerView.LayoutManager friendlayoutManager;
    private TagAdapter friendadapter;
    private ArrayList<FriendDTO> frienditems = new ArrayList<>();
    SlideUp slideUp;
    PhotoGroupDTO pgData;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                pgData.setPlace(place.getName().toString());
                pgData.setOriginId(place.getId());
                ItemInteractionUtil.getInstance(context).setPlace(pgData);

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        slideUp = new SlideUp.Builder(view)
                .withStartState(SlideUp.State.HIDDEN)
                .withStartGravity(Gravity.BOTTOM)
                .build();
        slideUp.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getVisibility() == View.VISIBLE) {
                    slideUp.hide();
                } else {
                    slideUp.show();
                }
            }
        });
        initView();
        initPhotoRecylerview();
        initFriendRecyclerview();

        initBtnListener();
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                initView();
                initPhotoRecylerview();
                initFriendRecyclerview();
            }
        });

    }

    private void initView() {
        pgData = Parcels.unwrap(getIntent().getParcelableExtra("item"));
        if (pgData != null) {

            PhotoGroupData photoGroupData = realm.where(PhotoGroupData.class).equalTo("id", pgData.getId()).findFirst();
            if (photoGroupData != null) {
                pgData = new PhotoGroupDTO(photoGroupData);
            }

            comment.setText(pgData.getComment());
            String dateString = DateHelper.getInstance().toDateString("HH : mm", pgData.getDate());
            date.setText(dateString);
            place.setText(pgData.getPlace());
        }
    }

    private void initFriendRecyclerview() {
        friendlayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        friendadapter = new TagAdapter(context);
        friendadapter.updateItem(frienditems);
        rvTag.setHasFixedSize(true);
        rvTag.setLayoutManager(friendlayoutManager);
        rvTag.setAdapter(friendadapter);

        if (pgData.getShare() == 1) {
            RetrofitHelper.getInstance(context).getTaggedFriends(pgData.get_id(), new DataReceiveListener<ArrayList<FriendDTO>>() {
                @Override
                public void onReceive(ArrayList<FriendDTO> response) {
                    frienditems.clear();
                    if(response.size()!=0){
                        for (FriendDTO friend : response) {
                            frienditems.add(friend);
                            friendadapter.updateItem(frienditems);
                            friendadapter.notifyDataSetChanged();
                        }
                    }

                }
            });
        }

    }

    private void initPhotoRecylerview() {
        Realm realm = Realm.getDefaultInstance();
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        adapter = new DayAdapter(this, realm);
        items.clear();
        for (PhotoDTO p : pgData.getPhotoss()) {
            items.add(p);
        }

        adapter.updateItem(items);
        adapter.updateHashItem(usersHash);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }

    private void initBtnListener() {
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latlng = new LatLng(pgData.getLat(), pgData.getLng());
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

        final Calendar cal = DateHelper.getInstance().toDate(pgData.getDate());
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                final long millis = DateHelper.getInstance().toMillis(cal, hourOfDay, minute);
                ItemInteractionUtil.getInstance(context).setDate(pgData, millis);
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
                ItemInteractionUtil.getInstance(context).show((AppCompatActivity) context, pgData.getId(), RealmClassHelper.PHOTO_GROUP_DATA);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemInteractionUtil.getInstance(context).deletePhotoGroupItem(pgData);
            }
        });
    }


    @Override
    public void onPhotoItemClick(BaseDTO item) {
        Intent intent = new Intent(this, DetailPhotoActivity.class);
        intent.putExtra("item", Parcels.wrap(item));
        startActivity(intent);
    }
}
