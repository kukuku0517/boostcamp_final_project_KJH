package com.example.android.contentproviderbroadcastreceiver.Background;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.android.contentproviderbroadcastreceiver.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Data.PhotoData;
import com.example.android.contentproviderbroadcastreceiver.Data.RealmHelper;
import com.example.android.contentproviderbroadcastreceiver.Data.SmsData;

import io.realm.Realm;

/**
 * Created by samsung on 2017-07-27.
 */

public class ContentProviderData {

long start,end;

    Realm realm;
Context context;
    public ContentProviderData(Context context , long start, long end, Realm realm) {
        this.start = start;
        this.end = end;
        this.realm = realm;
        this.context = context;

    }

    public   void readCallLogs() {

        CallData callData = new CallData();

        String[] projection = {CallLog.Calls._ID, CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION};
//        String selection = CallLog.Calls.DATE + " < " + end + " and " + CallLog.Calls.DATE + ">" + start;
        String selection = null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, selection,
                null, CallLog.Calls.DEFAULT_SORT_ORDER);

        while (c.moveToNext()) {
            RealmHelper.callDataSave(c);

        }

    }

    public void readSMSMessage() {
        SmsData smsData = new SmsData();
        Log.d("start", String.valueOf(start));
        Log.d("start", String.valueOf(end));

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = context.getContentResolver();
        String[] projection = {"_id", "person", "date", "body"};

        String sortOrder = "date DESC";
        String selection = null;
        Cursor c = cr.query(allMessage, projection, selection, null, sortOrder);

        while (c.moveToNext()) {
            RealmHelper.smsDataSave(c);

        }

    }

   public  void readImages() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        PhotoData photo = new PhotoData();
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.DATE_TAKEN};
        String selection = null;
//        String selection = MediaStore.Images.Media.DATE_ADDED + ">" + start/1000 + " and " + MediaStore.Images.Media.DATE_ADDED + "<" + end/1000;
        Cursor imageCursor = context.getApplicationContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                selection,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함


        while (imageCursor.moveToNext()) {
           RealmHelper.photoDataSave(imageCursor);
        }


    }
}
