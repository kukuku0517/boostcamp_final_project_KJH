package com.example.android.contentproviderbroadcastreceiver.Interface;

import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;

import io.realm.RealmObject;

/**
 * Created by samsung on 2017-08-01.
 */

public interface CardItemClickListener {
    void onNotifyItemClick(MyRealmObject item);
    void onSmsItemClick(MyRealmObject item);
    void onPhotoItemClick(MyRealmObject item);


}
