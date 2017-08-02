package com.example.android.contentproviderbroadcastreceiver.Background;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
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

import com.example.android.contentproviderbroadcastreceiver.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.Data.RealmHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.text.DecimalFormat;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.R.attr.start;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;


public class GoogleLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    // . . . . . . . . more stuff here
    LocationRequest locationRequest;
    GoogleApiClient locationClient;
    Location lastLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("googleLoc", "start");
        locationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * 60*2);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationRequest.setFastestInterval(1000 * 60*2);
        locationClient.connect();
        return Service.START_STICKY;
    }

    // . . . . . . . . other methods
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        FusedLocationApi.requestLocationUpdates(locationClient, locationRequest, this);
//
//        if (location == null)
//            locationClient.requestLocationUpdates(locationRequest, this);
//        else
//            Toast.makeText(getActivity(), "Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    // . . . . . . . . other methods
    @Override
    public void onLocationChanged(final Location location) {
//        locationClient.removeLocationUpdates(this);
//        LocationServices.FusedLocationApi.removeLocationUpdates(locationClient,this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
                Log.d("googleLoc last", String.valueOf(lastLocation.getLatitude()));
//                double distance = CalculationByDistance(lastLocation.getLatitude(), lastLocation.getLongitude(), location.getLatitude(), location.getLongitude());
//                Log.d("googleLoc dis", String.valueOf(distance));
                Toast toast = null;
                double distance = location.distanceTo(lastLocation);
                Log.d("googleLoc dis2", String.valueOf(distance));
                Log.d("googleLoc time", String.valueOf(location.getTime()));

                Log.d("googleLoc ctime", String.valueOf(System.currentTimeMillis()));
                if (distance > 50) {
                    lastLocation = location;

//                        toast.cancel();
//                    toast.makeText(getApplicationContext(), lastLocation.getLatitude()+lastLocation.getLongitude()+ " : change("+distance, Toast.LENGTH_SHORT).show();
                    RealmHelper.gpsDataSave(location.getTime(), location.getLatitude(), location.getLongitude(), 1,
                            (String) likelyPlaces.get(0).getPlace().getName());

                } else {

//                        toast.cancel();
//                    toast.makeText(getApplicationContext(), lastLocation.getLatitude()+","+lastLocation.getLongitude()+ "\n meaningless("+distance, Toast.LENGTH_SHORT).show();

                    RealmHelper.gpsDataSave(location.getTime(), location.getLatitude(), location.getLongitude(), 0, (String) likelyPlaces.get(0).getPlace().getName());

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

//    public double CalculationByDistance(double lat_1, double lng_1, double lat_2, double lng_2) {
//        int Radius = 6371;// radius of earth in Km
//        double lat1 = lat_1;
//        double lat2 = lat_2;
//        double lon1 = lng_1;
//        double lon2 = lng_2;
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLon = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
//                + Math.cos(Math.toRadians(lat1))
//                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
//                * Math.sin(dLon / 2);
//        double c = 2 * Math.asin(Math.sqrt(a));
//        double valueResult = Radius * c;
//        double km = valueResult / 1;
//        DecimalFormat newFormat = new DecimalFormat("####");
//        int kmInDec = Integer.valueOf(newFormat.format(km));
//        double meter = valueResult % 1000;
//        int meterInDec = Integer.valueOf(newFormat.format(meter));
//        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
//                + " Meter   " + meterInDec);
//
//        return meterInDec;
//    }
}