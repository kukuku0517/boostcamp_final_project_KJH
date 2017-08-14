package com.example.android.selfns.Interface;

/**
 * Created by samsung on 2017-08-14.
 */

public interface MyRealmCommentableObject extends MyRealmObject {


    void setComment(String comment);
    void setHighlight();
    boolean getHighlight();

}
