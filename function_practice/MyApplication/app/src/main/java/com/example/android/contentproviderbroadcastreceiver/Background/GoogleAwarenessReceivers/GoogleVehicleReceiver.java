package com.example.android.contentproviderbroadcastreceiver.Background.GoogleAwarenessReceivers;

import com.example.android.contentproviderbroadcastreceiver.Data.RealmHelper;

public class GoogleVehicleReceiver extends MyFenceReceiver {

    //    AwarenessFence walkingFence = DetectedActivityFence.during(DetectedActivityFence.ON_FOOT);
//    AwarenessFence stillFence = DetectedActivityFence.during(DetectedActivityFence.STILL);
//    AwarenessFence vehicleFence = DetectedActivityFence.during(DetectedActivityFence.IN_VEHICLE);
//    AwarenessFence cycleFence = DetectedActivityFence.during(DetectedActivityFence.ON_BICYCLE);
//
//    AwarenessFence moveFence =
//            AwarenessFence.or(vehicleFence, cycleFence);


    @Override
    public String getFenceType() {
        return ReceiverConstants.VEHICLE_KEY;
    }

    @Override
    public void onStateTrue(double lat, double lng, CharSequence place,int type) {
        RealmHelper.gpsDataSave(System.currentTimeMillis(),lat,lng,1, (String) place ,4);

    }

    @Override
    public void onStateFalse(double lat, double lng, CharSequence place,int type) {
        RealmHelper.gpsDataSave(System.currentTimeMillis(),lat, lng,1, (String) place,5);

    }
}
