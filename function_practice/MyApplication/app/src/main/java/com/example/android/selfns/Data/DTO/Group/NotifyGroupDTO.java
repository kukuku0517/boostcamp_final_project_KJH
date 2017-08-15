package com.example.android.selfns.Data.DTO.Group;
import android.util.Log;

import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.GroupData.NotifyGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.NotifyUnitData;
import com.example.android.selfns.Helper.RealmClassHelper;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-15.
 */

@Parcel
public class NotifyGroupDTO implements BaseDTO {

    NotifyGroupDTO(){

    }
    long id;
    ArrayList<NotifyUnitDTO> units = new ArrayList<>();
    long start, end;


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

    public ArrayList<NotifyUnitDTO> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<NotifyUnitDTO> units) {
        this.units = units;
    }

    public int checkName(String s) {

        for (int i = 0; i < units.size(); i++) {
            Log.d("asdfasdf", ":::" + units.get(i).getName());
            if (units.get(i).getName() != null && units.get(i).getName().equals(s)) {
                return i;
            }
        }
        return -1;
    }

    public NotifyGroupDTO(NotifyGroupData data) {
        this.id = data.getId();

        for (NotifyUnitData d : data.getUnits()) {
            this.units.add(new NotifyUnitDTO(d));
        }
        this.start = data.getStart();
        this.end = data.getEnd();
    }

    public void setTime(long start, long end) {
        setStart(start);

        setEnd(end);
    }

    @Override
    public int getType() {
        return RealmClassHelper.NOTIFY_GROUP_DATA;
    }

    @Override
    public long getDate() {
        return end;
    }

    @Override
    public long getId() {
        return id;
    }

}
