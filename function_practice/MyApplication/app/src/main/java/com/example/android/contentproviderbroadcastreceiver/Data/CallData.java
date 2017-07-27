package com.example.android.contentproviderbroadcastreceiver.Data;

import io.realm.RealmObject;

/**
 * Created by samsung on 2017-07-26.
 */

public class CallData extends RealmObject implements MyRealmObject {

    public long date;
    public String duration;
    public String person;
    public long id;
    public int a;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;}
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String number;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
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
        return 0;
    }
}
