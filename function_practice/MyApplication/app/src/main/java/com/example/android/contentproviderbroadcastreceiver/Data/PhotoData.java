package com.example.android.contentproviderbroadcastreceiver.Data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-26.
 */

public class PhotoData extends RealmObject implements MyRealmObject{
    long date;
    double lat;
    double lng;
    String place;
    @PrimaryKey
    long id;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
    @Override
    public long  getId() {
        return id;
    }
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getLat() {
        return lat;
    }

    public void setLat( double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng( double lng) {
        this.lng = lng;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    String path;

    @Override
    public int getType() {
        return 5;
    }
}
