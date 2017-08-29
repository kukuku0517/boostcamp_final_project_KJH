package com.example.android.selfns.LoginView;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.Helper.FriendAddEvent;
import com.example.android.selfns.Helper.RetrofitHelper;
import com.example.android.selfns.Interface.DataReceiveListener;
import com.example.android.selfns.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendActivity extends AppCompatActivity {


    @BindView(R.id.rv_friend)
    RecyclerView rv;

    RecyclerView.LayoutManager layoutManager;
    FriendAdapter adapter;
    ArrayList<FriendDTO> items = new ArrayList<>();
    Context context = this;
    DataReceiveListener<ArrayList<FriendDTO>> listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);

//        FirebaseHelper instance = FirebaseHelper.getInstance(context);
//        final String currentUid = instance.getCurrentUserId();
//
//        DatabaseReference allUserRef = instance.getUsersRef();
//        allUserRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                items.clear();
//                for (final DataSnapshot d : dataSnapshot.getChildren()) {
//                    final String uid = d.getKey().toString();
//                    if (!uid.equals(currentUid)) {
//
//                        DatabaseReference mRef = FirebaseHelper.getInstance(context).getFriendsRef(currentUid);
//                        mRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                boolean isFriend = false;
//                                for (DataSnapshot m : dataSnapshot.getChildren()) {
//                                    if (uid.equals(m.getValue())) isFriend = true;
//                                }
//
//                                if (!isFriend) {
//                                    UserDTO user = d.child("userDTO").getValue(UserDTO.class);
//                                    items.add(user);
//
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//                    }
//
//                }
//                adapter.updateItem(items);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        DatabaseReference mRef = instance.getFriendsRef(currentUid);
//
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                items.clear();
//                for (DataSnapshot d : dataSnapshot.getChildren()) {
//                    String uid = d.getValue().toString();
//
//                    if (!uid.equals(currentUid)) {
//                        DatabaseReference userRef = FirebaseHelper.getInstance(context).getUserData(uid);
//                        userRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                UserDTO user = dataSnapshot.getValue(UserDTO.class);
//
//                                items.add(user);
//                                adapter.updateItem(items);
//                                adapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new FriendAdapter(this, 0);
        adapter.updateItem(items);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

       listener = new DataReceiveListener<ArrayList<FriendDTO>>() {
            @Override
            public void onReceive(ArrayList<FriendDTO> response) {
                items = response;
                adapter.updateItem(items);
                adapter.notifyDataSetChanged();
            }

        };

        RetrofitHelper.getInstance(context).getUsers(FirebaseHelper.getInstance(context).getCurrentUser().getEmail(), listener);
    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(FriendAddEvent event) {
//        RetrofitHelper.getInstance(context).getUsers(FirebaseHelper.getInstance(context).getCurrentUser().getEmail(), listener);
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }
}
