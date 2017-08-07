package com.example.android.contentproviderbroadcastreceiver.Data;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-26.
 */

public class PhotoData extends RealmObject implements MyRealmParcelableObject {
    @PrimaryKey
    private long id;

    private long date;
    private double lat, lng;
    String place, comment;

    public PhotoData() {

    }

    protected PhotoData(Parcel in) {
        id = in.readLong();
        date = in.readLong();
        lat = in.readDouble();
        lng = in.readDouble();
        place = in.readString();
        comment = in.readString();
        path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(date);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(place);
        dest.writeString(comment);
        dest.writeString(path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoData> CREATOR = new Creator<PhotoData>() {
        @Override
        public PhotoData createFromParcel(Parcel in) {
            return new PhotoData(in);
        }

        @Override
        public PhotoData[] newArray(int size) {
            return new PhotoData[size];
        }
    };

    public String getComment() {
        return comment;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
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

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
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
