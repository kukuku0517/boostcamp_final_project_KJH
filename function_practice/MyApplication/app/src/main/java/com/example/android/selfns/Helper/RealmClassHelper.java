package com.example.android.selfns.Helper;

import com.example.android.selfns.DetailView.Data.CallData;
import com.example.android.selfns.DetailView.Data.CustomData;
import com.example.android.selfns.DetailView.Data.DatePinData;
import com.example.android.selfns.DetailView.Data.GpsData;
import com.example.android.selfns.GroupView.Data.GpsGroupData;
import com.example.android.selfns.GroupView.Data.NotifyGroupData;
import com.example.android.selfns.GroupView.Data.NotifyUnitData;
import com.example.android.selfns.GroupView.Data.PhotoGroupData;
import com.example.android.selfns.GroupView.Data.SmsGroupData;
import com.example.android.selfns.GroupView.Data.SmsUnitData;
import com.example.android.selfns.DetailView.Data.PhotoData;
import com.example.android.selfns.DetailView.Data.SmsTradeData;

/**
 * Created by samsung on 2017-08-08.
 */

public class RealmClassHelper {
    static RealmClassHelper instance;
    public static final int CALL_DATA = 0;
    public static final int GPS_DATA = 1;
    public static final int PHOTO_DATA = 2;
    public static final int SMS_TRADE_DATA = 3;
    public static final int NOTIFY_GROUP_DATA = 4;
    public static final int NOTIFY_UNIT_DATA = 5;
    public static final int PHOTO_GROUP_DATA = 6;
    public static final int SMS_GROUP_DATA = 7;
    public static final int SMS_UNIT_DATA = 8;
    public static final int GPS_GROUP_DATA = 9;
    public static final int CUSTOM_DATA = 10;
    public static final int DATE_PIN_DATA = 11;

    public static RealmClassHelper getInstance() {
        if (instance == null) {
            instance = new RealmClassHelper();
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
            case GPS_GROUP_DATA:
                return GpsGroupData.class;
            case CUSTOM_DATA:
                return CustomData.class;
            case DATE_PIN_DATA:
                return DatePinData.class;
            default:
                return null;
        }
    }
}
