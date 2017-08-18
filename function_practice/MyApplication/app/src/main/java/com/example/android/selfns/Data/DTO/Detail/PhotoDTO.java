package com.example.android.selfns.Data.DTO.Detail;

import com.example.android.selfns.Data.DTO.interfaceDTO.CommentableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.GpsableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Data.RealmData.UnitData.PhotoData;
import com.example.android.selfns.Helper.RealmClassHelper;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-15.
 */
@Parcel
public class PhotoDTO implements CommentableDTO, ShareableDTO, GpsableDTO {

    long id;

    long date;
    double lat, lng;
    String place, comment, originId;
    boolean highlight = false;
    long photoGroupId;
    boolean share;
    String friends = "[]";

    @Override
    public String getFriends() {
        return friends;
    }

    @Override
    public void setFriends(String friends) {
        this.friends = friends;
    }


    public PhotoDTO() {

    }

    public PhotoDTO(PhotoData data) {
        this.id = data.getId();
        this.date = data.getDate();
        this.lat = data.getLat();
        this.lng = data.getLng();
        this.place = data.getPlace();
        this.comment = data.getComment();
        this.highlight = data.isHighlight();
        this.photoGroupId = data.getPhotoGroupId();
        this.share = data.isShare();
        this.path = data.getPath();
        this.originId = data.getOriginId();
        this.friends = data.getFriends();
    }


    @Override
    public boolean isShare() {
        return share;
    }

    @Override
    public void setShare(boolean share) {
        this.share = share;
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


    public String getComment() {
        return comment;
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
