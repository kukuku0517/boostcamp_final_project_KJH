package com.example.android.selfns.Data.RealmData.UnitData;

import com.example.android.selfns.Data.DTO.Detail.CustomDTO;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmCommentableObject;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmGpsObject;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmShareableObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-12.
 */

public class CustomData extends RealmObject implements MyRealmCommentableObject, MyRealmShareableObject, MyRealmGpsObject {
    public CustomData() {

    }

    public CustomData(CustomDTO data) {this.id = data.getId();
        this._id=data.get_id();
        this.date = data.getDate();
        this.title = data.getTitle();
        this.comment = data.getComment();
        this.tag = data.getTag();
        this.lat = data.getLat();
        this.lng = data.getLng();
        this.place = data.getPlace();
        this.highlight = data.getHighlight();
        this.share = data.getShare();
        this.friends = data.getFriends();
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
    private long date;
    private String title, comment, tag;
    double lat, lng;
    private String place;

    String originId;
    String friends = "[]";
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

    int share = 0;

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


    public void setId(long id) {
        this.id = id;
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

    public String getPlace() {
        return place;
    }

    @Override
    public String getOriginId() {
        return originId;
    }

    @Override
    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public void setPlace(String place) {
        this.place = place;
    }


    @Override
    public int getType() {
        return type;
    }

    int type=RealmClassHelper.CUSTOM_DATA;
    @Override
    public long getDate() {
        return date;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}
