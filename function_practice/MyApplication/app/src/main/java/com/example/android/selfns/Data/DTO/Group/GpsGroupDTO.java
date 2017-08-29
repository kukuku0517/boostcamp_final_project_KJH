package com.example.android.selfns.Data.DTO.Group;

import com.example.android.selfns.Data.DTO.Detail.GpsDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.CommentableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.GpsableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Data.RealmData.GroupData.GpsGroupData;
import com.example.android.selfns.Data.RealmData.UnitData.GpsData;
import com.example.android.selfns.Helper.RealmClassHelper;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsung on 2017-08-15.
 */
@Parcel
public class GpsGroupDTO implements CommentableDTO, ShareableDTO, GpsableDTO {


    long id;

    long start = -1, end = -1;
    String comment, place;

    long endId = -1, startId = -1;
    String originId;
    String friends="[]";
    String fid;
    long timestamp = 0;

    long _id;

    @Override
    public long get_id() {
        return _id;
    }

    @Override
    public void set_id(long _id) {
        this._id = _id;
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


    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }


    public GpsGroupDTO(GpsGroupData data) {
        this._id=data.get_id(); this.id = data.getId();
        this.start = data.getStart();
        this.end = data.getEnd();
        this.comment = data.getComment();
        this.place = data.getPlace();
        this.highlight = data.getHighlight();
        this.share = data.getShare();
        this.endId = data.getEndId();
        this.startId = data.getStartId();
        this.lat = data.getLat();
        this.lng = data.getLng();

        this.friends = data.getFriends();   this.fid=data.getFid();
        this.timestamp=data.getTimestamp();
        for (GpsData d : data.getGpsDatas()) {
            this.gpsDatas.add(new GpsDTO(d));
        }
    }

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
    double lat, lng;

    public GpsGroupDTO() {

    }


    List<GpsDTO> gpsDatas = new ArrayList<>();

    public List<GpsDTO> getGpsDatas() {
        return gpsDatas;
    }

    public void setGpsDatas(ArrayList<GpsDTO> gpsDatas) {
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
