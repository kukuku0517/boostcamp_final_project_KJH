package com.example.android.selfns.GroupView.Data;

import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Interface.MyRealmCommentableObject;
import com.example.android.selfns.Interface.MyRealmGpsObject;
import com.example.android.selfns.Interface.MyRealmObject;
import com.example.android.selfns.DetailView.Data.SmsData;
import com.example.android.selfns.Interface.MyRealmShareableObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-01.
 */

public class SmsUnitData extends RealmObject implements MyRealmCommentableObject{
    @PrimaryKey
    private long id;

    private int count;
    private long start, end;
    private String name;
    private String content = "";
    private String comment;
    private boolean highlight = false;
    private long smsGroupId;

    public long getSmsGroupId() {
        return smsGroupId;
    }

    public void setSmsGroupId(long smsGroupId) {
        this.smsGroupId = smsGroupId;
    }

    @Override
    public void setHighlight() {
        highlight = !highlight;
    }

    @Override
    public boolean getHighlight() {
        return highlight;
    }
    public SmsUnitData(){

    }
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAddress() {
        return address;
    }

    public String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return RealmClassHelper.SMS_UNIT_DATA;
    }

    @Override
    public long getDate() {
        return start;
    }

    @Override
    public long getId() {
        return id;
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

    public RealmList<SmsData> getSmss() {
        return smss;
    }

    public void setSmss(RealmList<SmsData> smss) {
        this.smss = smss;
    }

    RealmList<SmsData> smss;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

}