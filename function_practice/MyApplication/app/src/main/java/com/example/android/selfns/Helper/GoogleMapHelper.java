package com.example.android.selfns.Helper;

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
