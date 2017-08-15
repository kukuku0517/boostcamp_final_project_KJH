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

/**
 * Created by samsung on 2017-08-15.
 */
@Parcel
public class GpsGroupDTO implements CommentableDTO, ShareableDTO, GpsableDTO {


    long id;

    long start = -1, end = -1;
    String comment, place;
    boolean highlight = false;
    long endId = -1, startId = -1;
    String originId;
    ArrayList<String> friend=new ArrayList<>();

    public ArrayList<String> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<String> friend) {
        this.friend = friend;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }


    public GpsGroupDTO(GpsGroupData data) {
        this.id = data.getId();
        this.start = data.getStart();
        this.end = data.getEnd();
        this.comment = data.getComment();
        this.place = data.getPlace();
        this.highlight = data.isHighlight();
        this.endId = data.getEndId();
        this.startId = data.getStartId();
        this.lat = data.getLat();
        this.lng = data.getLng();
        this.share = data.isShare();
        this.friend=data.getFriend();
        for (GpsData d : data.getGpsDatas()) {
            this.gpsDatas.add(new GpsDTO(d));
        }
    }

    double lat, lng;

    boolean share;

    public GpsGroupDTO() {

    }

    @Override
    public boolean isShare() {
        return share;
    }

    @Override
    public void setShare(boolean share) {
        this.share = share;
    }

    ArrayList<GpsDTO> gpsDatas = new ArrayList<>();

    public ArrayList<GpsDTO> getGpsDatas() {
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
    public boolean isHighlight() {
        return highlight;
    }

    @Override
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    @Override
    public int getType() {
        return RealmClassHelper.GPS_GROUP_DATA;
    }

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
