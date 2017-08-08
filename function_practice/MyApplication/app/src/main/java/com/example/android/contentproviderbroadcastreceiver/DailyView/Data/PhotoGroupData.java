package com.example.android.contentproviderbroadcastreceiver.DailyView.Data;

import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.PhotoData;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-03.
 */

public class PhotoGroupData extends RealmObject implements MyRealmObject {
    @PrimaryKey
    private long id;
    private int count;
    private long start, end;
    private String place,comment;
    private RealmList<PhotoData> photoss;
    private String content = "";
    private boolean highlight = false;

    @Override
    public void setHighlight() {
        highlight = !highlight;
    }

    @Override
    public boolean getHighlight() {
        return highlight;
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

    @Override
    public void setComment(String comment) {
this.comment = comment;
    }
}
