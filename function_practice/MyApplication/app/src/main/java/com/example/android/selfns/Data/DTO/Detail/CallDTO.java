package com.example.android.selfns.Data.DTO.Detail;

import com.example.android.selfns.Data.DTO.interfaceDTO.CommentableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Data.RealmData.UnitData.CallData;
import com.example.android.selfns.Helper.RealmClassHelper;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-15.
 */

@Parcel
public class CallDTO implements CommentableDTO, ShareableDTO {

    long id;
    int callState;
    long date, duration;
    String person, number, comment;
    int highlight = 0;

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

    long _id;

    @Override
    public long get_id() {
        return _id;
    }

    @Override
    public void set_id(long _id) {
        this._id = _id;
    }
    int share = 0;
    String friends = "[]";
    String fid;
    long timestamp = 0;

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


    public CallDTO() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public CallDTO(CallData data) {
        this._id=data.get_id();
        this.id = data.getId();
        this.callState = data.getCallState();
        this.date = data.getDate();
        this.duration = data.getDuration();
        this.person = data.getPerson();
        this.number = data.getNumber();
        this.comment = data.getComment();
        this.highlight = data.getHighlight();
        this.share = data.getShare();
        this.friends = data.getFriends();
        this.fid = data.getFid();
        this.timestamp = data.getTimestamp();
    }


    public String getComment() {
        return comment;
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
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public int getType() {
        return type;
    }

    int type=RealmClassHelper.CALL_DATA;
}
