package com.example.android.selfns.DetailView.Data;

import com.example.android.selfns.Interface.MyRealmObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-26.
 */

public class SmsData extends RealmObject implements MyRealmObject {
    @PrimaryKey
    private long id;

    private String address, content, person;
    private long date;
    private boolean isSent;
    private long smsUnitId;

    public long getSmsUnitId() {
        return smsUnitId;
    }

    public void setSmsUnitId(long smsUnitId) {
        this.smsUnitId = smsUnitId;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }


    public String getAddress() {
        return address;
    }


    @Override
    public long getId() {
        return id;
    }


    @Override
    public int getType() {


        return 1;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
