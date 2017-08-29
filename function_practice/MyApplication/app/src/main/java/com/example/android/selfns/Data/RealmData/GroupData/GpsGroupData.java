package com.example.android.selfns.Data.RealmData.GroupData;

import com.example.android.selfns.Data.DTO.Detail.GpsDTO;
import com.example.android.selfns.Data.DTO.Group.GpsGroupDTO;
import com.example.android.selfns.Data.RealmData.UnitData.GpsData;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmCommentableObject;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmGpsObject;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmShareableObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-11.
 */

public class GpsGroupData extends RealmObject implements MyRealmCommentableObject, MyRealmShareableObject, MyRealmGpsObject {
    public GpsGroupData() {

    }

    public GpsGroupData(GpsGroupDTO data) {
        this._id=data.get_id(); this.id = data.getId();
        this.start = data.getStart();
        this.end = data.getEnd();
        this.comment = data.getComment();
        this.place = data.getPlace();

        this.endId = data.getEndId();
        this.startId = data.getStartId();
        this.lat = data.getLat();
        this.lng = data.getLng();
        this.highlight = data.getHighlight();
        this.share = data.getShare();
        this.friends = data.getFriends();
        for (GpsDTO d : data.getGpsDatas()) {
            this.gpsDatas.add(new GpsData(d));
        }
    }


    @PrimaryKey
    private long id;

    long _id;

    @Override
    public long get_id() {
        return _id;
    }

    @Override
    public void set_id(long _id) {
        this._id = _id;
    }
    private long start = -1, end = -1;
    private String comment, place;
    private long endId = -1, startId = -1;
    private double lat, lng;

    String originId;
    String friends = "[]";
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

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getFriends() {
        return friends;
    }

    @Override
    public void setFriends(String friends) {
        this.friends = friends;
    }

    String fid;

    @Override
    public String getFid() {
        return fid;
    }

    @Override
    public void setFid(String fid) {
        this.fid = fid;
    }


    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    private RealmList<GpsData> gpsDatas;

    public RealmList<GpsData> getGpsDatas() {
        return gpsDatas;
    }

    public void setGpsDatas(RealmList<GpsData> gpsDatas) {
        this.gpsDatas = gpsDatas;
    }

    public boolean isStart() {
        if (start > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }



    @Override
    public int getType() {
        return type;
    }

    int type=RealmClassHelper.GPS_GROUP_DATA;
    @Override
    public long getDate() {
        if (isStart()) {
            return start;
        } else {
            return end;
        }
    }

    @Override
    public long getId() {
        return id;
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

    public long getEndId() {
        return endId;
    }

    public void setEndId(long endId) {
        this.endId = endId;
    }

    public long getStartId() {
        return startId;
    }

    public void setStartId(long startId) {
        this.startId = startId;
    }

    @Override
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


}
