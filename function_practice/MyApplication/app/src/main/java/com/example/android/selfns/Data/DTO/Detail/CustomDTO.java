package com.example.android.selfns.Data.DTO.Detail;

import com.example.android.selfns.Data.DTO.interfaceDTO.CommentableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Data.RealmData.UnitData.CustomData;
import com.example.android.selfns.Helper.RealmClassHelper;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-15.
 */
@Parcel
public class CustomDTO implements CommentableDTO, ShareableDTO {

    long id;
    long date;
    String title, comment, tag;
    double lat, lng;
    String place;
    boolean share;
    boolean highlight = false;
    ArrayList<String> friend=new ArrayList<>();

    public ArrayList<String> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<String> friend) {
        this.friend = friend;
    }
    public CustomDTO() {

    }

    public CustomDTO(CustomData data) {
        this.id = data.getId();
        this.date = data.getDate();
        this.title = data.getTitle();
        this.comment = data.getComment();
        this.tag = data.getTag();
        this.lat = data.getLat();
        this.lng = data.getLng();
        this.place = data.getPlace();
        this.share = data.isShare();
        this.highlight = data.isHighlight();
        this.friend=data.getFriend();
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean isShare() {
        return share;
    }

    @Override
    public void setShare(boolean share) {
        this.share = share;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public int getType() {
        return RealmClassHelper.CUSTOM_DATA;
    }

    @Override
    public long getDate() {
        return date;
    }

    @Override
    public long getId() {
        return 0;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}
