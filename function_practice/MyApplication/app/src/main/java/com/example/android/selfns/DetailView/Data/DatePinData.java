package com.example.android.selfns.DetailView.Data;

import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Interface.MyRealmObject;

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
        return RealmClassHelper.DATE_PIN_DATA;
    }

    @Override
    public long getDate() {
        return date;
    }

    @Override
    public long getId() {
        return 0;
    }

}
