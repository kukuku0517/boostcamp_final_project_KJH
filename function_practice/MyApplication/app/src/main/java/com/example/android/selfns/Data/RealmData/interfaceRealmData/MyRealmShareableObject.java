package com.example.android.selfns.Data.RealmData.interfaceRealmData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsung on 2017-08-14.
 */

public interface MyRealmShareableObject extends MyRealmObject {

    boolean isShare();

    void setShare(boolean share);

    List<String> getFriend();

    void setFriend(ArrayList<String> friend);

}
