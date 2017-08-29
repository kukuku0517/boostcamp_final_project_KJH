package com.example.android.selfns.Data.RealmData.interfaceRealmData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsung on 2017-08-14.
 */

public interface MyRealmShareableObject extends MyRealmObject {

    int getShare();

    void setShare(int share);


    String getFriends();

    void setFriends(String friends);


    String getFid();

    void setFid(String fid);

    long getTimestamp();

    void setTimestamp(long timestamp);


    long get_id();

    void set_id(long _id);
}
