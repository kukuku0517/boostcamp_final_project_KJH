package com.example.android.selfns.DetailView.Data;

import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Interface.MyRealmCommentableObject;
import com.example.android.selfns.Interface.MyRealmObject;
import com.example.android.selfns.Interface.MyRealmShareableObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-26.
 */

public class PhotoData extends RealmObject implements MyRealmCommentableObject ,MyRealmShareableObject {
    @PrimaryKey
    private long id;

    private long date;
    private double lat, lng;
    String place, comment;
    private boolean highlight = false;
    private long photoGroupId;
    private boolean share;


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
    public void setHighlight() {
        highlight = !highlight;
    }

    @Override
    public boolean getHighlight() {
        return highlight;
    }
    public PhotoData() {

    }

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
        return RealmClassHelper.PHOTO_DATA;
    }

}
