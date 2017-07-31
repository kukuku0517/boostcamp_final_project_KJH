package com.example.android.contentproviderbroadcastreceiver.Data;

import io.realm.RealmObject;

/**
 * Created by samsung on 2017-07-28.
 */

public class NotifyData extends RealmObject implements MyRealmObject {
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

    public long date;
    public String content,person;
    public long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;}

    @Override
    public int getType() {
        return 3;
    }
}
