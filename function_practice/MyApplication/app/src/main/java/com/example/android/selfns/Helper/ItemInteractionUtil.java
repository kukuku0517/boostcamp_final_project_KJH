package com.example.android.selfns.Helper;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
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
import com.example.android.selfns.ExtraView.Comment.CommentDialogFragment;
import com.example.android.selfns.ExtraView.Friend.FriendDialogFragment;
import com.example.android.selfns.LoginView.UserDTO;

import org.parceler.Parcels;

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

    //    public void show(AppCompatActivity activity, long id, Class c){
//        FragmentManager fm = activity.getSupportFragmentManager();
//        Bundle args = new Bundle();
//        args.putString("class", c.getCanonicalName());
//        args.putLong("id", id);
//        args.putParcelable();
//        Log.d("dialog", SmsUnitData.class.getCanonicalName());
//        CommentDialogFragment dialogFragment = new CommentDialogFragment();
//        dialogFragment.setArguments(args);
//        dialogFragment.show(fm, "fragment_dialog_test");
//    }
    public void show(AppCompatActivity activity, long id, int type) {

        FragmentManager fm = activity.getSupportFragmentManager();
        Bundle args = new Bundle();
//        args.putString("class", c.getCanonicalName());
//        args.putLong("id", id);
        args.putLong("id", id);
        args.putInt("type", type);
        Log.d("dialog", SmsUnitData.class.getCanonicalName());
        CommentDialogFragment dialogFragment = new CommentDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(fm, "fragment_dialog_test");
    }

    public void highlight(final CommentableDTO item) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d("highlight", "asdf");
                item.setHighlight(!item.isHighlight());
            }
        });
    }

    public void deleteItem(final BaseDTO item) {
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

    public void deletePhotoItem(final PhotoDTO item) {


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
                        RealmHelper.getInstance().photodataDelete(myItem);
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
                }
            }
        });
    }

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

    public void shareItem(final BaseDTO item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
// Add the buttons
        builder.setPositiveButton("공유하기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int type = item.getType();
                FirebaseHelper.getInstance(context).setPost(type, item);
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

    public void addFriend(final ShareableDTO item, UserDTO user) {
        final String uid =user.getUid();
        item.getFriend().add(uid);
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
                            callData.getFriend().add(uid);
                            break;
                        case RealmClassHelper.CUSTOM_DATA:
                            CustomData customData = (CustomData) realm.where(c).equalTo("id", item.getId()).findFirst();
                            customData.getFriend().add(uid);
                            break;
                        case RealmClassHelper.PHOTO_GROUP_DATA:
                            PhotoGroupData photoGroupData = (PhotoGroupData) realm.where(c).equalTo("id", item.getId()).findFirst();
                            photoGroupData.getFriend().add(uid);
                            break;
                        case RealmClassHelper.PHOTO_DATA:
                            PhotoData photoData = (PhotoData) realm.where(c).equalTo("id", item.getId()).findFirst();
                            photoData.getFriend().add(uid);
                            break;
                        case RealmClassHelper.SMS_TRADE_DATA:
                            SmsTradeData smsTradeData = (SmsTradeData) realm.where(c).equalTo("id", item.getId()).findFirst();
                            smsTradeData.getFriend().add(uid);
                            break;
                        case RealmClassHelper.GPS_GROUP_DATA:
                            GpsGroupData gpsGroupData = (GpsGroupData) realm.where(c).equalTo("id", item.getId()).findFirst();
                            gpsGroupData.getFriend().add(uid);
                            break;
                    }
                }
            });


//            RealmHelper.getInstance().addFriend(item,uid);


        } else {

        }
    }

}
