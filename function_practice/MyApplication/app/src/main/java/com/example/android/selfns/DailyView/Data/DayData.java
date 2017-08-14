package com.example.android.selfns.DailyView.Data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-03.
 */

public class DayData extends RealmObject {

//    -	gps 연속 : 직전이 "의미있는" gps면
//	-	call 연속 : 직전이 gps제외 call이면
//	-	photo 연속 : gps이동시 reset
//	-	photo 최종 시간 : 1시간 경과시 reset
//	-	highlight
//	-	etc
    @PrimaryKey
    private  long id;

    private  long start, end;
    private long  gpsNew=0;
    private  int callNew =0;


    public long getId() {
        return id;
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

    public long getGpsNew() {
        return gpsNew;
    }

    public void setGpsNew(long gpsNew) {
        this.gpsNew = gpsNew;
    }

    public int getCallNew() {
        return callNew;
    }

    public void setCallNew(int callNew) {
        this.callNew = callNew;
    }

    public long getPhotoLast() {
        return photoLast;
    }

    public void setPhotoLast(long photoLast) {
        this.photoLast = photoLast;
    }

    public long getHighlightId() {
        return highlightId;
    }

    public void setHighlightId(long highlightId) {
        this.highlightId = highlightId;
    }

    long photoLast=0;
    long highlightId;



}
