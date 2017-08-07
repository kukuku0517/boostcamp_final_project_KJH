package com.example.android.contentproviderbroadcastreceiver.Data;

import android.os.Parcelable;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by samsung on 2017-07-26.
 */
public interface MyRealmObject extends RealmModel {

    int getType();

    long getDate();

    long getId();

    void setComment(String comment);
}
