package com.example.android.selfns.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.LoginView.UserDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

/**
 * Created by samsung on 2017-08-14.
 */

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;
    private Context context;
    private SharedPreferences mPref;

    public final String USERS = "users";
    public final String USER_DTO = "userDTO";
    public final String POSTS = "posts";
    public final String POST = "post";

    public final String FRIENDS = "friends";

    public final String CLASS = "class";

    public final String UID = "uid";
    public final String ITEM = "item";


    public FirebaseHelper(Context context) {
        this.context = context;
        mPref = context.getSharedPreferences("setting", Activity.MODE_PRIVATE);

    }

    public static FirebaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseHelper(context);
        }
        return instance;
    }

//    database = FirebaseDatabase.getInstance();
//    myRef = database.getReference("message");
//    currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public void setCurrentUser(FirebaseUser currentUser) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("userId", currentUser.getUid());
        if(currentUser.getPhotoUrl()!=null){
            editor.putString("userPhoto", currentUser.getPhotoUrl().toString());
        }
        editor.putString("userName", currentUser.getDisplayName());
        editor.commit();
    }

    public FirebaseUser getCurrentUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser;
    }

    public String getCurrentUserId() {

        if (!mPref.contains("userId")) {
            mPref.edit().putString("userId", getCurrentUser().getUid().toString());
            mPref.edit().commit();
        }

        return mPref.getString("userId", "");
    }


    public String getCurrentUserPhoto() {

        if (!mPref.contains("userPhoto")) {

            mPref.edit().putString("userPhoto", getCurrentUser().getPhotoUrl().toString());
            mPref.edit().commit();
        }

        return mPref.getString("userPhoto", "");
    }

    public String getCurrentUserName() {

        if (!mPref.contains("userName")) {

            mPref.edit().putString("userName", getCurrentUser().getDisplayName());
            mPref.edit().commit();
        }

        return mPref.getString("userName", "");
    }


    public DatabaseReference getUsersRef() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(USERS);
        return myRef;
    }

    public DatabaseReference getUserRef(String id) {
        DatabaseReference myRef = getUsersRef().child(id);
        return myRef;
    }


    public DatabaseReference getCurrentUserRef() {
        DatabaseReference myRef = getUsersRef().child(getCurrentUserId());
        return myRef;
    }

    public DatabaseReference getUserData(String id) {
        DatabaseReference myRef = getUserRef(id).child(USER_DTO);
        return myRef;
    }

    public void setUserData() {
        FirebaseUser u = getCurrentUser();
        UserDTO user = new UserDTO();
        user.setId(u.getEmail());
        user.setUid(u.getUid());
        if(u.getPhotoUrl()!=null){
            user.setPhotoUrl(u.getPhotoUrl().toString());
        }
        if(u.getDisplayName()!=null){
            user.setName(u.getDisplayName());
        }

        getUsersRef().child(user.getUid()).child(USER_DTO).setValue(user);


    }

    public DatabaseReference getFriendsRef(String id) {
        DatabaseReference myRef = getUserRef(id).child(FRIENDS);
        return myRef;
    }

    public void setFriends(final String friendId) {

        String a = getCurrentUserId();
        String b = friendId;

        myRef = getFriendsRef(getCurrentUserId());
        myRef.push().setValue(friendId);

//        myRef.runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//                boolean isFriend = false;
//                for(MutableData m:mutableData.getChildren()){
//                    if(friendId.equals(m.getValue()))isFriend=true;
//                }
//
//                if(!isFriend){
//                    myRef.push().setValue(friendId);
//                }
//
//
//                return null;
//            }
//
//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//
//            }
//        });

    }

    public DatabaseReference getPostsRef() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(POSTS);
        return myRef;
    }

    public DatabaseReference getUserPostsRef() {

        return myRef;
    }

    public DatabaseReference getPostRef() {
        DatabaseReference myRef = getPostsRef();
        return myRef;
    }

    public void getUserPosts() {

    }

    public void setPost(int type, BaseDTO item) {
        DatabaseReference myRef = getPostsRef().push();
        String key = myRef.getKey();
        myRef.child(CLASS).setValue(type);
        myRef.child(UID).setValue(getCurrentUserId());
        myRef.child(ITEM).setValue(item);

        DatabaseReference userRef = getCurrentUserRef();
        userRef.child(POST).push().setValue(key);
    }


}
