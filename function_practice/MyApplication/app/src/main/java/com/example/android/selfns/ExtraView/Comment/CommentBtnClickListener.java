package com.example.android.selfns.ExtraView.Comment;

import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmObject;

/**
 * Created by samsung on 2017-08-05.
 */

public interface CommentBtnClickListener{
    void onClick(Class c, MyRealmObject item,String text);
}
