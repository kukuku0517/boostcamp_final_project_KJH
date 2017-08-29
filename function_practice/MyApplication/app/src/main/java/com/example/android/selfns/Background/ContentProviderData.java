package com.example.android.selfns.Background;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.android.selfns.Helper.DateHelper;
import com.example.android.selfns.Helper.RealmHelper;
import com.example.android.selfns.Interface.DataReceiveListener;

import io.realm.Realm;

/**
 * Created by samsung on 2017-07-27.
 */

public class ContentProviderData {

    long start, end;
    Realm realm;
    Context context;

    public ContentProviderData(Context context, long start, long end, Realm realm) {
        this.start = start;
        this.end = end;
        this.realm = realm;
        this.context = context;
    }

    public void readCallLogs(DataReceiveListener<String> listener) {
        String[] projection = {CallLog.Calls._ID, CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.TYPE};
        String selection = null;
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("permissionread","asf");
//            return;
//        }
        //https://stackoverflow.com/questions/40805971/permission-error-in-broadcastreceiver

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_CALL_LOG)) {
                return;
            } else {


                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CALL_LOG}, 1);

            }
        }
        Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, selection,
                null, CallLog.Calls.DEFAULT_SORT_ORDER);


        int total = 0;
        int count = 0;
        if (c.getCount() != 0) {
            total = c.getCount();
        }

        while (c.moveToNext()) {
         RealmHelper.getInstance().callDataSave(c);
            count++;
            listener.onReceive(String.format("전화 내역을 로딩 중입니다 \n %d/%d", count, total));
        }


        c.close();
    }

    public void readSMSMessage(DataReceiveListener<String> listener) {
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("permissionread","asf");return;
//        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_SMS)) {
                return;
            } else {


                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_SMS}, 1);

            }
        }
        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = context.getContentResolver();
        String[] projection = {"_id", "person", "date", "body", "address", "type"};
        String sortOrder = "date DESC";
        String selection = null;
        Cursor c = cr.query(allMessage, projection, selection, null, sortOrder);

        int total = 0;
        int count = 0;
        if (c.getCount() != 0) {
            total = c.getCount();
        }

        while (c.moveToNext()) {
            new RealmHelper(context).smsDataSave(c);
            count++;
            listener.onReceive(String.format("문자 내역을 로딩 중입니다 \n %d/%d", count, total));
        }
        c.close();
    }

    public void readImages(DataReceiveListener<String> listener) {
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("permissionread","asf"); return;
//        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                return;
            } else {


                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE};
        String selection = MediaStore.Images.Media.DATE_ADDED + ">" + DateHelper.getInstance().getDayAfter(start, -3) / 1000 + " and " + MediaStore.Images.Media.DATE_ADDED + "<" + end / 1000;
        Cursor imageCursor = context.getApplicationContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                selection,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        int total = 0;
        int count = 0;
        if (imageCursor.getCount() != 0) {
            total = imageCursor.getCount();
        }

        while (imageCursor.moveToNext()) {
            RealmHelper.getInstance().photoDataSave(imageCursor);
            count++;
            listener.onReceive(String.format("사진을 로딩 중입니다 \n %d/%d", count, total));
        }

        imageCursor.close();
    }
}
