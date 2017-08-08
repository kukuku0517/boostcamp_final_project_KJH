package com.example.android.contentproviderbroadcastreceiver.Helper;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

/**
 * Created by samsung on 2017-08-08.
 */

public class GoogleMapHelper {
    public static GoogleMapHelper instance;

    public static GoogleMapHelper getInstance(){
        if(instance==null){
            instance=new GoogleMapHelper();

        }
        return instance;
    }

}
