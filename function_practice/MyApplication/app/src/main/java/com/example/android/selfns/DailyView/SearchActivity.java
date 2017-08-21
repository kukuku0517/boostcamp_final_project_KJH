package com.example.android.selfns.DailyView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.example.android.selfns.DailyView.Adapter.DayAdapter;
import com.example.android.selfns.Data.DTO.Detail.CallDTO;
import com.example.android.selfns.Data.DTO.Detail.CustomDTO;
import com.example.android.selfns.Data.DTO.Detail.GpsDTO;
import com.example.android.selfns.Data.DTO.Detail.NotifyDTO;
import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.DTO.Detail.SmsDTO;
import com.example.android.selfns.Data.DTO.Detail.SmsTradeDTO;
import com.example.android.selfns.Data.DTO.Group.GpsGroupDTO;
import com.example.android.selfns.Data.DTO.Group.NotifyGroupDTO;
import com.example.android.selfns.Data.DTO.Group.NotifyUnitDTO;
import com.example.android.selfns.Data.DTO.Group.PhotoGroupDTO;
import com.example.android.selfns.Data.DTO.Group.SmsGroupDTO;
import com.example.android.selfns.Data.DTO.Group.SmsUnitDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.GroupData.GpsGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.NotifyUnitData;
import com.example.android.selfns.Data.RealmData.GroupData.PhotoGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.SmsUnitData;
import com.example.android.selfns.Data.RealmData.UnitData.CallData;
import com.example.android.selfns.Data.RealmData.UnitData.CustomData;
import com.example.android.selfns.Data.RealmData.UnitData.GpsData;
import com.example.android.selfns.Data.RealmData.UnitData.NotifyData;
import com.example.android.selfns.Data.RealmData.UnitData.PhotoData;
import com.example.android.selfns.Data.RealmData.UnitData.SmsData;
import com.example.android.selfns.Data.RealmData.UnitData.SmsTradeData;
import com.example.android.selfns.DetailView.CallActivity;
import com.example.android.selfns.DetailView.DetailPhotoActivity;
import com.example.android.selfns.DetailView.GpsGroupActivity;
import com.example.android.selfns.DetailView.GpsStillActivity;
import com.example.android.selfns.DetailView.SmsTradeActivity;
import com.example.android.selfns.GroupView.PhotoActivity;
import com.example.android.selfns.GroupView.UnitActivity;
import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.ItemComparator;
import com.example.android.selfns.Helper.JsonUtil;
import com.example.android.selfns.Helper.RealmHelper;
import com.example.android.selfns.Interface.CardItemClickListener;
import com.example.android.selfns.Interface.PhotoItemClickListener;
import com.example.android.selfns.LoginView.UserDTO;
import com.example.android.selfns.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, CardItemClickListener, PhotoItemClickListener {

    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.search_text)
    SearchView searchView;
    @BindView(R.id.search_range_button)
    Button rangeBtn;
    @BindView(R.id.search_start)
    TextView tvStart;
    @BindView(R.id.search_end)
    TextView tvEnd;
    @BindView(R.id.rv_search)
    RecyclerView rv;

    final int PLACE = 0;
    final int PEOPLE = 1;
    final int CONTENT = 2;
    int option = 0;
    Context context = this;
    long startMillis, endMillis;
    Realm realm;
    private RecyclerView.LayoutManager layoutManager;
    private DayAdapter adapter;
    private ArrayList<BaseDTO> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new DayAdapter(this, realm);
        adapter.setHasStableIds(true);


        adapter.updateItem(items);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        Calendar month = DateHelper.getInstance().getMonth(System.currentTimeMillis());
        startMillis = month.getTimeInMillis();
        month.add(Calendar.MONTH, 1);
        endMillis = month.getTimeInMillis();
        tvStart.setText(DateHelper.getInstance().toDateString("yyyy/MM/dd", startMillis));
        tvEnd.setText(DateHelper.getInstance().toDateString("yyyy/MM/dd", endMillis));

        rangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        (DatePickerDialog.OnDateSetListener) context,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                option = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                items.clear();
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();

                switch (option) {
                    case PLACE:
                        queryPlace(query);
                        break;
                    case PEOPLE:
                        queryPeople(query);
                        break;
                    case CONTENT:
                        queryContent(query);
                        break;
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    /**
     * call custom photo smstrade gosgroup notifyunit
     **/
    private void queryContent(final String query) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmObject> cData = RealmHelper.getInstance().DataCommentQuery(CallData.class, query, "date", startMillis, endMillis);
                RealmResults<RealmObject> customData = RealmHelper.getInstance().DataCommentQuery(CustomData.class, query, "date", startMillis, endMillis);
                RealmResults<RealmObject> photoData = RealmHelper.getInstance().DataCommentQuery(PhotoData.class, query, "date", startMillis, endMillis);
                RealmResults<RealmObject> smsTradeData = RealmHelper.getInstance().DataCommentQuery(SmsTradeData.class, query, "date", startMillis, endMillis);

                RealmResults<RealmObject> ggData = RealmHelper.getInstance().DataCommentQuery(GpsGroupData.class, query, "end", startMillis, endMillis);
                RealmResults<RealmObject> pgData = RealmHelper.getInstance().DataCommentQuery(PhotoGroupData.class, query, "start", startMillis, endMillis - 1);
                RealmResults<RealmObject> nuData = RealmHelper.getInstance().DataCommentQuery(NotifyUnitData.class, query, "start", startMillis, endMillis - 1);
                RealmResults<RealmObject> suData = RealmHelper.getInstance().DataCommentQuery(SmsUnitData.class, query, "start", startMillis, endMillis - 1);

                RealmResults<RealmObject> nData = RealmHelper.getInstance().DataContentQuery(NotifyData.class, query, "date", startMillis, endMillis - 1);
                RealmResults<RealmObject> sData = RealmHelper.getInstance().DataContentQuery(SmsData.class, query, "date", startMillis, endMillis - 1);


                for (RealmObject cd : nData) {
                    items.add(new NotifyDTO((NotifyData) cd));
                }
                for (RealmObject cd : sData) {
                    items.add(new SmsDTO((SmsData) cd));
                }


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
                for (RealmObject std : smsTradeData) {
                    items.add(new SmsTradeDTO((SmsTradeData) std));
                }
                for (RealmObject std : photoData) {
                    items.add(new PhotoDTO((PhotoData) std));
                }
                for (RealmObject std : nuData) {
                    items.add(new NotifyUnitDTO((NotifyUnitData) std));
                }
                for (RealmObject std : suData) {
                    items.add(new SmsUnitDTO((SmsUnitData) std));
                }
                Collections.sort(items, new ItemComparator());

                adapter.updateItem(items);

                adapter.notifyDataSetChanged();

            }
        });

    }

    /*
    * call custom photogroup gpsgroup smstrade
    *
    * */
    public void queryPeople(final String query) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                RealmResults<NotifyUnitData> nuData = realm.where(NotifyUnitData.class)
                        .contains("name", query)
                        .between("end", startMillis, endMillis)
                        .findAll();
                RealmResults<SmsUnitData> suData = realm.where(SmsUnitData.class)
                        .contains("name", query)
                        .between("end", startMillis, endMillis)
                        .findAll();
//                RealmResults<SmsUnitData> suData2 = realm.where(SmsUnitData.class)
//                        .contains("name", query)
//                        .findAll();
                RealmResults<CallData> cData = realm.where(CallData.class)
                        .contains("person", query)
                        .between("date", startMillis, endMillis)
                        .findAll();

                for (NotifyUnitData g : nuData) {
                    NotifyUnitDTO data = new NotifyUnitDTO(g);
                    items.add(data);
                }
                for (SmsUnitData g : suData) {
                    SmsUnitDTO data = new SmsUnitDTO(g);
                    items.add(data);
                }
                for (CallData g : cData) {
                    CallDTO data = new CallDTO(g);
                    items.add(data);
                }


                RealmResults<GpsGroupData> gpsGroupData = realm.where(GpsGroupData.class)
                        .notEqualTo("friends", "[]")
                        .between("start", startMillis, endMillis)
                        .findAll();
                for (GpsGroupData ggd : gpsGroupData) {
                    GpsGroupDTO data = new GpsGroupDTO(ggd);
                    ArrayList<UserDTO> users = JsonUtil.getInstance().friendJsonTOArray(data);
                    for(UserDTO user: users){
                        if(user.getName().contains(query)){
                            items.add(data);
                            break;
                        }
                    }
                }


                RealmResults<CallData> callDatas= realm.where(CallData.class)
                        .notEqualTo("friends", "[]")
                        .between("date", startMillis, endMillis)
                        .findAll();
                for (CallData cd : callDatas) {
                    CallDTO data = new CallDTO(cd);
                    ArrayList<UserDTO> users = JsonUtil.getInstance().friendJsonTOArray(data);
                    for(UserDTO user: users){
                        if(user.getName().contains(query)){
                            items.add(data);
                            break;
                        }
                    }
                }


                RealmResults<CustomData> customDatas= realm.where(CustomData.class)
                        .notEqualTo("friends", "[]")
                        .between("date", startMillis, endMillis)
                        .findAll();
                for (CustomData cd : customDatas) {
                    CustomDTO data = new CustomDTO(cd);
                    ArrayList<UserDTO> users = JsonUtil.getInstance().friendJsonTOArray(data);
                    for(UserDTO user: users){
                        if(user.getName().contains(query)){
                            items.add(data);
                            break;
                        }
                    }
                }



                RealmResults<PhotoGroupData> photoGroupDatas= realm.where(PhotoGroupData.class)
                        .notEqualTo("friends", "[]")
                        .between("start", startMillis, endMillis)
                        .findAll();
                for (PhotoGroupData cd : photoGroupDatas) {
                    PhotoGroupDTO data = new PhotoGroupDTO(cd);
                    ArrayList<UserDTO> users = JsonUtil.getInstance().friendJsonTOArray(data);
                    for(UserDTO user: users){
                        if(user.getName().contains(query)){
                            items.add(data);
                            break;
                        }
                    }
                }

                RealmResults<SmsTradeData> smsTradeDatas= realm.where(SmsTradeData.class)
                        .notEqualTo("friends", "[]")
                        .between("date", startMillis, endMillis)
                        .findAll();
                for (SmsTradeData cd : smsTradeDatas) {
                    SmsTradeDTO data = new SmsTradeDTO(cd);
                    ArrayList<UserDTO> users = JsonUtil.getInstance().friendJsonTOArray(data);
                    for(UserDTO user: users){
                        if(user.getName().contains(query)){
                            items.add(data);
                            break;
                        }
                    }
                }


                Collections.sort(items, new ItemComparator());

                adapter.updateItem(items);

                adapter.notifyDataSetChanged();


            }
        });
    }

    public void queryPlace(final String query) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<GpsData> results = realm.where(GpsData.class)
                        .contains("place", query)
                        .between("date", startMillis, endMillis)
                        .findAll();
                for (GpsData g : results) {
                    Log.d("QueryGps", g.getPlace());
                    GpsDTO gpsDTO = new GpsDTO(g);
                    items.add(gpsDTO);
                }
                RealmResults<SmsTradeData> smsTradeResults = realm.where(SmsTradeData.class)
                        .contains("place", query)
                        .between("date", startMillis, endMillis)
                        .findAll();
                for (SmsTradeData g : smsTradeResults) {
                    Log.d("QuerySms", g.getPlace());
                    SmsTradeDTO smsTradeDTO = new SmsTradeDTO(g);
                    items.add(smsTradeDTO);
                }

                RealmResults<PhotoData> photoDatas = realm.where(PhotoData.class)
                        .contains("place", query)
                        .between("date", startMillis, endMillis)
                        .findAll();
                for (PhotoData g : photoDatas) {
                    Log.d("QueryPhoto", g.getPlace());
                    PhotoDTO photoDTO = new PhotoDTO(g);
                    items.add(photoDTO);
                }

                Collections.sort(items, new ItemComparator());


                adapter.updateItem(items);

                adapter.notifyDataSetChanged();


            }
        });

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, monthOfYear);
        start.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, yearEnd);
        end.set(Calendar.MONTH, monthOfYearEnd);
        end.set(Calendar.DAY_OF_MONTH, dayOfMonthEnd);

        startMillis = start.getTimeInMillis();
        endMillis = end.getTimeInMillis();

        tvStart.setText(DateHelper.getInstance().toDateString("yyyy/MM/dd", startMillis));
        tvEnd.setText(DateHelper.getInstance().toDateString("yyyy/MM/dd", endMillis));


    }

    @Override
    public void onNotifyItemClick(BaseDTO item) {
        Intent intent = new Intent(this, UnitActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().NOTIFY_GROUP_DATA);
        intent.putExtra("item", Parcels.wrap((NotifyGroupDTO) item));
        startActivity(intent);
    }

    @Override
    public void onSmsGroupItemClick(BaseDTO item) {
        Intent intent = new Intent(this, UnitActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().SMS_GROUP_DATA);
        intent.putExtra("item", Parcels.wrap((SmsGroupDTO) item));
        startActivity(intent);
    }


    @Override
    public void onSmsTradeItemClick(BaseDTO item) {
        Intent intent = new Intent(this, SmsTradeActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().SMS_TRADE_DATA);
        intent.putExtra("item", Parcels.wrap((SmsTradeDTO) item));
        startActivity(intent);
    }

    @Override
    public void onPhotoGroupItemClick(BaseDTO item) {
        Intent intent = new Intent(this, PhotoActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().PHOTO_GROUP_DATA);
        intent.putExtra("item", Parcels.wrap((PhotoGroupDTO) item));
        startActivity(intent);
    }


    @Override
    public void onGpsItemClick(BaseDTO item) {
        Intent intent = new Intent(this, GpsStillActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().GPS_DATA);
        intent.putExtra("item", Parcels.wrap((GpsDTO) item));
        startActivity(intent);
    }

    @Override
    public void onCallItemClick(BaseDTO item) {
        Intent intent = new Intent(this, CallActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().CALL_DATA);
        intent.putExtra("item", Parcels.wrap((CallDTO) item));
        startActivity(intent);
    }

    @Override
    public void onCustomItemClick(BaseDTO item) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra("item", Parcels.wrap((CustomDTO) item));
        startActivity(intent);
    }

    @Override
    public void onGpsGroupItemClick(BaseDTO item) {
        Intent intent = new Intent(this, GpsGroupActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().GPS_GROUP_DATA);
        intent.putExtra("item", Parcels.wrap((GpsGroupDTO) item));

        startActivity(intent);

    }

    @Override
    public void onPhotoItemClick(BaseDTO item) {
        Intent intent = new Intent(this, DetailPhotoActivity.class);
        intent.putExtra("item", Parcels.wrap(item));
        startActivity(intent);
    }
}
