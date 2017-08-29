package com.example.android.selfns.Data.RealmData.GroupData;

import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.DTO.Group.PhotoGroupDTO;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmCommentableObject;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmGpsObject;
import com.example.android.selfns.Data.RealmData.UnitData.PhotoData;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmShareableObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-03.
 */

public class PhotoGroupData extends RealmObject implements MyRealmCommentableObject, MyRealmShareableObject, MyRealmGpsObject {
    public PhotoGroupData(PhotoGroupDTO data) {
        this._id=data.get_id();   this.id = data.getId();
        this.count = data.getCount();
        this.start = data.getStart();
        this.end = data.getEnd();
        this.place = data.getPlace();
        this.comment = data.getComment();

        for (PhotoDTO d : data.getPhotoss()) {
            this.photoss.add(new PhotoData(d));
        }
        this.highlight = data.getHighlight();
        this.share = data.getShare();
        this.friends=data.getFriends();
    }

    @PrimaryKey
    private long id;
    private int count;
    private long start, end;
    private String place, comment;
    private RealmList<PhotoData> photoss=new RealmList<>();
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

    public PhotoGroupData() {

    }



    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    @Override
    public double getLat() {

        return getPhotoss().get(0).getLat();
    }

    @Override
    public double getLng() {
        return getPhotoss().get(0).getLng();
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public RealmList<PhotoData> getPhotoss() {
        return photoss;
    }

    public void setPhotoss(RealmList<PhotoData> photoss) {
        this.photoss = photoss;
    }

    public String getComment() {
        return comment;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int getType() {
        return type;
    }

    int type=RealmClassHelper.PHOTO_GROUP_DATA;
    @Override
    public long getDate() {
        return start;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }


}
