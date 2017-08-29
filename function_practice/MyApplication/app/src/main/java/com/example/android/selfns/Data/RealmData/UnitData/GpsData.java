package com.example.android.selfns.Data.RealmData.UnitData;

import com.example.android.selfns.Data.DTO.Detail.GpsDTO;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmGpsObject;

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
    private String place, originId;
    private int change, moveState;

    long _groupId;

    public long get_groupId() {
        return _groupId;
    }

    public void set_groupId(long _groupId) {
        this._groupId = _groupId;
    }

    public GpsData(GpsDTO data) {   this._groupId=data.get_groupId();
        this.id = data.getId();
        this.lat = data.getLat();
        this.lng = data.getLng();
        this.date = data.getDate();
        this.place = data.getPlace();
        this.originId = data.getOriginId();
        this.change = data.getChange();
        this.moveState = data.getMoveState();
    }
    public GpsData() {

    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
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


    @Override
    public int getType() {
        return type;
    }

    int type=RealmClassHelper.GPS_DATA;
    public long getDate() {
        return date;
    }

    public long getId() {
        return id;
    }


}
