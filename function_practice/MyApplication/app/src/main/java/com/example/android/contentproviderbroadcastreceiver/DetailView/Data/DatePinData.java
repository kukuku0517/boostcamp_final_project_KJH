package com.example.android.contentproviderbroadcastreceiver.DetailView.Data;

import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;

/**
 * Created by samsung on 2017-08-09.
 */

public class DatePinData implements MyRealmObject {

    private long date;

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int getType() {
        return 7;
    }

    @Override
    public long getDate() {
        return date;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setComment(String comment) {

    }

    @Override
    public void setHighlight() {

    }

    @Override
    public boolean getHighlight() {
        return false;
    }

    @Override
    public String select() {
        return null;
    }
}
