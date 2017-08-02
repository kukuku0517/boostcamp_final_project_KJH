package com.example.android.contentproviderbroadcastreceiver.Data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by samsung on 2017-07-26.
 */

public class SmsData extends RealmObject implements MyRealmObject {
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
    @PrimaryKey
    long id;

    public long date;
    public String content;
    public String person;

    public String getAddress() {
        return address;
    }

    public String address;

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
