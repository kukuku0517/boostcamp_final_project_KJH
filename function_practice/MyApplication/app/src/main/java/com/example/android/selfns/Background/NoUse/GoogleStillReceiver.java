package com.example.android.selfns.Background.NoUse;

import com.example.android.selfns.Helper.ReceiverConstants;

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
