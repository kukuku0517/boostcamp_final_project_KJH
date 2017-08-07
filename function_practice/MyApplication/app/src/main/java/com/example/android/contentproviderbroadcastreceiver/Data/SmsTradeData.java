package com.example.android.contentproviderbroadcastreceiver.Data;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-04.
 */

public class SmsTradeData extends RealmObject implements MyRealmParcelableObject{
    @PrimaryKey
    long id;

    private String address, content, person, place, comment;
    private long date;
    private double lat, lng;

    public SmsTradeData() {

    }

    protected SmsTradeData(Parcel in) {
        id = in.readLong();
        address = in.readString();
        content = in.readString();
        person = in.readString();
        place = in.readString();
        comment = in.readString();
        date = in.readLong();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(address);
        dest.writeString(content);
        dest.writeString(person);
        dest.writeString(place);
        dest.writeString(comment);
        dest.writeLong(date);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SmsTradeData> CREATOR = new Creator<SmsTradeData>() {
        @Override
        public SmsTradeData createFromParcel(Parcel in) {
            return new SmsTradeData(in);
        }

        @Override
        public SmsTradeData[] newArray(int size) {
            return new SmsTradeData[size];
        }
    };

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }


    public String getAddress() {
        return address;
    }


    @Override
    public long getId() {
        return id;
    }


    @Override
    public int getType() {

        return 6;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
