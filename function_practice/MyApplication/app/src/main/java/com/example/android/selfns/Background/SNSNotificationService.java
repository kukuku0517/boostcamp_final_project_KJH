package com.example.android.selfns.Background;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.android.selfns.Helper.RealmHelper;

/**
 * Created by samsung on 2017-07-28.
 */

public class SNSNotificationService extends NotificationListenerService{
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("NotificationListener", "[snowdeer] onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("NotificationListener", "[snowdeer] onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("NotificationListener", "[snowdeer] onDestroy()");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("NotificationListener", "[snowdeer] onNotificationPosted() - " + sbn.toString());
        Log.i("NotificationListener", "[snowdeer] PackageName:" + sbn.getPackageName());
        Log.i("NotificationListener", "[snowdeer] PostTime:" + sbn.getPostTime());

        Notification notificatin = sbn.getNotification();
        Bundle extras = notificatin.extras;

        Log.i("NotificationListener", "[snowdeer] bundle:"+extras);
        String title = extras.getString(Notification.EXTRA_TITLE);
        int smallIconRes = extras.getInt(Notification.EXTRA_SMALL_ICON);
        Bitmap largeIcon = ((Bitmap) extras.getParcelable(Notification.EXTRA_LARGE_ICON));
        long when = notificatin.when;
        CharSequence text = extras.getString(Notification.EXTRA_TEXT);
        CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);

        Log.i("NotificationListener", "[snowdeer] Title:" + title);
//        extras.get
        CharSequence people =extras.getCharSequence(Notification.EXTRA_PEOPLE);
        Log.i("NotificationListener", "[snowdeer] people:" +people);

        Log.i("NotificationListener", "[snowdeer] Text:" + text);;
        String convtitle = (String) extras.getCharSequence(Notification.EXTRA_CONVERSATION_TITLE);
        Log.i("NotificationListener", "[snowdeer] conv title:" + convtitle);
        Log.i("NotificationListener", "[snowdeer] Sub Text:" + subText);

        Log.i("NotificationListener", "[snowdeer] BIG TEXT:" + extras.getString(Notification.EXTRA_BIG_TEXT));

        Log.i("NotificationListener", "[snowdeer] id:" + extras.getString("uuid"));
        Log.i("NotificationListener", "[snowdeer] intfo :" + extras.getString(Notification.EXTRA_INFO_TEXT));
//        Log.i("NotificationListener", "[snowdeer] Group:" + notificatin.getGroup());
//        Log.i("NotificationListener", "[snowdeer] Title:" + notificatin.getSortKey());
//

        RealmHelper.getInstance().notifyDataSave(title, String.valueOf(text), String.valueOf(subText), when);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("NotificationListener", "[snowdeer] onNotificationRemoved() - " + sbn.toString());
    }

}
