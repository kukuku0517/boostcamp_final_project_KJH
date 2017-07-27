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

import static com.example.android.contentproviderbroadcastreceiver.R.string.month;
import static com.example.android.contentproviderbroadcastreceiver.R.string.year;

public class DataUpdateService extends Service {
    public DataUpdateService() {
    }

    long sms = -1;
    long call = -1;
    long photo = -1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service", "on");
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        Realm realm = Realm.getDefaultInstance();

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setWhen(0)
                .setTicker("").build();

        startForeground(1, notification);


        final ContentResolver cr = getApplicationContext().getContentResolver();
        final Uri allMessage = Uri.parse("content://sms");
        Handler mHandler = new Handler(Looper.getMainLooper());

        ContentObserver smsObserver = new ContentObserver(mHandler) {

            @Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);

                String[] projection = {"_id", "person", "date", "body"};
                String sortOrder = "date DESC";
                if (!String.valueOf(uri).equals("content://sms/raw")) {
                    Cursor c = cr.query(uri, projection, null, null, sortOrder);
                    if (c.moveToNext()) {
                        long id = c.getLong(0);
                        Log.d("smsid", String.valueOf(id));
                        if (sms != id) {
                            sms = id;
                            RealmHelper.smsDataSave(c);

                        }

                    }
                }
            }
        };
        ContentObserver photoObserver = new ContentObserver(mHandler) {

            @Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);

//                Calendar start =Calendar.getInstance();
//                start.set(Calendar.HOUR_OF_DAY,0);
//                start.set(Calendar.MINUTE,0);
//                start.set(Calendar.SECOND,0);
//                long startMillis = start.getTimeInMillis();
//                start.add(Calendar.DATE,1);
//                long endMillis = start.getTimeInMillis();

//                RealmResults<PhotoData> pData = RealmHelper.photoDataLoad("date",startMillis,endMillis);

                String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.DATE_TAKEN};

                if (!String.valueOf(uri).equals("content://sms/raw")) {

                    Cursor c = cr.query(
                            uri, // 이미지 컨텐트 테이블
                            projection, // DATA를 출력
                            null,       // 모든 개체 출력
                            null,
                            null);
                    if (c.moveToNext()) {

                        String filePath = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                        Uri imageUri = Uri.parse(filePath);
                        long id = c.getLong(c.getColumnIndex(MediaStore.Images.Media._ID));
                        if (call != id) {
                            call = id;

                            RealmHelper.photoDataSave(c);
                        }
                    }
                }
            }
        };
        ContentObserver callObserver = new ContentObserver(mHandler) {

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
                            RealmHelper.callDataSave(c);

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

        Log.d("service", "off");
    }
}
