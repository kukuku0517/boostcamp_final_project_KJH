package com.example.android.contentproviderbroadcastreceiver.Background.NoUse;

import com.example.android.contentproviderbroadcastreceiver.Helper.ReceiverConstants;

public class GoogleStillReceiver extends MyFenceReceiver {


    @Override
    public String getFenceType() {
        return ReceiverConstants.STILL_KEY;
    }

    @Override
    public void onStateTrue(double lat, double lng, CharSequence place,int type) {
//        RealmHelper.gpsDataSave(System.currentTimeMillis(),lat,lng,1, (String) place ,2);

    }

    @Override
    public void onStateFalse(double lat, double lng, CharSequence place,int type) {
//        RealmHelper.gpsDataSave(System.currentTimeMillis(),lat, lng,1, (String) place,3);

    }
}