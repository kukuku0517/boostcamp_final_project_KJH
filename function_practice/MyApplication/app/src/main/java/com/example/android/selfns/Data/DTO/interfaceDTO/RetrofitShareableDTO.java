package com.example.android.selfns.Data.DTO.interfaceDTO;

import com.example.android.selfns.Data.DTO.Detail.GpsDTO;
import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-22.
 */

public class RetrofitShareableDTO {
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    long _id;
    int highlight;
    int share;
    String friends;
    int type;
    long timestamp;
    String comment;
    long date;
    String person;
    String title;
    String tag;
    double lat;
    double lng;
    String place;
    long photoGroupId;
    String path;
    String originId;

    int _change;
    int moveState;

    public int get_change() {
        return _change;
    }

    public void set_change(int _change) {
        this._change = _change;
    }

    public int getMoveState() {
        return moveState;
    }

    public void setMoveState(int moveState) {
        this.moveState = moveState;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    String address;
    long start;
    long end;
    int endId;
    int startId;
    String content;
    int count;

    ArrayList<GpsDTO> gpsDatas;

    public ArrayList<GpsDTO> getGpsDatas() {
        return gpsDatas;
    }

    public void setGpsDatas(ArrayList<GpsDTO> gpsDatas) {
        this.gpsDatas = gpsDatas;
    }

    public ArrayList<PhotoDTO> getPhotoss() {
        return photoss;
    }

    public void setPhotoss(ArrayList<PhotoDTO> photoss) {
        this.photoss = photoss;
    }

    ArrayList<PhotoDTO> photoss;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public int getHighlight() {
        return highlight;
    }

    public void setHighlight(int highlight) {
        this.highlight = highlight;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public void setPlace(String place) {
        this.place = place;
    }

    public long getPhotoGroupId() {
        return photoGroupId;
    }

    public void setPhotoGroupId(long photoGroupId) {
        this.photoGroupId = photoGroupId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getEndId() {
        return endId;
    }

    public void setEndId(int endId) {
        this.endId = endId;
    }

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
