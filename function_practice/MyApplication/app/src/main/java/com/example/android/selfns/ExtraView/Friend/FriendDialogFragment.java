package com.example.android.selfns.ExtraView.Friend;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.selfns.Data.DTO.Retrofit.FriendAddDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.Helper.FriendTagEvent;
import com.example.android.selfns.Helper.RetrofitHelper;
import com.example.android.selfns.Interface.DataReceiveListener;
import com.example.android.selfns.LoginView.FriendAdapter;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by samsung on 2017-08-15.
 */

public class FriendDialogFragment extends DialogFragment {

    RecyclerView rv;

    RecyclerView.LayoutManager layoutManager;
    FriendAdapter adapter;
    ArrayList<FriendDTO> items = new ArrayList<>();
    Context context;
    ShareableDTO item;
    private Realm realm;

    DataReceiveListener<ArrayList<FriendDTO>> listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        Bundle args = getArguments();
        if (args != null) {
            item = Parcels.unwrap(args.getParcelable("item"));
        }

        listener = new DataReceiveListener<ArrayList<FriendDTO>>() {
            @Override
            public void onReceive(ArrayList<FriendDTO> response) {
                items=response;
                adapter.updateItem(items);
                adapter.notifyDataSetChanged();
            }
        };
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.friend_fragment, container, false);

        FirebaseHelper instance = FirebaseHelper.getInstance(context);
        final String currentUid = instance.getCurrentUserId();

        RetrofitHelper.getInstance(context).getTagFriends(item.get_id(), FirebaseHelper.getInstance(context).getCurrentUserId(),listener);
//        DatabaseReference mRef = FirebaseHelper.getInstance(context).getFriendsRef(currentUid);
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot d : dataSnapshot.getChildren()) {
//
//
//                    Query query = FirebaseHelper.getInstance(context).getUsersRef().orderByKey().equalTo(d.getValue().toString());
//
//                    query.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                                FriendDTO user = dataSnapshot1.child("userDTO").getValue(FriendDTO.class);
//                                items.add(user);
//                                adapter.updateItem(items);
//                                adapter.notifyDataSetChanged();
//                            }
////
////
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        rv = (RecyclerView) view.findViewById(R.id.rv_friend);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        adapter = new FriendAdapter(context, 1, item);
        adapter.updateItem(items);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        return view;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FriendTagEvent event) {
        Log.d("eventbus","asdfa");
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
