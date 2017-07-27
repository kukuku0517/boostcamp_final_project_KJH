package com.example.android.contentproviderbroadcastreceiver.Data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;

import static io.realm.Realm.getDefaultInstance;

/**
 * Created by samsung on 2017-07-27.
 */

public class RealmHelper {

    public static void callDataSave(Cursor c) {

        Realm realm = Realm.getDefaultInstance();
        CallData callData = new CallData();

        String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
        String phone = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
        String duration = c.getString(c.getColumnIndex(CallLog.Calls.DURATION));
        long datetimeMillis = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
        long id = c.getLong(c.getColumnIndex(CallLog.Calls._ID));

        realm.beginTransaction();
        callData.setId(id);
        callData.setPerson(name);
        callData.setDate(datetimeMillis);
        callData.setDuration(duration);
        callData.setNumber(phone);

        realm.copyToRealm(callData);
        realm.commitTransaction();
    }


    public static void photoDataSave(Cursor imageCursor) {
        Realm realm = Realm.getDefaultInstance();
        PhotoData photo = new PhotoData();

        String filePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
        Uri imageUri = Uri.parse(filePath);
        long date = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)) * 1000;
        long id = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));

        String lat = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
        String lng = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
        Log.d("photo", String.valueOf(date));

        realm.beginTransaction();
        photo.setId(id);
        photo.setDate(date);
        photo.setLat(lat);
        photo.setLng(lng);
        photo.setPath(filePath);

        realm.copyToRealm(photo);
        realm.commitTransaction();
    }

    public static void smsDataSave(Cursor c) {
        Realm realm = Realm.getDefaultInstance();
        SmsData smsData = new SmsData();
        long messageId = c.getLong(0);
        long contactId = c.getLong(1);
        String contactId_string = String.valueOf(contactId);
        long timestamp = c.getLong(2);
        String body = c.getString(3);

        realm.beginTransaction();

        smsData.setId(messageId);
        smsData.setDate(timestamp);
        smsData.setContent(body);
        smsData.setPerson(contactId_string);

        realm.copyToRealm(smsData);
        realm.commitTransaction();


    }

    public static  RealmResults<CallData> callDataLoad(String query, long start, long end){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CallData> cData = realm.where(CallData.class).between(query, start, end).findAll();
        return cData;
    }
    public static  RealmResults<PhotoData> photoDataLoad(String query, long start, long end){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PhotoData> pData = realm.where(PhotoData.class).between(query, start, end).findAll();
        return pData;

    } public static  RealmResults<SmsData> smsDataLoad(String query, long start, long end){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SmsData> sData = realm.where(SmsData.class).between(query, start, end).findAll();
        return sData;
    }

}
