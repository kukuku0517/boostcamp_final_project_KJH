package com.example.android.contentproviderbroadcastreceiver.Data.GroupData;

import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.NotifyData;
import com.example.android.contentproviderbroadcastreceiver.Data.PhotoData;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-03.
 */

public class PhotoGroupData extends RealmObject implements MyRealmObject {
    int count;
    public  long start,end;
    public String place;

    RealmList<PhotoData> photoss;
    String content="";
    @PrimaryKey
    long id;

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
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public RealmList<PhotoData> getPhotoss() {
        return photoss;
    }

    public void setPhotoss(RealmList<PhotoData> photoss) {
        this.photoss = photoss;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int getType() {
        return 2;
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
