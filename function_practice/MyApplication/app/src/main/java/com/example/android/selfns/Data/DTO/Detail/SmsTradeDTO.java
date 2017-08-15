package com.example.android.selfns.Data.DTO.Detail;
import com.example.android.selfns.Data.DTO.interfaceDTO.CommentableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.GpsableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Data.RealmData.UnitData.SmsTradeData;
import com.example.android.selfns.Helper.RealmClassHelper;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-15.
 */
@Parcel
public class SmsTradeDTO implements CommentableDTO, ShareableDTO, GpsableDTO {

    public SmsTradeDTO() {

    }

    String originId,address, content,person,place,comment;
    long date;
    double lat, lng;

    long id;

    boolean highlight = false;
    boolean share;
    ArrayList<String> friend=new ArrayList<>();

    public ArrayList<String> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<String> friend) {
        this.friend = friend;
    }
    public SmsTradeDTO(SmsTradeData data) {
        this.id = data.getId();
        this.address = data.getAddress();
        this.content = data.getContent();
        this.person = data.getPerson();
        this.place = data.getPlace();
        this.comment = data.getComment();
        this.highlight = data.isHighlight();
        this.share = data.isShare();
        this.originId = data.getOriginId();
        this.date = data.getDate();
        this.lat = data.getLat();
        this.lng = data.getLng();
        this.friend=data.getFriend();
    }


    @Override
    public boolean isShare() {
        return share;
    }

    @Override
    public void setShare(boolean share) {
        this.share = share;
    }

    @Override
    public boolean isHighlight() {
        return highlight;
    }

    @Override
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

        return RealmClassHelper.SMS_TRADE_DATA;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
