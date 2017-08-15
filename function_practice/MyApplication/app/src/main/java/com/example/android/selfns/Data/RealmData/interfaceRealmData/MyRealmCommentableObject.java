package com.example.android.selfns.Data.RealmData.interfaceRealmData;

/**
 * Created by samsung on 2017-08-14.
 */

public interface MyRealmCommentableObject extends MyRealmObject {


    void setComment(String comment);

    boolean isHighlight();

    void setHighlight(boolean highlight);

}
