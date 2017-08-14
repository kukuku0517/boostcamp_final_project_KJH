package com.example.android.selfns.LoginView;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.selfns.DailyView.Adapter.CalendarPinAdapter;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.Helper.PinnedHeaderItemDecoration;
import com.example.android.selfns.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class FriendActivity extends AppCompatActivity {


    @BindView(R.id.rv_friend)
    RecyclerView rv;

    RecyclerView.LayoutManager layoutManager;
    FriendAdapter adapter;
    ArrayList<UserDTO> items = new ArrayList<>();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);

        FirebaseHelper instance = FirebaseHelper.getInstance(context);
        DatabaseReference mRef = instance.getFriendsRef(instance.getCurrentUserId());


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String uid = d.getValue().toString();
                    DatabaseReference userRef = FirebaseHelper.getInstance(context).getUserData(uid);
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserDTO user = dataSnapshot.getValue(UserDTO.class);
                            items.add(user);
                            Log.d("firebaserv", user.getUid());
                            adapter.updateItem(items);
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new FriendAdapter(this);

        adapter.updateItem(items);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);


    }
}
