package com.example.android.selfns.DetailView.Data;

import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Interface.MyRealmCommentableObject;
import com.example.android.selfns.Interface.MyRealmGpsObject;
import com.example.android.selfns.Interface.MyRealmShareableObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-04.
 */
public class SmsTradeData extends RealmObject implements MyRealmCommentableObject,MyRealmShareableObject,MyRealmGpsObject {
    @PrimaryKey
    long id;



    private String address;
    private String content;
    private String person;
    private String place;
    private String comment;
    private boolean highlight = false;
    private boolean share;


    @Override
    public boolean isShare() {
        return share;
    }

    @Override
    public void setShare(boolean share) {
        this.share=share;
    }
    @Override
    public void setHighlight() {
        highlight = !highlight;
    }

    @Override
    public boolean getHighlight() {
        return highlight;
    }
    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    private String originId;
    private long date;
    private double lat, lng;

    public SmsTradeData() {

    }
    public void setId(long id) {
        this.id = id;
    }

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

        return RealmClassHelper.SMS_TRADE_DATA;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}