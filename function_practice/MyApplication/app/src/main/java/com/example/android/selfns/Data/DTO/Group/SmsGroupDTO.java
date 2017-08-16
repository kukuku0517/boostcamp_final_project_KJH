package com.example.android.selfns.Data.DTO.Group;
import android.util.Log;

import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.GroupData.SmsGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.SmsUnitData;
import com.example.android.selfns.Helper.RealmClassHelper;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-15.
 */

@Parcel
public class SmsGroupDTO implements BaseDTO {
    SmsGroupDTO() {

    }

    long id;

    ArrayList<SmsUnitDTO> units=new ArrayList<>();;
    long start, end;

    public SmsGroupDTO(SmsGroupData data) {
        this.id = data.getId();
        for (SmsUnitData d : data.getUnits()) {
            this.units.add(new SmsUnitDTO(d));
        }
        this.start = data.getStart();
        this.end = data.getEnd();
    }


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

    public ArrayList<SmsUnitDTO> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<SmsUnitDTO> units) {
        this.units = units;
    }

    //custom
    public int checkName(String s) {

        for (int i = 0; i < units.size(); i++) {
            Log.d("grouping", units.get(i).getName() + " : " + s);
            if (units.get(i).getName().equals(s)) {
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
