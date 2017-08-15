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
    boolean highlight = false;
    boolean share = false;
    ArrayList<String> friend=new ArrayList<>();

    public ArrayList<String> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<String> friend) {
        this.friend = friend;
    }
    public CallDTO() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public CallDTO(CallData data) {
        this.id = data.getId();
        this.callState = data.getCallState();
        this.date = data.getDate();
        this.duration = data.getDuration();
        this.person = data.getPerson();
        this.number = data.getNumber();
        this.comment = data.getComment();
        this.highlight = data.isHighlight();
        this.share = data.isShare();
        this.friend=data.getFriend();
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
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
        return RealmClassHelper.CALL_DATA;
    }
}
