package com.example.android.contentproviderbroadcastreceiver.Data;

import io.realm.RealmObject;
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

    public long date;
    public String content,person;


    @Override
    public int getType() {
        return 2;
    }
}
