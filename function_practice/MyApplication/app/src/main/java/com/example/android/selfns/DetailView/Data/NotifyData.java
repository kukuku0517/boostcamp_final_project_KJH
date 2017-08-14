package com.example.android.selfns.DetailView.Data;

import com.example.android.selfns.Interface.MyRealmObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-28.
 */

public class NotifyData extends RealmObject implements MyRealmObject {
    @PrimaryKey
    private   long  id;

    private  long date;
    private String content,person;

    private long notifyUnitId;

    public long getNotifyUnitId() {
        return notifyUnitId;
    }

    public void setNotifyUnitId(long notifyUnitId) {
        this.notifyUnitId = notifyUnitId;
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

    public long  getId() {
        return id;
    }


    @Override
    public int getType() {
        return 0;
    }

}
