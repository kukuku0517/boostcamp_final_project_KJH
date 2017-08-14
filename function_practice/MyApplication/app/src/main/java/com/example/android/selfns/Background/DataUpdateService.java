package com.example.android.selfns.Background;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.selfns.Helper.RealmHelper;
import com.example.android.selfns.R;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DataUpdateService extends Service {

    //contentObserver 임시 id
    long sms = -1;
    long call = -1;
    long photo = -1;
    ContentResolver contentResolver;
    ContentObserver callObserver, photoObserver, smsObserver;

    /**
     * realm intialize
     * notification
     * content resolver setting (sms, photo, call)
     **/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "on");
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setWhen(0)
                .setTicker("").build();
        startForeground(1, notification);

        contentResolver = getApplicationContext().getContentResolver();
        final Uri allMessage = Uri.parse("content://sms");
        Handler mHandler = new Handler(Looper.getMainLooper());

        smsObserver = new ContentObserver(mHandler) {

            @Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                String[] projection = {"_id", "person", "date", "body", "address", "type"};
                String sortOrder = "date DESC";
                if (!String.valueOf(uri).equals("content://sms/raw")) {
                    Cursor c = contentResolver.query(uri, projection, null, null, sortOrder);
                    if (c.moveToNext()) {
                        long id = c.getLong(0);
                        int type = c.getInt(5);
                        Log.d("smstype", String.valueOf(type));
                        if (sms != id && type != 6 && type != 4) { //TODO sms 없어도 될듯
                            sms = id;
                            new RealmHelper(getApplicationContext()).smsDataSave(c);
                        }
                    }
                }
            }
        };

        photoObserver = new ContentObserver(mHandler) {

            @Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            /*
            * photo observer
            * 조건 :
            * 이전에 들어온것과 id가 같지않은것,
            * 가장 최신에 들어온것 보다 시간이 같거나 큰것(방금 찍은게 저장이되고 그 사진이 가장 최신이 되긴 하는듯?)
            *
            * */
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE};
                Cursor c = contentResolver.query(
                        uri, // 이미지 컨텐트 테이블
                        projection, // DATA를 출력
                        null,       // 모든 개체 출력
                        null,
                        null);
                if (c != null && c.moveToNext()) {
                    long id = c.getLong(c.getColumnIndex(MediaStore.Images.Media._ID));
                    long time = c.getLong(c.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                    if (call != id && time >= readLastDateFromMediaStore(getApplicationContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI)) {
                        call = id;
                        RealmHelper.getInstance().photoDataSave(c);
                    }
                }
            }

        };

        /*
        * call observer
        * 수신, 발신만 저장
        *
        * */
        callObserver = new ContentObserver(mHandler) {

            @Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String[] projection = {CallLog.Calls._ID, CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.TYPE};
                String selection = null;
                Cursor c = contentResolver.query(CallLog.Calls.CONTENT_URI, projection, selection,
                        null, CallLog.Calls.DEFAULT_SORT_ORDER);
                if (c.moveToNext()) {
                    long id = c.getLong(c.getColumnIndex(CallLog.Calls._ID));
                    long type = c.getLong(c.getColumnIndex(CallLog.Calls.TYPE));
                                       if (photo != id && (type == 1 || type == 2)) {
                        photo = id;
                        new RealmHelper(getApplicationContext()).callDataSave(c);
                    }
                }
            }
        };

        contentResolver.registerContentObserver(CallLog.Calls.CONTENT_URI, true, callObserver);
        contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, photoObserver);
        contentResolver.registerContentObserver(allMessage, true, smsObserver);

        return super.onStartCommand(intent, flags, startId);
    }

    private Long readLastDateFromMediaStore(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, "date_added DESC");
        Long dateAdded = -1L;
        if (cursor.moveToNext()) {
            dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED));
        }
        cursor.close();
        return dateAdded;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contentResolver.unregisterContentObserver(callObserver);
        contentResolver.unregisterContentObserver(photoObserver);
        contentResolver.unregisterContentObserver(smsObserver);

        Log.d("service", "off");
    }
}
