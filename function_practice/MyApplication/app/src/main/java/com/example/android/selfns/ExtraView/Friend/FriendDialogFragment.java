package com.example.android.selfns.ExtraView.Friend;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Helper.FirebaseHelper;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmCommentableObject;
import com.example.android.selfns.LoginView.FriendAdapter;
import com.example.android.selfns.LoginView.UserDTO;
import com.example.android.selfns.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by samsung on 2017-08-15.
 */

public class FriendDialogFragment extends DialogFragment {

    RecyclerView rv;

    RecyclerView.LayoutManager layoutManager;
    FriendAdapter adapter;
    ArrayList<UserDTO> items = new ArrayList<>();
    Context context;
    ShareableDTO item;
    private Realm realm;

    public FriendDialogFragment() {
        this.context = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            item = Parcels.unwrap(args.getParcelable("item"));
        }
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.friend_fragment, container, false);

        FirebaseHelper instance = FirebaseHelper.getInstance(context);
        final String currentUid = instance.getCurrentUserId();


        DatabaseReference mRef = FirebaseHelper.getInstance(context).getFriendsRef(currentUid);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {


                    Query query = FirebaseHelper.getInstance(context).getUsersRef().orderByKey().equalTo(d.getValue().toString());

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                UserDTO user = dataSnapshot1.child("userDTO").getValue(UserDTO.class);
                                items.add(user);
                                adapter.updateItem(items);
                                adapter.notifyDataSetChanged();
                            }
//
//
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


        rv = (RecyclerView) view.findViewById(R.id.rv_friend);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        adapter = new FriendAdapter(context, 1, item);
        adapter.updateItem(items);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        return view;

    }
}
