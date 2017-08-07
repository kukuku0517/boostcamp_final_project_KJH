package com.example.android.contentproviderbroadcastreceiver.Background;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.android.contentproviderbroadcastreceiver.BuildConfig;
import com.example.android.contentproviderbroadcastreceiver.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.Data.RealmHelper;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceQueryRequest;
import com.google.android.gms.awareness.fence.FenceQueryResult;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceStateMap;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.annotations.Required;

import static android.R.attr.start;
import static android.provider.Settings.System.DATE_FORMAT;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;


public class GoogleLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    // . . . . . . . . more stuff here
    LocationRequest locationRequest;
    GoogleApiClient locationClient;
    Location lastLocation;

    // The fence key is how callback code determines which fence fired.


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        locationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Awareness.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * 60 * 2);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(1000 * 60 * 2);
        locationClient.connect();
        return Service.START_STICKY;
    }



    @Override
    public void onConnected(Bundle bundle) {
        Location last = LocationServices.FusedLocationApi.getLastLocation(locationClient);



        if (last != null) {
            lastLocation = last;
        } else if (lastLocation != null) {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<GpsData> gpsData = realm.where(GpsData.class).findAll();
            GpsData lastdata = gpsData.last();
            lastLocation.setLatitude(lastdata.getLat());
            lastLocation.setLongitude(lastdata.getLng());
        } else {
            lastLocation = new Location("fused");
            lastLocation.setLatitude(0);
            lastLocation.setLongitude(0);
        }
        Log.d("googleLoc", "connected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        FusedLocationApi.requestLocationUpdates(locationClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    // . . . . . . . . other methods
    @Override
    public void onLocationChanged(final Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(locationClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
//                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                    Log.i("googleLoc", String.format("Place '%s' has likelihood: %g",
//                            placeLikelihood.getPlace().getName(),
//                            placeLikelihood.getLikelihood()));
//                }

                double distance = location.distanceTo(lastLocation);

                if (distance > 50) {
                    lastLocation = location;
                    RealmHelper.gpsDataSave(location.getTime(), location.getLatitude(), location.getLongitude(), 1,
                            (String) likelyPlaces.get(0).getPlace().getName(),0);

                } else {
                    RealmHelper.gpsDataSave(location.getTime(), location.getLatitude(),
                            location.getLongitude(), 0, (String) likelyPlaces.get(0).getPlace().getName(),0);

                }
                Log.d("googleLoc now", String.valueOf(location.getLatitude()));
                Log.d("googleLoc now", String.valueOf(location.getLongitude()));
                Log.d("googleLoc now", String.valueOf(location.getProvider()));
                likelyPlaces.release();
            }
        });


        // Use the location here!!!
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d("googleLoc", "fails");
    }

}