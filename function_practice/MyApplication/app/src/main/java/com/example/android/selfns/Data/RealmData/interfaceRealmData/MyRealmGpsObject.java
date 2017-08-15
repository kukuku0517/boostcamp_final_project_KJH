package com.example.android.selfns.Data.RealmData.interfaceRealmData;

/**
 * Created by samsung on 2017-08-10.
 */

public interface MyRealmGpsObject extends MyRealmObject {
    double getLat();
    double getLng();
    String getPlace();


String getOriginId() ;

void setOriginId(String originId) ;

}
