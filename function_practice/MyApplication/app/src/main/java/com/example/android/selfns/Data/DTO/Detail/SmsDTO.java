package com.example.android.selfns.Data.DTO.Detail;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.RealmData.UnitData.SmsData;
import org.parceler.Parcel;

/**
 * Created by samsung on 2017-08-15.
 */
@Parcel
public class SmsDTO implements BaseDTO {
 long id;

 String address, content, person;
 long date;

    public SmsDTO(){

    }
    public SmsDTO(SmsData data) {
        this.id = data.getId();
        this.address = data.getAddress();
        this.content = data.getContent();
        this.person = data.getPerson();
        this.date = data.getDate();
        this.isSent = data.isSent();
        this.smsUnitId = data.getSmsUnitId();
    }

   boolean isSent;
 long smsUnitId;

    public long getSmsUnitId() {
        return smsUnitId;
    }

    public void setSmsUnitId(long smsUnitId) {
        this.smsUnitId = smsUnitId;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
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


    public String getAddress() {
        return address;
    }


    @Override
    public long getId() {
        return id;
    }


    @Override
    public int getType() {

        return 1;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
