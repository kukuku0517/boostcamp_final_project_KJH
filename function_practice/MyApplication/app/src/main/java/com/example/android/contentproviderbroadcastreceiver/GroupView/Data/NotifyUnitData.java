package com.example.android.contentproviderbroadcastreceiver.GroupView.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.NotifyData;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-31.
 */

public class NotifyUnitData extends RealmObject implements MyRealmObject {

    @PrimaryKey
    private long id;
    private int count;
    private long start, end;
    private String name, comment;
    private RealmList<NotifyData> notifys;
    private boolean highlight = false;
    private long notifyGroupId;

    public long getNotifyGroupId() {
        return notifyGroupId;
    }

    public void setNotifyGroupId(long notifyGroupId) {
        this.notifyGroupId = notifyGroupId;
    }

    @Override
    public void setHighlight() {
        highlight = !highlight;
    }

    @Override
    public boolean getHighlight() {
        return highlight;
    }
    public NotifyUnitData() {
    }

    public String getComment() {
        return comment;
    }

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
        this.start = this.start > start ? start : this.start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = this.end < end ? end : this.end;
    }

    public RealmList<NotifyData> getNotifys() {
        return notifys;
    }

    public void setNotifys(RealmList<NotifyData> notifys) {
        this.notifys = notifys;
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

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }
}
