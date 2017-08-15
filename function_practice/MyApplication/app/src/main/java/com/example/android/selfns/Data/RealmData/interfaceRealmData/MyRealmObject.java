package com.example.android.selfns.Data.RealmData.interfaceRealmData;

import com.gvillani.pinnedlist.GroupListWrapper;

import io.realm.RealmModel;

/**
 * Created by samsung on 2017-07-26.
 */
public interface MyRealmObject extends RealmModel{

    int getType();

    long getDate();

    long getId();


}
