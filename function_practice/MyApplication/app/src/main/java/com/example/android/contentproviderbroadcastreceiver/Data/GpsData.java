package com.example.android.contentproviderbroadcastreceiver.Data;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-01.
 */

public class GpsData extends RealmObject implements MyRealmParcelableObject{

    @PrimaryKey
    private long id;

    private double lat, lng;
    private long date;
    private String place, comment;
    private int change, moveState;

    protected GpsData(Parcel in) {
        id = in.readLong();
        lat = in.readDouble();
        lng = in.readDouble();
        date = in.readLong();
        place = in.readString();
        comment = in.readString();
        change = in.readInt();
        moveState = in.readInt();
    }

    public GpsData() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeLong(date);
        dest.writeString(place);
        dest.writeString(comment);
        dest.writeInt(change);
        dest.writeInt(moveState);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GpsData> CREATOR = new Creator<GpsData>() {
        @Override
        public GpsData createFromParcel(Parcel in) {
            return new GpsData(in);
        }

        @Override
        public GpsData[] newArray(int size) {
            return new GpsData[size];
        }
    };

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
