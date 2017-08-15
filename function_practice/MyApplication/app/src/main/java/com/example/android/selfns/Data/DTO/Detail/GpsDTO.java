package com.example.android.selfns.Data.DTO.Detail;
import com.example.android.selfns.Data.DTO.interfaceDTO.GpsableDTO;
import com.example.android.selfns.Data.RealmData.UnitData.GpsData;
import com.example.android.selfns.Helper.RealmClassHelper;

import org.parceler.Parcel;

/**
 * Created by samsung on 2017-08-15.
 */
@Parcel
public class GpsDTO implements GpsableDTO {

    public GpsDTO(GpsData data) {
        this.id = data.getId();
        this.lat = data.getLat();
        this.lng = data.getLng();
        this.date = data.getDate();
        this.place = data.getPlace();
        this.originId = data.getOriginId();
        this.change = data.getChange();
        this.moveState = data.getMoveState();
    }

long id;

    public void setId(long id) {
        this.id = id;
    }

    double lat, lng;
  long date;
   String place, originId;
 int change, moveState;


    public GpsDTO() {

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

    public int getType() {
        return RealmClassHelper.GPS_DATA;
    }

    public long getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

}
