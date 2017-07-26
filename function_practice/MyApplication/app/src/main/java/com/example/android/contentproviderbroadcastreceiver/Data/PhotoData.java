package com.example.android.contentproviderbroadcastreceiver.Data;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by samsung on 2017-07-26.
 */

public class PhotoData extends RealmObject implements MyRealmObject{
    long date;
    String lat;
    String lng;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
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
        return 1;
    }
}
