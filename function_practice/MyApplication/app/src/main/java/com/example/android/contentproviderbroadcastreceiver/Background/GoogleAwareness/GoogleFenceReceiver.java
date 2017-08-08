package com.example.android.contentproviderbroadcastreceiver.Background.GoogleAwareness;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.contentproviderbroadcastreceiver.Helper.RealmHelper;
import com.example.android.contentproviderbroadcastreceiver.Helper.ReceiverConstants;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GoogleFenceReceiver extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient locationClient;
    private final String TAG = getClass().getSimpleName();
    Context context;

    private int fenceStateNum;
    private int fenceType;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        FenceState fenceState = FenceState.extract(intent);
        Log.d("fenceCheck", fenceState.getFenceKey()); //false 1, true 2
//        Toast.makeText(context, String.valueOf(fenceState.getCurrentState()) + intent.getAction(), Toast.LENGTH_LONG).show();
        if (TextUtils.equals(fenceState.getFenceKey(), ReceiverConstants.ON_FOOT_KEY)) {
            fenceStateNum = fenceState.getCurrentState();
            fenceType = 0;
        } else if (TextUtils.equals(fenceState.getFenceKey(), ReceiverConstants.STILL_KEY)) {
            fenceStateNum = fenceState.getCurrentState();
            fenceType = 2;
        } else if (TextUtils.equals(fenceState.getFenceKey(), ReceiverConstants.VEHICLE_KEY)) {
            fenceStateNum = fenceState.getCurrentState();
            fenceType = 4;
        } else if(TextUtils.equals(fenceState.getFenceKey(), ReceiverConstants.HEADPHONE_KEY)) {
            fenceStateNum = fenceState.getCurrentState();
            fenceType = 6;
        }else{

        }
        Log.d("fenceState",fenceType+""+fenceStateNum);
        locationClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Awareness.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        locationClient.connect();

    }


    public void onStateTrue(double lat, double lng, CharSequence place, String originId){
        RealmHelper.getInstance().gpsDataSave(System.currentTimeMillis(),lat,lng,1, (String) place , fenceType+fenceStateNum, originId);

    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        final int[] type = new int[1];
        final double[] lat = new double[1];
        final double[] lng = new double[1];
        final CharSequence[] place = new CharSequence[1];

        Awareness.SnapshotApi.getPlaces(locationClient)
                .setResultCallback(new ResultCallback<PlacesResult>() {
                    @Override
                    public void onResult(@NonNull PlacesResult placesResult) {
                        if (!placesResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not get location.");
                            return;
                        }
                        List<PlaceLikelihood> placeLikelihoods = placesResult.getPlaceLikelihoods();
                        LatLng latLng = placeLikelihoods.get(0).getPlace().getLatLng();
                        double lat = latLng.latitude;
                      double lng = latLng.longitude;
                       CharSequence place = placeLikelihoods.get(0).getPlace().getName();
                        String originId = placeLikelihoods.get(0).getPlace().getId();
                        onStateTrue(lat,lng,place,originId);


//                                        RealmHelper.gpsDataSave(System.currentTimeMillis(),lat[0],lng[0],1, (String) place[0],type[0]);


                    }


                });


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}