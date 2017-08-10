package com.example.android.contentproviderbroadcastreceiver.Helper;

import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.NotifyUnitData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.PhotoGroupData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.SmsUnitData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.PhotoData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.SmsTradeData;

/**
 * Created by samsung on 2017-08-08.
 */

public class RealmDataHelper {
    static RealmDataHelper instance;
    public static final int CALL_DATA = 0;
    public static final int GPS_DATA = 1;
    public static final int PHOTO_DATA = 2;
    public static final int SMS_TRADE_DATA = 3;
    public static final int NOTIFY_GROUP_DATA = 4;
    public static final int NOTIFY_UNIT_DATA = 5;
    public static final int PHOTO_GROUP_DATA = 6;
    public static final int SMS_GROUP_DATA = 7;
    public static final int SMS_UNIT_DATA = 8;

    public static RealmDataHelper getInstance() {
        if (instance == null) {
            instance = new RealmDataHelper();
        }
        return instance;
    }

    public Class getClass(int type) {
        switch (type) {
            case CALL_DATA:
                return CallData.class;
            case GPS_DATA:
                return GpsData.class;
            case NOTIFY_GROUP_DATA:
                return NotifyGroupData.class;
            case NOTIFY_UNIT_DATA:
                return NotifyUnitData.class;
            case PHOTO_DATA:
                return PhotoData.class;
            case PHOTO_GROUP_DATA:
                return PhotoGroupData.class;
            case SMS_GROUP_DATA:
                return SmsGroupData.class;
            case SMS_TRADE_DATA:
                return SmsTradeData.class;
            case SMS_UNIT_DATA:
                return SmsUnitData.class;
            default:
                return null;
        }
    }
}
