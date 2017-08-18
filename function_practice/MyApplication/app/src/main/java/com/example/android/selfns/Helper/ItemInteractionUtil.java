package com.example.android.selfns.Helper;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.selfns.Data.DTO.Detail.CallDTO;
import com.example.android.selfns.Data.DTO.Detail.CustomDTO;
import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.DTO.Detail.SmsTradeDTO;
import com.example.android.selfns.Data.DTO.Group.GpsGroupDTO;
import com.example.android.selfns.Data.DTO.Group.PhotoGroupDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.CommentableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.GpsableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Data.RealmData.GroupData.GpsGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.PhotoGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.SmsUnitData;
import com.example.android.selfns.Data.RealmData.UnitData.CallData;
import com.example.android.selfns.Data.RealmData.UnitData.CustomData;
import com.example.android.selfns.Data.RealmData.UnitData.GpsData;
import com.example.android.selfns.Data.RealmData.UnitData.PhotoData;
import com.example.android.selfns.Data.RealmData.UnitData.SmsTradeData;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmCommentableObject;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmGpsObject;
import com.example.android.selfns.ExtraView.Comment.CommentDialogFragment;
import com.example.android.selfns.ExtraView.Friend.FriendDialogFragment;
import com.example.android.selfns.LoginView.UserDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.FileNotFoundException;

import io.realm.Realm;
import io.realm.RealmObject;

import static android.R.attr.id;


/**
 * Created by samsung on 2017-08-07.
 */

public class ItemInteractionUtil {
    private static ItemInteractionUtil instance;
    private static Context context;

    public static ItemInteractionUtil getInstance(Context c) {
        if (instance == null) {
            instance = new ItemInteractionUtil();
        }
        context = c;
        return instance;
    }

    //댓글
    public void show(AppCompatActivity activity, long id, int type) {

        FragmentManager fm = activity.getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putInt("type", type);
        Log.d("dialog", SmsUnitData.class.getCanonicalName());
        CommentDialogFragment dialogFragment = new CommentDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(fm, "fragment_dialog_test");
    }

    //하이라이트
    public void highlight(final CommentableDTO item) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Class c = RealmClassHelper.getInstance().getClass(item.getType());
                MyRealmCommentableObject myItem = (MyRealmCommentableObject) realm.where(c).equalTo("id", item.getId()).findFirst();

                Log.d("highlight", String.valueOf(item.isHighlight()));
                myItem.setHighlight(!item.isHighlight());
                Log.d("highlight", String.valueOf(item.isHighlight()));
            }
        });
    }

    //아이템삭제 (일반)
    public void deleteItem(final BaseDTO item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Class c = RealmClassHelper.getInstance().getClass(item.getType());

                        RealmObject myItem = (RealmObject) realm.where(c).equalTo("id", item.getId()).findFirst();
                        myItem.deleteFromRealm();
                    }
                });
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();

    }

    //아이템삭제 (photo)
    public void deletePhotoItem(final PhotoDTO item) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
// Add the buttons
        builder.setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Realm realm = Realm.getDefaultInstance();

                Class c = RealmClassHelper.getInstance().getClass(item.getType());
                RealmObject myItem = (RealmObject) realm.where(c).equalTo("id", item.getId()).findFirst();
                RealmHelper.getInstance().photodataDelete(myItem);

            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();

    }

    //아이템 삭제 (photogroup)
    public void deletePhotoGroupItem(final PhotoGroupDTO item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
// Add the buttons
        builder.setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Class c = RealmClassHelper.getInstance().getClass(item.getType());
                        RealmObject myItem = (RealmObject) realm.where(c).equalTo("id", item.getId()).findFirst();
                        RealmHelper.getInstance().photoGroupDataDelete(myItem);
                    }
                });

            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();


    }

    //시간 수정
    public void setDate(final BaseDTO item, final long date) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Class c = RealmClassHelper.getInstance().getClass(item.getType());
                switch (item.getType()) {
                    case RealmClassHelper.CALL_DATA:
                        CallData callData = (CallData) realm.where(c).equalTo("id", item.getId()).findFirst();
                        callData.setDate(date);
                        break;
                    case RealmClassHelper.CUSTOM_DATA:
                        CustomData customData = (CustomData) realm.where(c).equalTo("id", item.getId()).findFirst();
                        customData.setDate(date);
                        break;
                    case RealmClassHelper.SMS_TRADE_DATA:
                        SmsTradeData smsTradeData = (SmsTradeData) realm.where(c).equalTo("id", item.getId()).findFirst();
                        smsTradeData.setDate(date);
                        break;
                    case RealmClassHelper.PHOTO_DATA:
                        PhotoData photoData = (PhotoData) realm.where(c).equalTo("id", item.getId()).findFirst();
                        photoData.setDate(date);
                        break;
                    case RealmClassHelper.PHOTO_GROUP_DATA:
                        PhotoGroupData photoGroupData = (PhotoGroupData) realm.where(c).equalTo("id", item.getId()).findFirst();
                        photoGroupData.setStart(date);
                        break;
                }
            }
        });
    }

    //장소 수정
    public void setPlace(final GpsableDTO place) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Class c = RealmClassHelper.getInstance().getClass(place.getType());
                long id = place.getId();
                switch (place.getType()) {
                    case RealmClassHelper.CUSTOM_DATA:
                        CustomData customData = (CustomData) realm.where(c).equalTo("id", id).findFirst();
                        customData.setLat(place.getLat());
                        customData.setLng(place.getLng());
                        customData.setPlace(place.getPlace());
                        customData.setOriginId(place.getOriginId());
                        break;
                    case RealmClassHelper.SMS_TRADE_DATA:
                        SmsTradeData smsTradeData = (SmsTradeData) realm.where(c).equalTo("id", id).findFirst();
                        smsTradeData.setLat(place.getLat());
                        smsTradeData.setLng(place.getLng());
                        smsTradeData.setPlace(place.getPlace());
                        smsTradeData.setOriginId(place.getOriginId());
                        break;
                    case RealmClassHelper.PHOTO_DATA:
                        PhotoData photoData = (PhotoData) realm.where(c).equalTo("id", id).findFirst();
                        photoData.setLat(place.getLat());
                        photoData.setLng(place.getLng());
                        photoData.setPlace(place.getPlace());
                        photoData.setOriginId(place.getOriginId());
                        break;
                    case RealmClassHelper.GPS_DATA:
                        GpsData gpsData = (GpsData) realm.where(c).equalTo("id", id).findFirst();
                        gpsData.setLat(place.getLat());
                        gpsData.setLng(place.getLng());
                        gpsData.setPlace(place.getPlace());
                        gpsData.setOriginId(place.getOriginId());
                        break;
                }


            }
        });
    }

    //아이템 공유
    public void shareItem(final ShareableDTO item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("공유하기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int type = item.getType();
                item.setShare(true);

                String key = null;
                if (type == RealmClassHelper.PHOTO_GROUP_DATA) {
                    try {
                        key = FirebaseHelper.getInstance(context).setPostPhotoGroup(type, (PhotoGroupDTO) item);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    key = FirebaseHelper.getInstance(context).setPost(type, item);
                }
                JSONArray friends = null;
                try {
                    friends = new JSONArray(item.getFriends());
                    for (int i = 0; i < friends.length(); i++) {
                        JSONObject friend = friends.getJSONObject(i);
                        String uid = friend.get("id").toString();
                        FirebaseHelper.getInstance(context).sendPostMessage(uid, key);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();

    }

    public void tagFriend(AppCompatActivity activity, BaseDTO item) {
        FragmentManager fm = activity.getSupportFragmentManager();
        Bundle args = new Bundle();

        switch (item.getType()) {

            case RealmClassHelper.CALL_DATA:
                args.putParcelable("item", Parcels.wrap((CallDTO) item));
                break;
            case RealmClassHelper.CUSTOM_DATA:
                args.putParcelable("item", Parcels.wrap((CustomDTO) item));
                break;
            case RealmClassHelper.PHOTO_GROUP_DATA:
                args.putParcelable("item", Parcels.wrap((PhotoGroupDTO) item));
                break;
            case RealmClassHelper.PHOTO_DATA:
                args.putParcelable("item", Parcels.wrap((PhotoDTO) item));
                break;
            case RealmClassHelper.SMS_TRADE_DATA:
                args.putParcelable("item", Parcels.wrap((SmsTradeDTO) item));
                break;
            case RealmClassHelper.GPS_GROUP_DATA:
                args.putParcelable("item", Parcels.wrap((GpsGroupDTO) item));
                break;

        }

        FriendDialogFragment dialogFragment = new FriendDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(fm, "fragment_dialog_test");
    }

    public void addFriend(final ShareableDTO item, UserDTO user) throws JSONException {
        final String uid = user.getUid();

        JSONArray jsonArray = new JSONArray(item.getFriends());
        JSONObject json = new JSONObject();
        json.put("id", uid);
        jsonArray.put(json);
        final String friendsString = jsonArray.toString();
        item.setFriends(friendsString);


        if (!item.isShare()) {
            final int type = item.getType();
            final Class c = RealmClassHelper.getInstance().getClass(type);

            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    switch (type) {
                        case RealmClassHelper.CALL_DATA:
                            CallData callData = (CallData) realm.where(c).equalTo("id", item.getId()).findFirst();
                            callData.setFriends(friendsString);
                            break;
                        case RealmClassHelper.CUSTOM_DATA:
                            CustomData customData = (CustomData) realm.where(c).equalTo("id", item.getId()).findFirst();
                            customData.setFriends(friendsString);
                            break;
                        case RealmClassHelper.PHOTO_GROUP_DATA:
                            PhotoGroupData photoGroupData = (PhotoGroupData) realm.where(c).equalTo("id", item.getId()).findFirst();
                            photoGroupData.setFriends(friendsString);
                            break;
                        case RealmClassHelper.PHOTO_DATA:
                            PhotoData photoData = (PhotoData) realm.where(c).equalTo("id", item.getId()).findFirst();
                            photoData.setFriends(friendsString);
                            break;
                        case RealmClassHelper.SMS_TRADE_DATA:
                            SmsTradeData smsTradeData = (SmsTradeData) realm.where(c).equalTo("id", item.getId()).findFirst();
                            smsTradeData.setFriends(friendsString);
                            break;
                        case RealmClassHelper.GPS_GROUP_DATA:
                            GpsGroupData gpsGroupData = (GpsGroupData) realm.where(c).equalTo("id", item.getId()).findFirst();
                            gpsGroupData.setFriends(friendsString);
                            break;
                    }
                }
            });


//            RealmHelper.getInstance().addFriend(item,uid);


        } else {

        }
    }

}
