package com.example.android.contentproviderbroadcastreceiver.Background;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.ContentResolver;
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
import android.provider.Telephony;
import android.service.notification.NotificationListenerService;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.contentproviderbroadcastreceiver.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Data.PhotoData;
import com.example.android.contentproviderbroadcastreceiver.Data.RealmHelper;
import com.example.android.contentproviderbroadcastreceiver.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static android.R.attr.id;
import static android.R.attr.type;
import static android.bluetooth.BluetoothClass.Service.TELEPHONY;
import static com.example.android.contentproviderbroadcastreceiver.R.string.month;
import static com.example.android.contentproviderbroadcastreceiver.R.string.year;

public class DataUpdateService extends Service {

    long sms = -1;
    long call = -1;
    long photo = -1;
    ContentResolver cr;
    ContentObserver callObserver, photoObserver, smsObserver;

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

        cr = getApplicationContext().getContentResolver();
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
                String[] projection = {"_id", "person", "date", "body", "address", "type", "status", "subject"};
                String sortOrder = "date DESC";
                if (!String.valueOf(uri).equals("content://sms/raw")) {
                    Cursor c = cr.query(uri, projection, null, null, sortOrder);
                    if (c.moveToNext()) {
                        long id = c.getLong(0);
                        if (sms != id) {
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

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.DATE_TAKEN};
                if (!String.valueOf(uri).equals("content://sms/raw")) {
                    Cursor c = cr.query(
                            uri, // 이미지 컨텐트 테이블
                            projection, // DATA를 출력
                            null,       // 모든 개체 출력
                            null,
                            null);
                    if (c != null && c.moveToNext()) {
                        String filePath = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                        Uri imageUri = Uri.parse(filePath);
                        long id = c.getLong(c.getColumnIndex(MediaStore.Images.Media._ID));
                        Log.d("photo uri", String.valueOf(imageUri));
                        Log.d("photo id", String.valueOf(id));
                        if (call != id) {
                            call = id;
                            RealmHelper.photoDataSave(c);
                        }
                    }
                }
            }
        };
        callObserver = new ContentObserver(mHandler) {

            @Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);

                String[] projection = {CallLog.Calls._ID, CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION};
//        String selection = CallLog.Calls.DATE + " < " + end + " and " + CallLog.Calls.DATE + ">" + start;
                String selection = null;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Log.d("callobs", String.valueOf(uri));
                String sortOrder = "date DESC";
                if (!String.valueOf(uri).equals("content://sms/raw")) {
                    Cursor c = cr.query(CallLog.Calls.CONTENT_URI, projection, selection,
                            null, CallLog.Calls.DEFAULT_SORT_ORDER);
                    if (c.moveToNext()) {
                        long id = c.getLong(c.getColumnIndex(CallLog.Calls._ID));
                        if (photo != id) {
                            photo = id;
                           new RealmHelper(getApplicationContext()).callDataSave(c);
                        }
                    }
                }
            }
        };

        cr.registerContentObserver(CallLog.Calls.CONTENT_URI, true, callObserver);
        cr.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, photoObserver);
        cr.registerContentObserver(allMessage, true, smsObserver);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cr.unregisterContentObserver(callObserver);
        cr.unregisterContentObserver(photoObserver);
        cr.unregisterContentObserver(smsObserver);

        Log.d("service", "off");
    }
}
