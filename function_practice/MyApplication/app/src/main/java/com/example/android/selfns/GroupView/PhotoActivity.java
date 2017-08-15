package com.example.android.selfns.GroupView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.android.selfns.DailyView.Adapter.DayAdapter;
import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.DTO.Group.PhotoGroupDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.DetailView.DetailPhotoActivity;
import com.example.android.selfns.Interface.PhotoItemClickListener;
import com.example.android.selfns.R;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class PhotoActivity extends AppCompatActivity implements PhotoItemClickListener {

    private RecyclerView.LayoutManager layoutManager;
    private DayAdapter adapter;
    private ArrayList<BaseDTO> items = new ArrayList<>();
   @BindView(R.id.photo_rv)
   RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);


            Realm realm = Realm.getDefaultInstance();

            layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            adapter = new DayAdapter(this,realm);
            PhotoGroupDTO pgData = Parcels.unwrap(getIntent().getParcelableExtra("item"));
            items.clear();
            for(PhotoDTO p:pgData.getPhotoss()){
                items.add(p);
            }
            adapter.updateItem(items);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(adapter);

    }

    @Override
    public void onPhotoItemClick(BaseDTO item) {
        Intent intent = new Intent(this,DetailPhotoActivity.class);
//        intent.putExtra("id", item.getId());
//        intent.putExtra("type", RealmClassHelper.getInstance().PHOTO_DATA);

        intent.putExtra("item",Parcels.wrap(item));
        startActivity(intent);
    }
}
