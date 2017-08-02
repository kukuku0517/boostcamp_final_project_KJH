package com.example.android.contentproviderbroadcastreceiver.Data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsUnitData;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static android.R.attr.id;
import static android.R.attr.start;
import static com.example.android.contentproviderbroadcastreceiver.R.string.month;
import static com.example.android.contentproviderbroadcastreceiver.R.string.year;
import static io.realm.Realm.getDefaultInstance;

/**
 * Created by samsung on 2017-07-27.
 */

public class RealmHelper {
    static Context context;
    public RealmHelper(Context context) {
        this.context =context;
    }

    static final long quarter = 21600000;

    public static long[] getDate() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);


        long start = today.getTimeInMillis();
        today.add(Calendar.DATE, 1);
        long end = today.getTimeInMillis();

        return new long[]{start, end};

    }

    public static void callDataSave(final Cursor c) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CallData callData = realm.createObject(CallData.class,  nextId(CallData.class,realm));

                String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String phone = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                String duration = c.getString(c.getColumnIndex(CallLog.Calls.DURATION));
                long datetimeMillis = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
                long id = c.getLong(c.getColumnIndex(CallLog.Calls._ID));
//
                callData.setPerson(name);
                callData.setDate(datetimeMillis);
                callData.setDuration(duration);
                callData.setNumber(phone);

            }
        });

    }


    public static void photoDataSave(final Cursor imageCursor) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                PhotoData photo = realm.createObject(PhotoData.class,  nextId(PhotoData.class,realm));

                String filePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Uri imageUri = Uri.parse(filePath);
                long date = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)) * 1000;
                long id = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));

                double lat = imageCursor.getDouble(imageCursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
                double lng = imageCursor.getDouble(imageCursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
                String place = "";
                Log.d("photolatbefore", String.valueOf(lat));
                if (lat == 0.0) {
                    RealmResults<GpsData> gpsDatas = realm.where(GpsData.class).findAll();
                    if (gpsDatas.size() != 0) {
                        GpsData last = gpsDatas.last();
                        lat = last.getLat();
                        lng = last.getLng();
                        place = last.getPlace();
                    }

                }
                Log.d("photolatafter", String.valueOf(lat));

                Log.d("photo", String.valueOf(date));

                photo.setDate(date);
                photo.setLat(lat);
                photo.setLng(lng);
                photo.setPath(filePath);
                photo.setPlace(place);


            }
        });


    }

    public static void gpsDataSave(final long date, final double lat, final double lng, final int change, final String place) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GpsData gd = realm.createObject(GpsData.class, nextId(GpsData.class,realm));
                gd.setDate(date);
                gd.setLat(lat);
                gd.setLng(lng);
                gd.setChange(change);
                gd.setPlace(place);
            }
        });
    }

public static int nextId(Class c, Realm realm){
    Number currentIdNum = realm.where(c).max("id");
    int nextId;
    if(currentIdNum == null) {
        nextId = 1;
    } else {
        nextId = currentIdNum.intValue() + 1;
    }
    return nextId;
}

    public static String getContactName(final String phoneNumber)
    {
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";
        Cursor cursor= context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }


    public static void smsDataSave(final Cursor c) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SmsData smsData = realm.createObject(SmsData.class, nextId(SmsData.class,realm));
                long messageId = c.getLong(0);
                long timestamp = c.getLong(2);
                String body = c.getString(3);
                String address = c.getString(4);
String person = getContactName(address);
//
//                Log.d("messageLog",c.getString(5)+""+c.getString(6)+""+c.getString(7)+""+c.getString(8)+"\n");

                smsData.setDate(timestamp);


                smsData.setContent(body);
                smsData.setPerson(person);
                smsData.setAddress(address);
//

//                NotifyData notifyData = realm.createObject(NotifyData.class);
//                notifyData.setId("0");
//                notifyData.setDate(when);
//                notifyData.setContent(text);
//                notifyData.setPerson(title);
                long[] today = getDate();
                long start = today[0];
                long end = today[1];

                RealmResults<SmsGroupData> smsDatas = smsGroupDataLoad("date", start, end);
                if (smsDatas.size() == 0) {
                    SmsGroupData ngData1 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class,realm));
                    SmsGroupData ngData2 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class,realm));
                    SmsGroupData ngData3 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class,realm));
                    SmsGroupData ngData4 = realm.createObject(SmsGroupData.class,  nextId(SmsGroupData.class,realm));
                    ngData1.setTime(start, start + quarter);
                    ngData2.setTime(start + quarter, start + quarter * 2);
                    ngData3.setTime(start + quarter * 2, start + quarter * 3);
                    ngData4.setTime(start + quarter * 3, start + quarter * 4);

                    smsDatas = RealmHelper.smsGroupDataLoad("date", start, end);
                }




                int index;
                if (smsData.getDate() < start + quarter) {
                    index = 0;
                } else if (smsData.getDate() < start + quarter * 2) {
                    index = 1;
                } else if (smsData.getDate() < start + quarter * 3) {
                    index = 2;
                } else {
                    index = 3;
                }

                SmsGroupData smsGData = smsDatas.get(index);

                int i = smsGData.checkName(smsData.getPerson());
                Log.d("grouping int i ", String.valueOf(i));
                if (i != -1) {
                    SmsUnitData nuData = smsGData.getUnits().get(i);
                    nuData.setCount(nuData.getCount() + 1);
                    nuData.setStart(smsData.getDate());
                    nuData.setEnd(smsData.getDate());
                    nuData.getSmss().add(smsData);
                } else {
                    SmsUnitData nuData = realm.createObject(SmsUnitData.class,  nextId(SmsUnitData.class,realm));
                    nuData.setCount(1);
                    nuData.setStart(smsData.getDate());
                    nuData.setEnd(smsData.getDate());
                    nuData.setName(smsData.getPerson());
                    nuData.setAddress(smsData.getAddress());
                    RealmList<SmsData> notiList = new RealmList<>();
                    notiList.add(smsData);
                    nuData.setSmss(notiList);
                    smsGData.getUnits().add(nuData);
                }
            }
        });

    }

    public static void notifyDataSave(final String title, final String text, String subtext, final long when) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                NotifyData notifyData = realm.createObject(NotifyData.class,  nextId(NotifyData.class,realm));

                notifyData.setDate(when);
                notifyData.setContent(text);
                notifyData.setPerson(title);
                long[] today = getDate();
                long start = today[0];
                long end = today[1];

                RealmResults<RealmObject> ngDatas = DataLoad(NotifyGroupData.class, "end", start, end);

                if (ngDatas.size() == 0) {
                    NotifyGroupData ngData1 = realm.createObject(NotifyGroupData.class,  nextId(NotifyGroupData.class,realm));
                    NotifyGroupData ngData2 = realm.createObject(NotifyGroupData.class,  nextId(NotifyGroupData.class,realm));
                    NotifyGroupData ngData3 = realm.createObject(NotifyGroupData.class,  nextId(NotifyGroupData.class,realm));
                    NotifyGroupData ngData4 = realm.createObject(NotifyGroupData.class,  nextId(NotifyGroupData.class,realm));
                    ngData1.setTime(start, start + quarter);
                    ngData2.setTime(start + quarter, start + quarter * 2);
                    ngData3.setTime(start + quarter * 2, start + quarter * 3);
                    ngData4.setTime(start + quarter * 3, start + quarter * 4);

                    ngDatas = RealmHelper.DataLoad(NotifyGroupData.class, "end", start, end);
                }
                int index;
                if (notifyData.getDate() < start + quarter) {
                    index = 0;
                } else if (notifyData.getDate() < start + quarter * 2) {
                    index = 1;
                } else if (notifyData.getDate() < start + quarter * 3) {
                    index = 2;
                } else {
                    index = 3;
                }

                NotifyGroupData ngData = (NotifyGroupData) ngDatas.get(index);

                int i = ngData.checkName(notifyData.getPerson());
                Log.d("grouping int i ", String.valueOf(i));
                if (i != -1) {
                    NotifyUnitData nuData = ngData.getUnits().get(i);
                    nuData.setCount(nuData.getCount() + 1);
                    nuData.setStart(notifyData.getDate());
                    nuData.setEnd(notifyData.getDate());
                    nuData.getNotifys().add(notifyData);
                } else {
                    NotifyUnitData nuData = realm.createObject(NotifyUnitData.class,  nextId(NotifyUnitData.class,realm));
                    nuData.setCount(1);
                    nuData.setStart(notifyData.getDate());
                    nuData.setEnd(notifyData.getDate());
                    nuData.setName(notifyData.getPerson());
                    RealmList<NotifyData> notiList = new RealmList<>();
                    notiList.add(notifyData);
                    nuData.setNotifys(notiList);
                    ngData.getUnits().add(nuData);
                }
            }
        });


    }

    public static RealmResults<RealmObject> DataLoad(Class c, String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmObject> cData = (RealmResults<RealmObject>) realm.where(c).between(query, start, end).findAll();
        return cData;
    }

    public static RealmResults<GpsData> gpsDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<GpsData> gData = realm.where(GpsData.class).between(query, start, end).equalTo("change",1).findAll();
        return gData;
    }

    public static RealmResults<CallData> callDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CallData> cData = realm.where(CallData.class).between(query, start, end).findAll();
        return cData;
    }

    public static RealmResults<PhotoData> photoDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PhotoData> pData = realm.where(PhotoData.class).between(query, start, end).findAll();
        return pData;

    }

    public static RealmResults<SmsData> smsDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SmsData> sData = realm.where(SmsData.class).between(query, start, end).findAll();
        return sData;
    }

    public static RealmResults<SmsGroupData> smsGroupDataLoad(String date, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SmsGroupData> SmsData = realm.where(SmsGroupData.class).between("start", start, end).findAll();
        return SmsData;
    }

    public static RealmResults<NotifyData> notifyDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NotifyData> nData = realm.where(NotifyData.class).between(query, start, end).findAll();
        return nData;
    }

    public static RealmResults<NotifyGroupData> notifyGroupDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NotifyGroupData> ngData = realm.where(NotifyGroupData.class).between("start", start, end).findAll();
        return ngData;
    }

    public static RealmResults<NotifyUnitData> notifyUnitDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NotifyUnitData> nuData = realm.where(NotifyUnitData.class).between(query, start, end).findAll();
        return nuData;
    }

}
