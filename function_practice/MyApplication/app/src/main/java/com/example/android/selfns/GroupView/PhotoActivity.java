package com.example.android.selfns.GroupView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.android.selfns.DailyView.Adapter.DayAdapter;
import com.example.android.selfns.GroupView.Data.PhotoGroupData;
import com.example.android.selfns.Interface.MyRealmObject;
import com.example.android.selfns.DetailView.Data.PhotoData;
import com.example.android.selfns.DetailView.DetailPhotoActivity;
import com.example.android.selfns.Interface.PhotoItemClickListener;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

public class PhotoActivity extends AppCompatActivity implements PhotoItemClickListener {

    private RecyclerView.LayoutManager layoutManager;
    private DayAdapter adapter;
    private RealmList<MyRealmObject> items = new RealmList<>();
   @BindView(R.id.photo_rv)
   RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        Long id = getIntent().getLongExtra("id", -1);

        if (id != -1) {
            Realm realm = Realm.getDefaultInstance();

            layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            adapter = new DayAdapter(this,realm);
            PhotoGroupData pgData = realm.where(PhotoGroupData.class).equalTo("id",id).findFirst();

            items.clear();
            for(PhotoData p:pgData.getPhotoss()){
                items.add(p);
            }
            adapter.updateItem(items);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(adapter);
        }
    }

    @Override
    public void onPhotoItemClick(MyRealmObject item) {
        Intent intent = new Intent(this,DetailPhotoActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("type", RealmClassHelper.getInstance().PHOTO_DATA);
        startActivity(intent);
    }
}
