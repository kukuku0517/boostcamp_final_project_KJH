package com.example.android.contentproviderbroadcastreceiver.Data.GroupData;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmParcelableObject;
import com.example.android.contentproviderbroadcastreceiver.Data.NotifyData;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-31.
 */

public class NotifyUnitData extends RealmObject implements MyRealmParcelableObject {

    @PrimaryKey
    private long id;
    private int count;
    private long start, end;
    private String name, comment;
    private RealmList<NotifyData> notifys;

    public NotifyUnitData() {
    }

    protected NotifyUnitData(Parcel in) {
        id = in.readLong();
        count = in.readInt();
        start = in.readLong();
        end = in.readLong();
        name = in.readString();
        comment = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(count);
        dest.writeLong(start);
        dest.writeLong(end);
        dest.writeString(name);
        dest.writeString(comment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotifyUnitData> CREATOR = new Creator<NotifyUnitData>() {
        @Override
        public NotifyUnitData createFromParcel(Parcel in) {
            return new NotifyUnitData(in);
        }

        @Override
        public NotifyUnitData[] newArray(int size) {
            return new NotifyUnitData[size];
        }
    };

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
