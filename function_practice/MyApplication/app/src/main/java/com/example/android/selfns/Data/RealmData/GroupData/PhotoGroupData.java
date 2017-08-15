package com.example.android.selfns.Data.RealmData.GroupData;

import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmCommentableObject;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmGpsObject;
import com.example.android.selfns.Data.RealmData.UnitData.PhotoData;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmShareableObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-03.
 */

public class PhotoGroupData extends RealmObject implements MyRealmCommentableObject,MyRealmShareableObject,MyRealmGpsObject {
    @PrimaryKey
    private long id;
    private int count;
    private long start, end;
    private String place,comment;
    private RealmList<PhotoData> photoss;
String originId;
    private boolean highlight = false;
    private boolean share;

   List<String> friend=new ArrayList<>();

    public List<String> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<String> friend) {
        this.friend = friend;
    }
    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public PhotoGroupData(){

    }

    @Override
    public boolean isShare() {
        return share;
    }

    @Override
    public void setShare(boolean share) {
        this.share=share;
    }

    @Override
    public boolean isHighlight() {
        return highlight;
    }

    @Override
    public void setHighlight(boolean highlight) {
        this.highlight=highlight;
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

    public RealmList<PhotoData> getPhotoss() {
        return photoss;
    }

    public void setPhotoss(RealmList<PhotoData> photoss) {
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


}
