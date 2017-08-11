package com.example.android.contentproviderbroadcastreceiver.DetailView.Data;

import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmGpsObject;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-01.
 */

public class GpsData extends RealmObject implements MyRealmGpsObject {

    @PrimaryKey
    private long id;

    public void setId(long id) {
        this.id = id;
    }

    private double lat, lng;
    private long date;
    private String place, comment, originId;
    private int change, moveState;
    private boolean highlight = false;

    @Override
    public void setHighlight() {
        highlight = !highlight;
    }

    @Override
    public boolean getHighlight() {
        return highlight;
    }
    public GpsData() {

    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }


    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getMoveState() {
        return moveState;
    }

    public void setMoveState(int moveState) {
        this.moveState = moveState;
    }


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

    public int getType() {
        return 7;
    }

    public long getDate() {
        return date;
    }

    public long getId() {
        return id;
    }


    @Override
    public String select() {
        return String.valueOf(date);
    }
}
