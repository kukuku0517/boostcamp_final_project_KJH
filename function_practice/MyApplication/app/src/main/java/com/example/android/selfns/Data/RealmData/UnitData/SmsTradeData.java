package com.example.android.selfns.Data.RealmData.UnitData;

import com.example.android.selfns.Data.DTO.Detail.SmsTradeDTO;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmCommentableObject;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmGpsObject;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmShareableObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-04.
 */
public class SmsTradeData extends RealmObject implements MyRealmCommentableObject,MyRealmShareableObject,MyRealmGpsObject {

    public SmsTradeData(SmsTradeDTO data) {
        this._id=data.get_id();
        this.id = data.getId();
        this.address = data.getAddress();
        this.content = data.getContent();
        this.person = data.getPerson();
        this.place = data.getPlace();
        this.comment = data.getComment();
        this.highlight = data.getHighlight();
        this.share = data.getShare();
        this.originId = data.getOriginId();
        this.date = data.getDate();
        this.lat = data.getLat();
        this.lng = data.getLng();
        this.friends=data.getFriends();

    }

    @PrimaryKey
    long id;


    long _id;

    @Override
    public long get_id() {
        return _id;
    }

    @Override
    public void set_id(long _id) {
        this._id = _id;
    }

    private String address;
    private String content;
    private String person;
    private String place;
    private String comment;

    String friends="[]";
    String fid;
    long timestamp = 0;

    int highlight = 0;
    @Override
    public int getHighlight() {
        return highlight;
    }
    @Override
    public void setHighlight(int highlight) {
        this.highlight = highlight;
    }
    @Override
    public int getShare() {
        return share;
    }
    @Override
    public void setShare(int share) {
        this.share = share;
    }

    int  share = 0;
    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public String getFid() {
        return fid;
    }

    @Override
    public void setFid(String fid) {
        this.fid = fid;
    }


    @Override
    public String getFriends() {
        return friends;
    }

    @Override
    public void setFriends(String friends) {
        this.friends = friends;
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
        return type;
    }

    int type=RealmClassHelper.SMS_TRADE_DATA;
    public void setAddress(String address) {
        this.address = address;
    }

}
