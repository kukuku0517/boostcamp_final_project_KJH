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
 * Created by samsung on 2017-07-26.
 */

public class PhotoData extends RealmObject implements MyRealmCommentableObject ,MyRealmShareableObject,MyRealmGpsObject {
    @PrimaryKey
    private long id;

    private long date;
    private double lat, lng;
    String place, comment,originId;
    private boolean highlight = false;
    private long photoGroupId;
    private boolean share;

   List<String> friend=new ArrayList<>();

    public List<String> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<String> friend) {
        this.friend = friend;
    }
    @Override
    public boolean isShare() {
        return share;
    }

    @Override
    public void setShare(boolean share) {
        this.share=share;
    }

    public long getPhotoGroupId() {
        return photoGroupId;
    }

    public void setPhotoGroupId(long photoGroupId) {
        this.photoGroupId = photoGroupId;
    }
    @Override
    public boolean isHighlight() {
        return highlight;
    }

    @Override
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public PhotoData() {

    }

    public String getComment() {
        return comment;
    }

    public String getPlace() {
        return place;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
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
        return RealmClassHelper.PHOTO_DATA;
    }

}
