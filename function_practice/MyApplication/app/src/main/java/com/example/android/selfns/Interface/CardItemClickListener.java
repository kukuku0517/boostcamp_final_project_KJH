package com.example.android.selfns.Interface;

import com.example.android.selfns.DetailView.Data.GpsData;

/**
 * Created by samsung on 2017-08-01.
 */

public interface CardItemClickListener {
    void onNotifyItemClick(MyRealmObject item);

    void onSmsGroupItemClick(MyRealmObject item);

    void onSmsTradeItemClick(MyRealmObject item);

    void onPhotoGroupItemClick(MyRealmObject item);

    void onGpsItemClick(GpsData item);

    void onCallItemClick(MyRealmObject item);

    void onGpsGroupItemClick(MyRealmObject item);



}
