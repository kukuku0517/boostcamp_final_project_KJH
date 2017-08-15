package com.example.android.selfns.Data.RealmData.GroupData;

import android.util.Log;

import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-01.
 */

public class SmsGroupData extends RealmObject implements MyRealmObject {
    @PrimaryKey
    private  long id;

    private  RealmList<SmsUnitData> units;
    private long start,end;



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

    public RealmList<SmsUnitData> getUnits() {
        return units;
    }

    public void setUnits(RealmList<SmsUnitData> units) {
        this.units = units;
    }

    //custom
    public int checkName(String s){

        for(int i=0;i<units.size();i++){
            Log.d("grouping",units.get(i).getName() + " : "+s);
            if(units.get(i).getName().equals(s)){
                return i;
            }
        }
        return -1;
    }

    public void setTime(long start, long end){
        setStart(start);
        setEnd(end);
    }

    @Override
    public int getType() {
        return RealmClassHelper.SMS_GROUP_DATA;
    }

    @Override
    public long getDate() {
        return getEnd();
    }

    @Override
    public long getId() {
        return id;
    }
}
