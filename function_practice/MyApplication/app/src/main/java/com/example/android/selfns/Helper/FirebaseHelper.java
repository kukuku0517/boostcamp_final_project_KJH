package com.example.android.selfns.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.android.selfns.Data.DTO.Detail.CallDTO;
import com.example.android.selfns.Data.DTO.Detail.CustomDTO;
import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.DTO.Detail.SmsTradeDTO;
import com.example.android.selfns.Data.DTO.Group.PhotoGroupDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.GroupData.PhotoGroupData;
import com.example.android.selfns.Data.RealmData.UnitData.CallData;
import com.example.android.selfns.Data.RealmData.UnitData.CustomData;
import com.example.android.selfns.Data.RealmData.UnitData.PhotoData;
import com.example.android.selfns.Data.RealmData.UnitData.SmsTradeData;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import io.realm.Realm;

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

    FirebaseStorage storage;

    public final String USERS = "users";
    public final String USER_DTO = "userDTO";
    public final String POSTS = "posts";
    public final String MESSAGE = "message";

    public final String FRIENDS = "friends";

    public final String CLASS = "class";

    public final String UID = "uid";
    public final String ITEM = "item";

    public final String COUNT = "count";
    public final String TIMESTAMP = "timestamp";


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
        if (currentUser.getPhotoUrl() != null) {
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
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("userId", getCurrentUser().getEmail());
        editor.apply();
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
        if (u.getPhotoUrl() != null) {
            user.setPhotoUrl(u.getPhotoUrl().toString());
        }
        if (u.getDisplayName() != null) {
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

    public DatabaseReference getPostRef(String postId) {
        DatabaseReference myRef = getPostsRef().child(postId);
        return myRef;
    }

    public void getUserPosts() {

    }

    public String setPost(int type, BaseDTO item) {
        DatabaseReference myRef = getPostsRef().push();
        String key = myRef.getKey();
        long currentTime = System.currentTimeMillis();
        myRef.child(CLASS).setValue(type);
        myRef.child(UID).setValue(getCurrentUserId());
        myRef.child(ITEM).setValue(item);
        myRef.child(TIMESTAMP).setValue(currentTime);
        SharedPreferences mPref = context.getSharedPreferences("setting", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putLong("lastTimestamp", currentTime);
        editor.commit();
        DatabaseReference userRef = getCurrentUserRef().child(POSTS).push();
//        userRef.runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//                if(mutableData.child(COUNT)==null){
//                    mutableData.child(COUNT).setValue(1);
//                }else{
//                    int count = (int) mutableData.child(COUNT).getValue();
//                    mutableData.child(COUNT).setValue(count+1);
//                }
//
//                return null;
//            }
//
//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//
//            }
//        });

        String pushKey = userRef.getKey();
        userRef.setValue(key);
        return pushKey;
    }


    public void sendPostMessage(String friendId, String postKey) {
        DatabaseReference fRef = getUserRef(friendId);
        fRef.child(MESSAGE).push().setValue(postKey);
        fRef.child(POSTS).push().setValue(postKey);
    }

    public StorageReference getPhotosRef() {
        storage = FirebaseStorage.getInstance();

        return storage.getReference();
    }

    public StorageReference getPhotoRef(String photoId) {
        return getPhotosRef().child(photoId);
    }


    public String setPostPhotoGroup(int type, final PhotoGroupDTO item) throws FileNotFoundException {
        final DatabaseReference myRef = getPostsRef().push();
        String key = myRef.getKey();
        long currentTime = System.currentTimeMillis();

        myRef.child(CLASS).setValue(type);
        myRef.child(UID).setValue(getCurrentUserId());
        myRef.child(ITEM).setValue(item);
        myRef.child(TIMESTAMP).setValue(currentTime);
        SharedPreferences mPref = context.getSharedPreferences("setting", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putLong("lastTimestamp", currentTime);
        editor.commit();
        DatabaseReference userRef = getCurrentUserRef().child(POSTS).push();
        String pushKey = userRef.getKey();
        userRef.setValue(key);

        final ArrayList<PhotoDTO> photos = item.getPhotoss();
        final int[] count = {0};

        Toast toast = null;
        for (int i = 0; i < item.getPhotoss().size(); i++) {
            InputStream stream = new FileInputStream(new File(photos.get(i).getPath()));
            UploadTask uploadTask = getPhotoRef(key).child(String.valueOf(i)).putStream(stream);
            final int finalI = i;
            final Toast finalToast = toast;
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    count[0]++;
                    if (finalToast != null) {
                        finalToast.cancel();
                    }
                    finalToast.makeText(context, String.format("%d/%d 개 업로드 성공", count[0], photos.size()), Toast.LENGTH_SHORT).show();
                    photos.get(finalI).setPath(downloadUrl.toString());
                    if (count[0] == photos.size()) {
                        item.setPhotoss(photos);
                        myRef.child(ITEM).setValue(item);
                    }
                }
            });

        }


        return pushKey;
    }

    public void updateDataFromFirebase() {
        final Realm realm = Realm.getDefaultInstance();

        Query myRef = getCurrentUserRef().child(POSTS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    //post id로 posts에서 글 찾아오기
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final long timestamp = mPref.getLong("lastTimestamp", 0);
                        Log.d("fbloop", dataSnapshot.toString());
//                        Query postsQuery = getPostsRef().orderByChild(TIMESTAMP).startAt(timestamp+1);

                        DatabaseReference userPostQuery = getPostsRef().getRef().child(snapshot.getValue().toString());

                        userPostQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                SharedPreferences mPref = context.getSharedPreferences("setting", Activity.MODE_PRIVATE);
                                final long time = Long.parseLong(dataSnapshot.child(TIMESTAMP).getValue().toString());

                                if (dataSnapshot.hasChildren()) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            SharedPreferences mPref = context.getSharedPreferences("setting", Activity.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = mPref.edit();
                                            editor.putLong("lastTimestamp", time);
                                            editor.commit();

                                            int type = Integer.parseInt(dataSnapshot.child(CLASS).getValue().toString());
                                            String uid = dataSnapshot.child(UID).getValue().toString();
                                            String key = dataSnapshot.getKey();
                                            BaseDTO item;
                                            switch (type) {
                                                case RealmClassHelper.CALL_DATA:
                                                    item = dataSnapshot.child("item").getValue(CallDTO.class);
                                                    CallData callData = new CallData((CallDTO) item);

                                                    if (uid == getCurrentUserId()) {
                                                        realm.copyToRealm(callData);
                                                    } else {
                                                        CallData callData1 = realm.where(CallData.class).equalTo("fid", key).findFirst();
                                                        if (callData1 != null) {
                                                            callData1 = new CallData((CallDTO) item);
                                                            realm.insertOrUpdate(callData1);
                                                        } else {
                                                            long id = RealmHelper.getInstance().nextId(CallData.class, realm);
                                                            callData.setId(id);
                                                            realm.copyToRealm(callData);
                                                        }
                                                    }

                                                    break;
                                                case RealmClassHelper.CUSTOM_DATA:
                                                    item = dataSnapshot.child("item").getValue(CustomDTO.class);
                                                    CustomData customData = new CustomData((CustomDTO) item);

                                                    if (uid == getCurrentUserId()) {
                                                        realm.copyToRealm(customData);
                                                    } else {
                                                        CustomData customData1 = realm.where(CustomData.class).equalTo("fid", key).findFirst();
                                                        if (customData1 != null) {
                                                            customData1 = new CustomData((CustomDTO) item);
                                                            realm.insertOrUpdate(customData1);
                                                        } else {
                                                            long id = RealmHelper.getInstance().nextId(CallData.class, realm);
                                                            customData.setId(id);
                                                            realm.copyToRealm(customData);
                                                        }
                                                    }
                                                    break;
                                                case RealmClassHelper.PHOTO_DATA:
                                                    item = dataSnapshot.child("item").getValue(PhotoDTO.class);
                                                    PhotoData photoData = new PhotoData((PhotoDTO) item);

                                                    if (uid == getCurrentUserId()) {
                                                        realm.copyToRealm(photoData);
                                                    } else {
                                                        PhotoData photoData1 = realm.where(PhotoData.class).equalTo("fid", key).findFirst();
                                                        if (photoData1 != null) {
                                                            photoData1 = new PhotoData((PhotoDTO) item);
                                                            realm.insertOrUpdate(photoData1);
                                                        } else {
                                                            long id = RealmHelper.getInstance().nextId(CallData.class, realm);
                                                            photoData.setId(id);
                                                            realm.copyToRealm(photoData);
                                                        }
                                                    }

                                                    break;
                                                case RealmClassHelper.PHOTO_GROUP_DATA:
                                                    item = dataSnapshot.child("item").getValue(PhotoGroupDTO.class);

                                                    PhotoGroupData photoGroupData = new PhotoGroupData((PhotoGroupDTO) item);

                                                    if (uid.equals(getCurrentUserId())) {
                                                        realm.insertOrUpdate(photoGroupData);
                                                        Log.d("fbloop", "asdfa");
                                                    } else {
                                                        PhotoGroupData photoGroupData1 = realm.where(PhotoGroupData.class).equalTo("fid", key).findFirst();
                                                        if (photoGroupData1 != null) {
                                                            long id = photoGroupData1.getId();
                                                            ((PhotoGroupDTO) item).setId(id);
                                                            photoGroupData1 = new PhotoGroupData((PhotoGroupDTO) item);
                                                            realm.insertOrUpdate(photoGroupData1);
                                                        } else {
                                                            long id = RealmHelper.getInstance().nextId(PhotoGroupData.class, realm);
                                                            photoGroupData.setId(id);
                                                            realm.copyToRealm(photoGroupData);
                                                        }
                                                    }
                                                    break;
                                                case RealmClassHelper.SMS_TRADE_DATA:
                                                    item = dataSnapshot.child("item").getValue(SmsTradeDTO.class);
                                                    SmsTradeData smsTradeData = new SmsTradeData((SmsTradeDTO) item);
                                                    if (uid == getCurrentUserId()) {
                                                        realm.copyToRealm(smsTradeData);
                                                    } else {
                                                        SmsTradeData smsTradeData1 = realm.where(SmsTradeData.class).equalTo("fid", key).findFirst();
                                                        if (smsTradeData1 != null) {
                                                            smsTradeData1 = new SmsTradeData((SmsTradeDTO) item);
                                                            realm.insertOrUpdate(smsTradeData1);
                                                        } else {
                                                            long id = RealmHelper.getInstance().nextId(PhotoGroupData.class, realm);
                                                            smsTradeData.setId(id);
                                                            realm.copyToRealm(smsTradeData);
                                                        }
                                                    }
                                                    break;

                                            }

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}