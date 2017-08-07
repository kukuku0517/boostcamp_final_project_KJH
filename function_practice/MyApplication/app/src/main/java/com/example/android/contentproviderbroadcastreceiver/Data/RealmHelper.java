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
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.PhotoGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsUnitData;

import java.util.Calendar;
import java.util.Date;
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
        this.context = context;
    }

    static final long quarter = 21600000;

    //milli기준, 해당 일의 시작과 끝 return
    public static long[] getDate(long millis) {
        Date date = new Date(millis);
        GregorianCalendar today = new GregorianCalendar();
        today.setTime(date);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        long start = today.getTimeInMillis();
        today.add(Calendar.DATE, 1);
        long end = today.getTimeInMillis();
        return new long[]{start, end};
    }

    //millis기준 해당 일의 DayData createOrFind
    public static DayData getDayObject(Realm realm, long millis) {
        final long[] today = getDate(millis);
        final DayData[] dayData = new DayData[1];

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dayData[0] = realm.where(DayData.class).equalTo("start", today[0]).findFirst();
                if (dayData[0] == null) {
                    Log.d("photogroup", "new today");
                    dayData[0] = realm.createObject(DayData.class, nextId(DayData.class, realm));
                    dayData[0].setStart(today[0]);
                    dayData[0].setEnd(today[1]);
                } else {
                    Log.d("photogroup", "old today");
                }

            }
        });
        return dayData[0];
    }

    //해당 realmObject class의 id auto-increment한 값
    public static int nextId(Class c, Realm realm) {
        Number currentIdNum = realm.where(c).max("id");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    //해당 전화번호의 이름을 연락처에서 가져오기.
    public static String getContactName(final String phoneNumber) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        String contactName = "모르는 번호";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor.moveToNext()) {
            contactName = cursor.getString(0);
            cursor.close();
        }
        return contactName;
    }

    private static void getPlaceName(Realm realm, double[] lat, double[] lng, String[] place) {
        RealmResults<GpsData> gpsDatas = realm.where(GpsData.class).findAll();
        if (gpsDatas.size() != 0) {
            GpsData last = gpsDatas.last();
            lat[0] = last.getLat();
            lng[0] = last.getLng();
            place[0] = last.getPlace();
        }
    }

    public static void callDataSave(final Cursor c) {

        Realm realm = Realm.getDefaultInstance();
        final String phone = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
        final long duration = c.getLong(c.getColumnIndex(CallLog.Calls.DURATION));
        final long datetimeMillis = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
        long id = c.getLong(c.getColumnIndex(CallLog.Calls._ID));
        final int type = c.getInt(c.getColumnIndex(CallLog.Calls.TYPE));

        final DayData dayData = getDayObject(realm, datetimeMillis);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                dayData.setPhotoLast(0);
                dayData.setGpsNew(0);
                Log.d("photogroup", "photonew by call");
                CallData callData = realm.createObject(CallData.class, nextId(CallData.class, realm));
                String name = getContactName(phone);
                callData.setPerson(name);
                callData.setDate(datetimeMillis);
                callData.setDuration(duration);
                callData.setNumber(phone);
                callData.setCallState(type);

            }
        });

    }

    public static void gpsDataSave(final long date, final double lat, final double lng, final int change, final String place, final int type) {

        Realm realm = Realm.getDefaultInstance();
        final DayData dayData = getDayObject(realm, date);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                GpsData gd = realm.createObject(GpsData.class, nextId(GpsData.class, realm));
                gd.setDate(date);
                gd.setLat(lat);
                gd.setLng(lng);
                gd.setChange(change);
                gd.setPlace(place);
                gd.setMoveState(type);

                if (change == 1) {

                    Log.d("photogroup", "gpsChange");

                    dayData.setPhotoLast(0);
                }
            }
        });
    }

    public static void photoDataSave(final Cursor imageCursor) {
        Realm realm = Realm.getDefaultInstance();

        final String filePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
        Uri imageUri = Uri.parse(filePath);
        final long date = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)) * 1000;
        long id = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));

        final double[] lat = {imageCursor.getDouble(imageCursor.getColumnIndex(MediaStore.Images.Media.LATITUDE))};
        final double[] lng = {imageCursor.getDouble(imageCursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE))};
        final String[] place = {""};
        Log.d("photolatbefore", String.valueOf(lat[0]));
        final DayData dayData = getDayObject(realm, date);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {


                PhotoData photo = realm.createObject(PhotoData.class, nextId(PhotoData.class, realm));

                if (lat[0] == 0.0) { //TODO for contentProviderData
//                    getPlaceName(realm, lat, lng, place);
                    //TODO for contentProviderData
                }
                Log.d("photolatafter", String.valueOf(lat[0]));

                Log.d("photo", String.valueOf(date));

                photo.setDate(date);
                photo.setLat(lat[0]);
                photo.setLng(lng[0]);
                photo.setPath(filePath);
                photo.setPlace(place[0]);


                long photoLast = dayData.getPhotoLast();
                PhotoGroupData photoGroupData;
                if (photoLast == 0 || photoLast + 3600000 < date) {
                    photoGroupData = realm.createObject(PhotoGroupData.class, nextId(PhotoGroupData.class, realm));
                    photoGroupData.setPlace(place[0]);
                    photoGroupData.setStart(date);
                    photoGroupData.setEnd(date);
                    photoGroupData.setCount(1);
                    photoGroupData.getPhotoss().add(photo);
                    dayData.setPhotoLast(date);


                    Log.d("photogroup", "new group photo");
                } else {
                    photoGroupData = realm.where(PhotoGroupData.class).findAll().last();
                    photoGroupData.setEnd(date);
                    photoGroupData.setCount(photoGroupData.getCount() + 1);
                    photoGroupData.getPhotoss().add(photo);

                    Log.d("photogroup", "old group photo");
                }
                dayData.setCallNew(0);
                dayData.setGpsNew(0);


            }
        });


    }


    /**
     * smsData저장
     * <p>
     * 1. smsData생성
     * 2. smsData의 date로 해당일 찾기
     * 3. smsGroup findOrCreate(start시간이 day의 start,end사이)
     * 4. 해당 범위에 맞는 (6시간 단위) smsGroup 찾기
     * 5. 해당 발신자에 맞는 smsUnit findOrCreate
     * create시 group<unit<child add해준다
     **/
    public static void smsDataSave(final Cursor c) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //1

                long messageId = c.getLong(0);
                long date = c.getLong(2);
                String body = c.getString(3);
                String address = c.getString(4);
                String person = getContactName(address);
              int type = c.getInt(5);


                //2
                long[] today = getDate(date);
                long start = today[0];
                long end = today[1];

                //2.5

                if (isTrade(body)) { //trade
                    SmsTradeData smsTradeData = realm.createObject(SmsTradeData.class, nextId(SmsTradeData.class, realm));

                    smsTradeData.setDate(date);
                    smsTradeData.setContent(body);
                    smsTradeData.setPerson(person);
                    smsTradeData.setAddress(address);

                    final double[] lat = {0};
                    final double[] lng = {0};
                    final String[] place = {""};
                    getPlaceName(realm, lat, lng, place);
                    smsTradeData.setLat(lat[0]);
                    smsTradeData.setLng(lng[0]);
                    smsTradeData.setPlace(place[0]);

                } else {   //not Trade
                    SmsData smsData = realm.createObject(SmsData.class, nextId(SmsData.class, realm));

                    smsData.setDate(date);
                    smsData.setContent(body);
                    smsData.setPerson(person);
                    smsData.setAddress(address);

                    if(type==1){//inbox
                        smsData.setSent(false);
                    }else if(type ==2){//sent
                        smsData.setSent(true);
                    }

                    //3
                    RealmResults<SmsGroupData> smsDatas = smsGroupDataLoad("start", start, end-1);
                    if (smsDatas.size() == 0) {
                        SmsGroupData ngData1 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
                        SmsGroupData ngData2 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
                        SmsGroupData ngData3 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
                        SmsGroupData ngData4 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
                        ngData1.setTime(start, start + quarter);
                        ngData2.setTime(start + quarter, start + quarter * 2);
                        ngData3.setTime(start + quarter * 2, start + quarter * 3);
                        ngData4.setTime(start + quarter * 3, start + quarter * 4);
                        smsDatas = RealmHelper.smsGroupDataLoad("start", start, end);
                    }

                    //4
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

                    //5
                    int i = smsGData.checkName(smsData.getPerson());

                    if (i != -1) {
                        SmsUnitData nuData = smsGData.getUnits().get(i);
                        nuData.setCount(nuData.getCount() + 1);
                        nuData.setStart(smsData.getDate());
                        nuData.setEnd(smsData.getDate());
                        nuData.getSmss().add(smsData);
                    } else {
                        SmsUnitData nuData = realm.createObject(SmsUnitData.class, nextId(SmsUnitData.class, realm));
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
            }
        });

    }

    private static boolean isTrade(String body) {

        if ( body.contains("잔액") && body.contains("신한"))
            return true;
        else
            return false;
    }

    public static void notifyDataSave(final String title, final String text, String subtext, final long when) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                NotifyData notifyData = realm.createObject(NotifyData.class, nextId(NotifyData.class, realm));

                notifyData.setDate(when);
                notifyData.setContent(text);
                notifyData.setPerson(title);
                long[] today = getDate(when);
                long start = today[0];
                long end = today[1];

                RealmResults<RealmObject> ngDatas = DataLoad(NotifyGroupData.class, "start", start, end-1);

                if (ngDatas.size() == 0) {
                    NotifyGroupData ngData1 = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
                    NotifyGroupData ngData2 = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
                    NotifyGroupData ngData3 = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
                    NotifyGroupData ngData4 = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
                    ngData1.setTime(start, start + quarter);
                    ngData2.setTime(start + quarter, start + quarter * 2);
                    ngData3.setTime(start + quarter * 2, start + quarter * 3);
                    ngData4.setTime(start + quarter * 3, start + quarter * 4);

                    ngDatas = RealmHelper.DataLoad(NotifyGroupData.class, "start", start, end);
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
                if (notifyData.getPerson() == null) {
                    return;
                }
                int i = ngData.checkName(notifyData.getPerson());
                Log.d("grouping int i ", String.valueOf(i));

                if (i != -1) {
                    NotifyUnitData nuData = ngData.getUnits().get(i);
                    nuData.setCount(nuData.getCount() + 1);
                    nuData.setStart(notifyData.getDate());
                    nuData.setEnd(notifyData.getDate());
                    nuData.getNotifys().add(notifyData);
                } else {
                    NotifyUnitData nuData = realm.createObject(NotifyUnitData.class, nextId(NotifyUnitData.class, realm));
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
        RealmResults<GpsData> gData = realm.where(GpsData.class).between(query, start, end).equalTo("change", 1).findAll();
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
