package com.example.android.contentproviderbroadcastreceiver.Data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-01.
 */

public class GpsData extends RealmObject implements MyRealmObject {
    double lat;
    double lng;
    long date;
    int change;
    String place;
    @PrimaryKey
    long id;



    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public long getDate() {
        return date;
    }

    @Override
    public long getId() {
        return id;
    }

}
