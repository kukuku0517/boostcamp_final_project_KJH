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

import com.example.android.contentproviderbroadcastreceiver.Helper.DateHelper;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmHelper;

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

    public void readCallLogs() {
        String[] projection = {CallLog.Calls._ID, CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.TYPE};
        String selection = null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, selection,
                null, CallLog.Calls.DEFAULT_SORT_ORDER);
        while (c.moveToNext()) {
            RealmHelper.getInstance().callDataSave(c);
        }
        c.close();
    }

    public void readSMSMessage() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = context.getContentResolver();
        String[] projection = {"_id", "person", "date", "body", "address","type"};
        String sortOrder = "date DESC";
        String selection = null;
        Cursor c = cr.query(allMessage, projection, selection, null, sortOrder);
        while (c.moveToNext()) {
            new RealmHelper(context).smsDataSave(c);
        }
        c.close();
    }

    public void readImages() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE};
        String selection = MediaStore.Images.Media.DATE_ADDED + ">" + DateHelper.getInstance().getDayAfter(start,-3)/1000 + " and " + MediaStore.Images.Media.DATE_ADDED + "<" + end / 1000;
        Cursor imageCursor = context.getApplicationContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
              selection,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함
        while (imageCursor.moveToNext()) {
            RealmHelper.getInstance().photoDataSave(imageCursor);
        }
        imageCursor.close();
    }
}
