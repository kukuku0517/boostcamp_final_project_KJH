package com.example.android.contentproviderbroadcastreceiver.Data.GroupData;

import com.example.android.contentproviderbroadcastreceiver.Data.NotifyData;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by samsung on 2017-07-31.
 */

public class NotifyUnitData extends RealmObject {
    public   int id,count;
    public  long start,end;
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = this.start>start?start:this.start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = this.end<end?end:this.end;
    }

    public RealmList<NotifyData> getNotifys() {
        return notifys;
    }

    public void setNotifys(RealmList<NotifyData> notifys) {
        this.notifys = notifys;
    }

    RealmList<NotifyData> notifys;
}
