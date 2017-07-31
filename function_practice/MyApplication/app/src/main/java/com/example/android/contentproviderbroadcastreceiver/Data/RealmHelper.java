package com.example.android.contentproviderbroadcastreceiver.Data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.util.Log;

import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyUnitData;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static android.R.attr.start;
import static com.example.android.contentproviderbroadcastreceiver.R.string.month;
import static com.example.android.contentproviderbroadcastreceiver.R.string.year;
import static io.realm.Realm.getDefaultInstance;

/**
 * Created by samsung on 2017-07-27.
 */

public class RealmHelper {

    static final long quarter = 21600000;

    public static long[] getDate() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        long start = today.getTimeInMillis();
        today.add(Calendar.DATE, 1);
        long end = today.getTimeInMillis();

        return new long[]{start, end};

    }

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

    public static void notifyDataSave(final String title, final String text, String subtext, final long when) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                NotifyData notifyData = realm.createObject(NotifyData.class);
                notifyData.setId(0);
                notifyData.setDate(when);
                notifyData.setContent(text);
                notifyData.setPerson(title);
                long[] today = getDate();
                long start = today[0];
                long end = today[1];
                RealmResults<NotifyGroupData> ngDatas = notifyGroupDataLoad("date", start, end);
                if (ngDatas.size() == 0) {
                    NotifyGroupData ngData1 = realm.createObject(NotifyGroupData.class);
                    NotifyGroupData ngData2 = realm.createObject(NotifyGroupData.class);
                    NotifyGroupData ngData3 = realm.createObject(NotifyGroupData.class);
                    NotifyGroupData ngData4 = realm.createObject(NotifyGroupData.class);
                    ngData1.setTime(start, start + quarter);
                    ngData2.setTime(start + quarter, start + quarter * 2);
                    ngData3.setTime(start + quarter * 2, start + quarter * 3);
                    ngData4.setTime(start + quarter * 3, start + quarter * 4);
                    ngDatas = RealmHelper.notifyGroupDataLoad("date", start, end);
                }
                int index;
                if (notifyData.getDate() < start + quarter) {
                    index = 0;
                } else if (notifyData.getDate() < start + quarter * 2) {
                    index = 1;
                } else if (notifyData.getDate() < start + quarter * 3) {
                    index = 2;
                } else {
                    index = 3;
                }

                NotifyGroupData ngData = ngDatas.get(index);

                int i = ngData.checkName(notifyData.getPerson());
                Log.d("grouping int i ", String.valueOf(i));
                if (i != -1) {
                    NotifyUnitData nuData = ngData.getUnits().get(i);
                    nuData.setCount(nuData.getCount() + 1);
                    nuData.setStart(notifyData.getDate());
                    nuData.setEnd(notifyData.getDate());
                    nuData.getNotifys().add(notifyData);
                } else {
                    NotifyUnitData nuData = new NotifyUnitData();
                    nuData.setCount(1);
                    nuData.setStart(notifyData.getDate());
                    nuData.setEnd(notifyData.getDate());
                    nuData.setName(notifyData.getPerson());
                    RealmList<NotifyData> notiList = new RealmList<>();
                    notiList.add(notifyData);
                    nuData.setNotifys(notiList);
                    ngData.getUnits().add(nuData);
                }
            }
        });


    }

    public static RealmResults<CallData> callDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CallData> cData = realm.where(CallData.class).between(query, start, end).findAll();
        return cData;
    }

    public static RealmResults<PhotoData> photoDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PhotoData> pData = realm.where(PhotoData.class).between(query, start, end).findAll();
        return pData;

    }

    public static RealmResults<SmsData> smsDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SmsData> sData = realm.where(SmsData.class).between(query, start, end).findAll();
        return sData;
    }

    public static RealmResults<NotifyData> notifyDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NotifyData> nData = realm.where(NotifyData.class).between(query, start, end).findAll();
        return nData;
    }

    public static RealmResults<NotifyGroupData> notifyGroupDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NotifyGroupData> ngData = realm.where(NotifyGroupData.class).between("start", start, end).findAll();
        return ngData;
    }

    public static RealmResults<NotifyUnitData> notifyUnitDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NotifyUnitData> nuData = realm.where(NotifyUnitData.class).between(query, start, end).findAll();
        return nuData;
    }

}
