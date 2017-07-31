package com.example.android.contentproviderbroadcastreceiver.Data.GroupData;

import android.util.Log;

import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by samsung on 2017-07-31.
 */

public class NotifyGroupData extends RealmObject implements MyRealmObject{
   public RealmList<NotifyUnitData> units;
    long start,end;

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
        return 3;
    }

    @Override
    public long getDate() {
        return getEnd();
    }
}
