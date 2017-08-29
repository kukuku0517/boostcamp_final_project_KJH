package com.example.android.selfns.Data.RealmData.UnitData;

import com.example.android.selfns.Data.DTO.Detail.CallDTO;
import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmCommentableObject;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmShareableObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-26.
 */

public class CallData extends RealmObject implements MyRealmCommentableObject, MyRealmShareableObject {
    public void setId(long id) {
        this.id = id;
    }

    public CallData(CallDTO data) {
        this._id=data.get_id(); this.id = data.getId();
        this.callState = data.getCallState();
        this.date = data.getDate();
        this.duration = data.getDuration();
        this.person = data.getPerson();
        this.number = data.getNumber();
        this.comment = data.getComment();
        this.highlight = data.getHighlight();
        this.share = data.getShare();
        this.friends = data.getFriends();
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

    private int callState;
    private long date, duration;
    private String person, number, comment;

    String friends = "[]";
    String fid;
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



    public CallData() {

    }

    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCallState() {
        return callState;
    }

    public void setCallState(int callState) {
        this.callState = callState;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getDate() {
        return date;
    }

    @Override
    public long getId() {
        return id;
    }


    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }


    @Override
    public int getType() {
        return type;
    }

    int type=RealmClassHelper.CALL_DATA;

}
