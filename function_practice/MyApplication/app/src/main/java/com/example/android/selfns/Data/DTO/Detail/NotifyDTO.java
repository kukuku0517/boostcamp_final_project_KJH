package com.example.android.selfns.Data.DTO.Detail;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.UnitData.NotifyData;

import org.parceler.Parcel;

/**
 * Created by samsung on 2017-08-15.
 */

@Parcel
public class NotifyDTO implements BaseDTO {
public NotifyDTO(){

}
  long  id;
    long date;
  String content,person;
long notifyUnitId;

    public NotifyDTO(NotifyData data) {
        this.id = data.getId();
        this.date = data.getDate();
        this.content = data.getContent();
        this.person = data.getPerson();
        this.notifyUnitId = data.getNotifyUnitId();
    }

    public long getNotifyUnitId() {
        return notifyUnitId;
    }

    public void setNotifyUnitId(long notifyUnitId) {
        this.notifyUnitId = notifyUnitId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public long  getId() {
        return id;
    }

    @Override
    public int getType() {
        return 0;
    }

}
