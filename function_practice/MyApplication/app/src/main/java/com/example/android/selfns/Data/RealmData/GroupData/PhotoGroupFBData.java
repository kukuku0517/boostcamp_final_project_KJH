package com.example.android.selfns.Data.RealmData.GroupData;

import com.example.android.selfns.Data.RealmData.UnitData.PhotoData;

import java.util.ArrayList;

/**
 * Created by samsung on 2017-08-14.
 */

public class PhotoGroupFBData { //TODO deprecated
    private long id;
    private int count;
    private long start, end;
    private String place,comment;
    private ArrayList<PhotoData> photoss;
    private String content = "";
    private boolean highlight = false;
    private boolean share;
    public PhotoGroupFBData(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<PhotoData> getPhotoss() {
        return photoss;
    }

    public void setPhotoss(ArrayList<PhotoData> photoss) {
        this.photoss = photoss;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }
}
