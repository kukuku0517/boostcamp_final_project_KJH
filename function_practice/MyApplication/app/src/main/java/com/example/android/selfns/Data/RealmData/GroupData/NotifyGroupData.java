package com.example.android.selfns.Data.RealmData.GroupData;

import android.util.Log;

import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-07-31.
 */

public class NotifyGroupData extends RealmObject implements MyRealmObject {

    @PrimaryKey
    private long id;
    private RealmList<NotifyUnitData> units;
    private long start, end;


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

    public RealmList<NotifyUnitData> getUnits() {
        return units;
    }

    public void setUnits(RealmList<NotifyUnitData> units) {
        this.units = units;
    }

    //custom
    public int checkName(String s) {

        for (int i = 0; i < units.size(); i++) {
            Log.d("asdfasdf", ":::" + units.get(i).getName());
            if (units.get(i).getName() != null && units.get(i).getName().equals(s)) {
                return i;
            }
        }
        return -1;
    }

    public void setTime(long start, long end) {
        setStart(start);
        setEnd(end);
    }

    @Override
    public int getType() {
        return type;
    }

    int type=RealmClassHelper.NOTIFY_GROUP_DATA;
    @Override
    public long getDate() {
        return end;
    }

    @Override
    public long getId() {
        return id;
    }


}
