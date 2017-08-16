package com.example.android.selfns.Data.RealmData.UnitData;

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
    @PrimaryKey
    private long id;

    private long date;
    private String title, comment, tag;
    double lat, lng;
    private String place;
    private boolean share;
    String originId;
    String friends="[]";

    @Override
    public String getFriends() {
        return friends;
    }

    @Override
    public void setFriends(String friends) {
        this.friends = friends;
    }



    @Override
    public boolean isShare() {
        return share;
    }

    @Override
    public void setShare(boolean share) {
        this.share = share;
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

    private boolean highlight = false;

    @Override
    public int getType() {
        return RealmClassHelper.CUSTOM_DATA;
    }

    @Override
    public long getDate() {
        return date;
    }

    @Override
    public long getId() {
        return 0;
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

    @Override
    public boolean isHighlight() {
        return highlight;
    }

    @Override
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}
