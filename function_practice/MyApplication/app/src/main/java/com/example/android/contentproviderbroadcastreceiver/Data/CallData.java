package com.example.android.contentproviderbroadcastreceiver.Data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-26.
 */

public class CallData extends RealmObject implements MyRealmObject {
    @PrimaryKey
    long id;

    public long date;
    public String duration;
    public String person;

    public int a;

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

    @Override
    public long getId() {
        return id;
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
