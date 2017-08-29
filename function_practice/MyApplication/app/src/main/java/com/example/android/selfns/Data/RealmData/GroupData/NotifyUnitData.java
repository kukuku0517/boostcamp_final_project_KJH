package com.example.android.selfns.Data.RealmData.GroupData;

import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmCommentableObject;
import com.example.android.selfns.Data.RealmData.UnitData.NotifyData;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmShareableObject;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-31.
 */

public class NotifyUnitData extends RealmObject implements MyRealmCommentableObject{

    @PrimaryKey
    private long id;
    private int count;
    private long start, end;
    private String name, comment;
    private RealmList<NotifyData> notifys;

    private long notifyGroupId;

    int highlight = 0;
    @Override
    public int getHighlight() {
        return highlight;
    }
    @Override
    public void setHighlight(int highlight) {
        this.highlight = highlight;
    }
    public long getNotifyGroupId() {
        return notifyGroupId;
    }

    public void setNotifyGroupId(long notifyGroupId) {
        this.notifyGroupId = notifyGroupId;
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
        return type;
    }

    int type=RealmClassHelper.NOTIFY_UNIT_DATA;
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
