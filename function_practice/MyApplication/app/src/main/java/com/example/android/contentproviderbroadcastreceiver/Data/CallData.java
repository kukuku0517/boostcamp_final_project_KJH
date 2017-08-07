package com.example.android.contentproviderbroadcastreceiver.Data;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-26.
 */

public class CallData extends RealmObject implements MyRealmParcelableObject {
    @PrimaryKey
    private long id;

    private int callState;
    private long date, duration;
    private String person, number, comment;

    public CallData() {

    }

    protected CallData(Parcel in) {
        id = in.readLong();
        callState = in.readInt();
        date = in.readLong();
        duration = in.readLong();
        person = in.readString();
        number = in.readString();
        comment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(callState);
        dest.writeLong(date);
        dest.writeLong(duration);
        dest.writeString(person);
        dest.writeString(number);
        dest.writeString(comment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CallData> CREATOR = new Creator<CallData>() {
        @Override
        public CallData createFromParcel(Parcel in) {
            return new CallData(in);
        }

        @Override
        public CallData[] newArray(int size) {
            return new CallData[size];
        }
    };

    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCallState() {
        return callState;
    }

    public void setCallState(int callState) {
        this.callState = callState;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getDate() {
        return date;
    }

    @Override
    public long getId() {
        return id;
    }


    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    @Override
    public int getType() {
        return 0;
    }
}
