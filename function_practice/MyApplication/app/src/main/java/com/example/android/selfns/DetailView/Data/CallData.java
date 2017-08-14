package com.example.android.selfns.DetailView.Data;

import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Interface.MyRealmCommentableObject;
import com.example.android.selfns.Interface.MyRealmObject;
import com.example.android.selfns.Interface.MyRealmShareableObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-26.
 */

public class CallData extends RealmObject implements MyRealmCommentableObject,MyRealmShareableObject {
    @PrimaryKey
    private long id;

    private int callState;
    private long date, duration;
    private String person, number, comment;
    private boolean highlight = false;
    private boolean share=false;

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public CallData() {

    }
    @Override
    public void setHighlight() {
        highlight = !highlight;
    }

    @Override
    public boolean getHighlight() {
        return highlight;
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
        return RealmClassHelper.CALL_DATA;
    }

}
