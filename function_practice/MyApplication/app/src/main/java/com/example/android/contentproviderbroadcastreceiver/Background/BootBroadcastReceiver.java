package com.example.android.contentproviderbroadcastreceiver.Background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
    public static boolean isContainedInNotificationListeners(Context $context)
    {
        String enabledListeners = Settings.Secure.getString($context.getContentResolver(), "enabled_notification_listeners");
        return !TextUtils.isEmpty(enabledListeners) && enabledListeners.contains($context.getPackageName());
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            if (!isContainedInNotificationListeners(context))
            {
                Log.d("###notify","on");
                Intent notifyIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                context.startService(notifyIntent);
            }else{

                Log.d("###notify","off");
            }


            Intent updateIntent = new Intent(context, DataUpdateService.class);
            context.startService(updateIntent);
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
