package com.example.android.contentproviderbroadcastreceiver.Interface;

import android.os.Parcelable;

import com.gvillani.pinnedlist.GroupListWrapper;
import com.gvillani.pinnedlist.ItemPinned;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by samsung on 2017-07-26.
 */
public interface MyRealmObject extends RealmModel, GroupListWrapper.Selector {

    int getType();

    long getDate();

    long getId();

    void setComment(String comment);
    void setHighlight();
    boolean getHighlight();

}
