package com.example.android.contentproviderbroadcastreceiver.Interface;

import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.GpsData;

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

}
