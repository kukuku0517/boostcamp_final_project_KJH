package com.example.android.selfns.Data.DTO.Group;
import com.example.android.selfns.Data.DTO.Detail.NotifyDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.CommentableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Data.RealmData.GroupData.NotifyUnitData;
import com.example.android.selfns.Data.RealmData.UnitData.NotifyData;
import com.example.android.selfns.Helper.RealmClassHelper;

import org.parceler.Parcel;

import java.util.ArrayList;

import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-15.
 */

@Parcel
public class NotifyUnitDTO implements CommentableDTO{

    private long id;
    private int count;
    private long start, end;
    private String name, comment;
    private ArrayList<NotifyDTO> notifys=new ArrayList<>();;
    private boolean highlight = false;
    private long notifyGroupId;
    private boolean share;


    public long getNotifyGroupId() {
        return notifyGroupId;
    }

    public void setNotifyGroupId(long notifyGroupId) {
        this.notifyGroupId = notifyGroupId;
    }
    @Override
    public boolean isHighlight() {
        return highlight;
    }

    public NotifyUnitDTO(NotifyUnitData data) {
        this.id = data.getId();
        this.count = data.getCount();
        this.start = data.getStart();
        this.end = data.getEnd();
        this.name = data.getName();
        this.comment = data.getComment();

        for(NotifyData d:data.getNotifys()){
            this.notifys.add(new NotifyDTO(d));
        }
        this.highlight = data.isHighlight();
        this.notifyGroupId = data.getNotifyGroupId();

    }

    @Override
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public NotifyUnitDTO() {
    }

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

    public ArrayList<NotifyDTO> getNotifys() {
        return notifys;
    }

    public void setNotifys(ArrayList<NotifyDTO> notifys) {
        this.notifys = notifys;
    }

    @Override
    public int getType() {
        return RealmClassHelper.NOTIFY_UNIT_DATA;
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
