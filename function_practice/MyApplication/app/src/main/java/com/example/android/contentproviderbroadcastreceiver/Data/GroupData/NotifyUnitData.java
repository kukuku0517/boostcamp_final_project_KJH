package com.example.android.contentproviderbroadcastreceiver.Data.GroupData;

import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.NotifyData;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-31.
 */

public class NotifyUnitData extends RealmObject implements MyRealmObject{
   int count;
    public  long start,end;
    public String name;
    @PrimaryKey
    long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
String content="";
    public String getContent() {
        return content;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public long getDate() {
        return start;
    }

    @Override
    public long getId() {
        return id;
    }
}