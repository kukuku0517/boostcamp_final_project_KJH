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

    String path;

    long id;
    long date;
    double lat, lng;
    String place, comment, originId;
    long photoGroupId;
    long _groupId;

    public void setId(long id) {
        this.id = id;
    }

    public long get_groupId() {
        return _groupId;
    }

    public void set_groupId(long _groupId) {
        this._groupId = _groupId;
    }

    public void setType(int type) {
        this.type = type;
    }

    String friends = "[]";
    String fid;
    long timestamp = 0;
    int highlight = 0;
    long _id;

    int share = 0;

    @Override
    public long get_id() {
        return _id;
    }

    @Override
    public void set_id(long _id) {
        this._id = _id;
    }

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


    public PhotoDTO() {

    }

    public PhotoDTO(PhotoData data) {
        this._id = data.get_id();
        this.id = data.getId();
        this.date = data.getDate();
        this.lat = data.getLat();
        this.lng = data.getLng();
        this.place = data.getPlace();
        this.comment = data.getComment();

        this.photoGroupId = data.getPhotoGroupId();
        this.highlight = data.getHighlight();
        this.share = data.getShare();
        this.path = data.getPath();
        this.originId = data.getOriginId();
        this.friends = data.getFriends();
        this.fid = data.getFid();
        this.timestamp = data.getTimestamp();
        this._groupId = data.get_groupId();
    }


    public long getPhotoGroupId() {
        return photoGroupId;
    }

    public void setPhotoGroupId(long photoGroupId) {
        this.photoGroupId = photoGroupId;
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


    @Override
    public int getType() {
        return type;
    }

    int type = RealmClassHelper.PHOTO_DATA;
}
