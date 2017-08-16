package com.example.android.selfns.Data.DTO.Group;

import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.CommentableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.GpsableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Data.RealmData.GroupData.PhotoGroupData;
import com.example.android.selfns.Data.RealmData.UnitData.PhotoData;
import com.example.android.selfns.Helper.RealmClassHelper;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-15.
 */

@Parcel
public class PhotoGroupDTO implements CommentableDTO, ShareableDTO, GpsableDTO {
    public PhotoGroupDTO(PhotoGroupData data) {
        this.id = data.getId();
        this.count = data.getCount();
        this.start = data.getStart();
        this.end = data.getEnd();
        this.place = data.getPlace();
        this.comment = data.getComment();

        for (PhotoData d : data.getPhotoss()) {
            this.photoss.add(new PhotoDTO(d));
        }
        this.highlight = data.isHighlight();
        this.share = data.isShare();
        this.friends=data.getFriends();
    }


    long id;
    int count;
    long start, end;
    String place, comment;
    ArrayList<PhotoDTO> photoss = new ArrayList<>();
    String friends="[]";

    @Override
    public String getFriends() {
        return friends;
    }

    @Override
    public void setFriends(String friends) {
        this.friends = friends;
    }


    boolean highlight = false;
    boolean share;
    String originId;


    public PhotoGroupDTO() {

    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    @Override
    public boolean isShare() {
        return share;
    }

    @Override
    public void setShare(boolean share) {
        this.share = share;
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
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public double getLat() {

        return getPhotoss().get(0).getLat();
    }

    @Override
    public double getLng() {
        return getPhotoss().get(0).getLng();
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public ArrayList<PhotoDTO> getPhotoss() {
        return photoss;
    }

    public void setPhotoss(ArrayList<PhotoDTO> photoss) {
        this.photoss = photoss;
    }

    public String getComment() {
        return comment;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int getType() {
        return RealmClassHelper.PHOTO_GROUP_DATA;
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

    @Override
    public boolean isHighlight() {
        return highlight;
    }

    @Override
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

}
