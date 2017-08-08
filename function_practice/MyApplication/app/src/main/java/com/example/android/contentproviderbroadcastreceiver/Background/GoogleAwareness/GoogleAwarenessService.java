package com.example.android.contentproviderbroadcastreceiver.Background.GoogleAwareness;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.contentproviderbroadcastreceiver.BuildConfig;
import com.example.android.contentproviderbroadcastreceiver.Helper.ReceiverConstants;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceQueryRequest;
import com.google.android.gms.awareness.fence.FenceQueryResult;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceStateMap;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.util.Arrays;


public class GoogleAwarenessService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    GoogleApiClient locationClient;
    private final String TAG = getClass().getSimpleName();
    private PendingIntent mPendingIntent;
    private GoogleFenceReceiver myFenceReceiver;
    private final String FENCE_RECEIVER_ACTION =
            BuildConfig.APPLICATION_ID + "FENCE_RECEIVER_ACTION";

    public GoogleAwarenessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Awareness.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        locationClient.connect();
        return Service.START_STICKY;
    }

    protected void queryFence(final String fenceKey) {
        Awareness.FenceApi.queryFences(locationClient,
                FenceQueryRequest.forFences(Arrays.asList(fenceKey)))
                .setResultCallback(new ResultCallback<FenceQueryResult>() {
                    @Override
                    public void onResult(@NonNull FenceQueryResult fenceQueryResult) {
                        if (!fenceQueryResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not query fence: " + fenceKey);
                            return;
                        }
                        FenceStateMap map = fenceQueryResult.getFenceStateMap();


                        for (String fenceKey : map.getFenceKeys()) {

                            FenceState fenceState = map.getFenceState(fenceKey);

                            Log.i(TAG, "Fence " + fenceKey + ": "
                                    + fenceState.getCurrentState()
                                    + ", was="
                                    + fenceState.getPreviousState()
                                    + ", lastUpdateTime="
                            );
                        }
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        AwarenessFence walkingFence = DetectedActivityFence.during(DetectedActivityFence.ON_FOOT);
        AwarenessFence stillFence = DetectedActivityFence.during(DetectedActivityFence.STILL);
        AwarenessFence vehicleFence = DetectedActivityFence.during(DetectedActivityFence.IN_VEHICLE);
        AwarenessFence cycleFence = DetectedActivityFence.during(DetectedActivityFence.ON_BICYCLE);

        AwarenessFence moveFence = AwarenessFence.or(vehicleFence, cycleFence);
        AwarenessFence headPhone = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);


// Create a combination fence to AND primitive fences.

        Intent i = new Intent(FENCE_RECEIVER_ACTION);
        mPendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);

//        stillReceiver = new GoogleStillReceiver();
//        onFootReceiver = new GoogleOnFootReceiver();
//        vehicleReceiver = new GoogleVehicleReceiver();
//
//
//        registerReceiver(stillReceiver, new IntentFilter(FENCE_RECEIVER_ACTION));
//        registerReceiver(onFootReceiver, new IntentFilter(FENCE_RECEIVER_ACTION));
        myFenceReceiver = new GoogleFenceReceiver();
        registerReceiver(myFenceReceiver, new IntentFilter(FENCE_RECEIVER_ACTION));

        Awareness.FenceApi.updateFences(
                locationClient,
                new FenceUpdateRequest.Builder()
                        .addFence(ReceiverConstants.ON_FOOT_KEY, walkingFence, mPendingIntent)
//                        .addFence(ReceiverConstants.STILL_KEY, stillFence, mPendingIntent)
//                        .addFence(ReceiverConstants.VEHICLE_KEY, moveFence, mPendingIntent)
                        .addFence(ReceiverConstants.HEADPHONE_KEY, headPhone, mPendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Fence was successfully registered.");
//                            queryFence(FENCE_KEY);
                        } else {
                            Log.e(TAG, "Fence could not be registered: " + status);
                        }
                    }
                });


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Awareness.FenceApi.updateFences(
                locationClient,
                new FenceUpdateRequest.Builder()
                        .removeFence(ReceiverConstants.ON_FOOT_KEY)
//                        .removeFence(ReceiverConstants.STILL_KEY)
//                        .removeFence(ReceiverConstants.VEHICLE_KEY)
                        .removeFence(ReceiverConstants.HEADPHONE_KEY)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Fence was successfully registered.");
//                            queryFence(FENCE_KEY);
                        } else {
                            Log.e(TAG, "Fence could not be registered: " + status);
                        }
                    }
                });
    }
}
