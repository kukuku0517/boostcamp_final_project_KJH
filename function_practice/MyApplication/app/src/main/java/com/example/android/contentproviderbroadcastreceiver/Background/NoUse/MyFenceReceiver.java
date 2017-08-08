package com.example.android.contentproviderbroadcastreceiver.Background.NoUse;

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

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public abstract class MyFenceReceiver extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
   GoogleApiClient locationClient;
    private final String TAG = getClass().getSimpleName();
    Context context;

    private int fenceStateNum;

    public abstract String getFenceType();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        FenceState fenceState = FenceState.extract(intent);
        Log.d("fenceCheck", fenceState.getFenceKey());
//        Toast.makeText(context, String.valueOf(fenceState.getCurrentState()) + intent.getAction(), Toast.LENGTH_LONG).show();
        if (TextUtils.equals(fenceState.getFenceKey(), getFenceType())) {
            fenceStateNum = fenceState.getCurrentState();

        } else {
        }
        locationClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Awareness.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        locationClient.connect();

    }


    public abstract void onStateTrue(double lat, double lng, CharSequence place, int type);

    public abstract void onStateFalse(double lat, double lng, CharSequence place, int type);

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        final int[] type = new int[1];
        final double[] lat = new double[1];
        final double[] lng = new double[1];
        final CharSequence[] place = new CharSequence[1];

        Awareness.SnapshotApi.getDetectedActivity(locationClient)
                .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                    @Override
                    public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                        if (!detectedActivityResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Could not get the current activity.");
                            return;
                        }

                        ActivityRecognitionResult ar = detectedActivityResult.getActivityRecognitionResult();
                        DetectedActivity probableActivity = ar.getMostProbableActivity();
                        type[0] = probableActivity.getType();

                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
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
                                        lat[0] = latLng.latitude;
                                        lng[0] = latLng.longitude;
                                        place[0] = placeLikelihoods.get(0).getPlace().getName();


//                                        RealmHelper.gpsDataSave(System.currentTimeMillis(),lat[0],lng[0],1, (String) place[0],type[0]);

                                        switch (fenceStateNum) {
                                            case FenceState.TRUE:
                                                onStateTrue(lat[0], lng[0], place[0], type[0]);
                                                break;
                                            case FenceState.FALSE:
                                                onStateFalse(lat[0], lng[0], place[0], type[0]);
                                                break;
                                            case FenceState.UNKNOWN:
                                                break;
                                        }
                                    }


                                });
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