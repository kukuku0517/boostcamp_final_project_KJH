package com.example.android.selfns.GroupView.Data;

import com.example.android.selfns.Helper.RealmClassHelper;
import com.example.android.selfns.Interface.MyRealmCommentableObject;
import com.example.android.selfns.Interface.MyRealmGpsObject;
import com.example.android.selfns.DetailView.Data.PhotoData;
import com.example.android.selfns.Interface.MyRealmShareableObject;

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

    private boolean highlight = false;
    private boolean share;


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
    public void setHighlight() {
        highlight = !highlight;
    }

    @Override
    public boolean getHighlight() {
        return highlight;
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
