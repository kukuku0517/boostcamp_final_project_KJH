package com.example.android.contentproviderbroadcastreceiver.Data.GroupData;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmParcelableObject;
import com.example.android.contentproviderbroadcastreceiver.Data.NotifyData;
import com.example.android.contentproviderbroadcastreceiver.Data.SmsData;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-01.
 */

public class SmsUnitData extends RealmObject implements MyRealmParcelableObject {
    @PrimaryKey
    private long id;

    private int count;
    private long start, end;
    private String name;
    private String content = "";
    private String comment;

    public SmsUnitData(){

    }
    protected SmsUnitData(Parcel in) {
        id = in.readLong();
        count = in.readInt();
        start = in.readLong();
        end = in.readLong();
        name = in.readString();
        content = in.readString();
        comment = in.readString();
        address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(count);
        dest.writeLong(start);
        dest.writeLong(end);
        dest.writeString(name);
        dest.writeString(content);
        dest.writeString(comment);
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SmsUnitData> CREATOR = new Creator<SmsUnitData>() {
        @Override
        public SmsUnitData createFromParcel(Parcel in) {
            return new SmsUnitData(in);
        }

        @Override
        public SmsUnitData[] newArray(int size) {
            return new SmsUnitData[size];
        }
    };

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