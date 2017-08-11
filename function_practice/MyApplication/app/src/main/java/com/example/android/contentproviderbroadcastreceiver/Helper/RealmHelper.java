package com.example.android.contentproviderbroadcastreceiver.Helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.example.android.contentproviderbroadcastreceiver.DailyView.Data.DayData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.NotifyData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.PhotoData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.SmsData;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.SmsTradeData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.GpsGroupData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.NotifyUnitData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.PhotoGroupData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.GroupView.Data.SmsUnitData;

import java.util.StringTokenizer;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by samsung on 2017-07-27.
 */

public class RealmHelper {
    static Context context;
    private static RealmHelper instance;

    public static RealmHelper getInstance() {
        if (instance == null) {
            instance = new RealmHelper();
        }
        return instance;
    }

    public RealmHelper() {
    }

    ;

    public RealmHelper(Context context) {
        this.context = context;
    }

    final long QUARTER = 21600000;

    public DayData getDayObject(Realm realm, long date) {
        final long[] today = DateHelper.getInstance().getStartEndDate(date);
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
    public int nextId(Class c, Realm realm) {
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
    public String getContactName(final String phoneNumber) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        String contactName = "모르는 번호";
        Log.d("initialize",contactName);
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor.moveToNext()) {
            contactName = cursor.getString(0);

        }
        cursor.close();
        return contactName;
    }

    private void getPlaceName(Realm realm, double[] lat, double[] lng, String[] place) {
        RealmResults<GpsData> gpsDatas = realm.where(GpsData.class).findAll();
        if (gpsDatas.size() != 0) {
            GpsData last = gpsDatas.last();
            lat[0] = last.getLat();
            lng[0] = last.getLng();
            place[0] = last.getPlace();

//            type[0] = last.getOriginType();
        }
    }

    private void getPlaceWithType(Realm realm, double[] lat, double[] lng, String[] place, String[] originId, int[] type) {
        RealmResults<GpsData> gpsDatas = realm.where(GpsData.class).findAll();
        if (gpsDatas.size() != 0) {
            GpsData last = gpsDatas.last();
            lat[0] = last.getLat();
            lng[0] = last.getLng();
            place[0] = last.getPlace();
            originId[0] = last.getOriginId();
//            type[0] = last.getOriginType();
        }
    }

    public void callDataSave(final Cursor c) {

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

    public void gpsDataSave(final long date, final double lat, final double lng, final int change, final String place, final int type, final String originId) {

        Realm realm = Realm.getDefaultInstance();
        final DayData dayData = getDayObject(realm, date);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
//
                GpsData gd = realm.createObject(GpsData.class, nextId(GpsData.class, realm));
                gd.setDate(date);
                gd.setLat(lat);
                gd.setLng(lng);
                gd.setChange(change);
                gd.setPlace(place);
                gd.setMoveState(type);
                gd.setOriginId(originId);

                GpsGroupData gpsGroupData;
                if (dayData.getGpsNew() == 0) {
                    //create new group
                    gpsGroupData = realm.createObject(GpsGroupData.class, nextId(GpsGroupData.class, realm));
                    gpsGroupData.getGpsDatas().add(gd);

                    gpsGroupData.setStart(date);
                    gpsGroupData.setEnd(date);

                    gpsGroupData.setPlace(place);
                    gpsGroupData.setLat(lat);
                    gpsGroupData.setLng(lng);
                    dayData.setGpsNew(gpsGroupData.getId());
                } else {
                    gpsGroupData = realm.where(GpsGroupData.class).equalTo("id", dayData.getGpsNew()).findFirst();
                    if (gpsGroupData != null) {
                        if (gpsGroupData.getEnd() + 1000 * 15 < date) {

                            //clean up last group
                            GpsGroupData gpsGroupDataEnd = realm.createObject(GpsGroupData.class, nextId(GpsGroupData.class, realm));
                            GpsData last = gpsGroupData.getGpsDatas().last();

                            gpsGroupDataEnd.setEnd(last.getDate());
                            gpsGroupDataEnd.setPlace(last.getPlace());
                            gpsGroupDataEnd.setLat(last.getLat());
                            gpsGroupDataEnd.setLng(last.getLng());

                            //create new group
                            GpsGroupData gpsGroupData2 = realm.createObject(GpsGroupData.class, nextId(GpsGroupData.class, realm));
                            gpsGroupData2.getGpsDatas().add(gd);

                            gpsGroupData2.setStart(date);
                            gpsGroupData2.setEnd(date);
                            gpsGroupData2.setPlace(place);
                            gpsGroupData2.setLat(lat);
                            gpsGroupData2.setLng(lng);
                            dayData.setGpsNew(gpsGroupData2.getId());
                        } else {
                            gpsGroupData.getGpsDatas().add(gd);
                            gpsGroupData.setEnd(date);
                        }
                    }
                }

                //TODO gps update photo group
//                if (change == 1) {
//
//                    Log.d("photogroup", "gpsChange");
//
//                    dayData.setPhotoLast(0);
//                }


            }
        });
    }

    public void photoDataSave(final Cursor imageCursor) {
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
                    getPlaceName(realm, lat, lng, place);
                    //TODO for contentProviderData
                }

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

                } else {
                    photoGroupData = realm.where(PhotoGroupData.class).findAll().last();
                    photoGroupData.setEnd(date);
                    photoGroupData.setCount(photoGroupData.getCount() + 1);
                    photoGroupData.getPhotoss().add(photo);
                }
                photo.setPhotoGroupId(photoGroupData.getId());
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
    public void smsDataSave(final Cursor c) {
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
                long[] today = DateHelper.getInstance().getStartEndDate(date);
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
                    final String[] originId = {""};
                    final int[] typeNum = {0};

                    getPlaceWithType(realm, lat, lng, place, originId, typeNum);
                    smsTradeData.setLat(lat[0]);
                    smsTradeData.setLng(lng[0]);
                    smsTradeData.setPlace(place[0]);
                    smsTradeData.setOriginId(originId[0]);

                } else {   //not Trade
                    SmsData smsData = realm.createObject(SmsData.class, nextId(SmsData.class, realm));

                    smsData.setDate(date);
                    smsData.setContent(body);
                    smsData.setPerson(person);
                    smsData.setAddress(address);

                    if (type == 1) {//inbox
                        smsData.setSent(false);
                    } else if (type == 2) {//sent
                        smsData.setSent(true);
                    }

                    //3
                    RealmResults<SmsGroupData> smsGroupDatas = smsGroupDataLoad("start", start, end - 1);
                    if (smsGroupDatas.size() == 0) {
                        SmsGroupData sgData1 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
                        SmsGroupData sgData2 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
                        SmsGroupData sgData3 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
                        SmsGroupData sgData4 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
                        sgData1.setTime(start, start + QUARTER);
                        sgData2.setTime(start + QUARTER, start + QUARTER * 2);
                        sgData3.setTime(start + QUARTER * 2, start + QUARTER * 3);
                        sgData4.setTime(start + QUARTER * 3, start + QUARTER * 4);
                        smsGroupDatas = smsGroupDataLoad("start", start, end - 1);
                    }

                    //4
                    int index;
                    if (smsData.getDate() < start + QUARTER) {
                        index = 0;
                    } else if (smsData.getDate() < start + QUARTER * 2) {
                        index = 1;
                    } else if (smsData.getDate() < start + QUARTER * 3) {
                        index = 2;
                    } else {
                        index = 3;
                    }
                    SmsGroupData sgData = smsGroupDatas.get(index);

                    //5
                    int i = sgData.checkName(smsData.getPerson());

                    SmsUnitData suData;
                    if (i != -1) {
                        suData = sgData.getUnits().get(i);
                        suData.setCount(suData.getCount() + 1);
                        suData.setStart(smsData.getDate());
                        suData.setEnd(smsData.getDate());
                        suData.getSmss().add(smsData);
                    } else {
                        suData = realm.createObject(SmsUnitData.class, nextId(SmsUnitData.class, realm));
                        suData.setCount(1);
                        suData.setStart(smsData.getDate());
                        suData.setEnd(smsData.getDate());
                        suData.setName(smsData.getPerson());
                        suData.setAddress(smsData.getAddress());
                        RealmList<SmsData> notiList = new RealmList<>();
                        notiList.add(smsData);
                        suData.setSmss(notiList);
                        sgData.getUnits().add(suData);
                    }
                    suData.setSmsGroupId(sgData.getId());
                    smsData.setSmsUnitId(suData.getId());
                }
            }
        });

    }

    private boolean isTrade(String body) {
        String[] banks = new String[]{"KB", "국민은행", "신한", "하나", "우리", "외환", "씨티", "농협"};
        String[] elements = {"잔액", "WEB", "결제", "원", "W", "$"};


        if (containsList(body, banks)) {
            if (containsList(body, elements)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsList(String body, String[] list) {
        for (String s : list) {
            if (body.contains(s)) {
                return true;
            }
        }
        return false;
    }

    private String[] getTradeData(String body) {
        String[] tradeDatas = new String[3]; //은행, 가격, 장소/아이템
        String[] banks = new String[]{"KB", "국민은행", "신한", "하나", "우리", "외환", "씨티", "농협"};
        String[] elements = {"잔액", "WEB", "결제", "원", "W", "$"};
        StringTokenizer tokenizer = new StringTokenizer(body);
        for (int i = 0; i < tokenizer.countTokens(); i++) {
            String token = tokenizer.nextToken();
            if (containsList(token, banks)) {
                tradeDatas[0] = token;
            }


        }
        return tradeDatas;
    }

    public void notifyDataSave(final String title, final String text, String subtext, final long date) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                NotifyData notifyData = realm.createObject(NotifyData.class, nextId(NotifyData.class, realm));

                notifyData.setDate(date);
                notifyData.setContent(text);
                notifyData.setPerson(title);
                long[] today = DateHelper.getInstance().getStartEndDate(date);
                long start = today[0];
                long end = today[1];

                RealmResults<RealmObject> ngDatas = DataLoad(NotifyGroupData.class, "start", start, end - 1);

                if (ngDatas.size() == 0) {
                    NotifyGroupData ngData1 = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
                    NotifyGroupData ngData2 = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
                    NotifyGroupData ngData3 = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
                    NotifyGroupData ngData4 = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
                    ngData1.setTime(start, start + QUARTER);
                    ngData2.setTime(start + QUARTER, start + QUARTER * 2);
                    ngData3.setTime(start + QUARTER * 2, start + QUARTER * 3);
                    ngData4.setTime(start + QUARTER * 3, start + QUARTER * 4);

                    ngDatas = DataLoad(NotifyGroupData.class, "start", start, end - 1);
                }
                int index;
                if (notifyData.getDate() < start + QUARTER) {
                    index = 0;
                } else if (notifyData.getDate() < start + QUARTER * 2) {
                    index = 1;
                } else if (notifyData.getDate() < start + QUARTER * 3) {
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
                NotifyUnitData nuData;
                if (i != -1) {
                    nuData = ngData.getUnits().get(i);
                    nuData.setCount(nuData.getCount() + 1);
                    nuData.setStart(notifyData.getDate());
                    nuData.setEnd(notifyData.getDate());
                    nuData.getNotifys().add(notifyData);
                } else {
                    nuData = realm.createObject(NotifyUnitData.class, nextId(NotifyUnitData.class, realm));
                    nuData.setCount(1);
                    nuData.setStart(notifyData.getDate());
                    nuData.setEnd(notifyData.getDate());
                    nuData.setName(notifyData.getPerson());
                    RealmList<NotifyData> notiList = new RealmList<>();
                    notiList.add(notifyData);
                    nuData.setNotifys(notiList);
                    ngData.getUnits().add(nuData);
                }

                nuData.setNotifyGroupId(ngData.getId());
                notifyData.setNotifyUnitId(nuData.getId());
            }
        });


    }

    public RealmResults<RealmObject> DataLoad(Class c, String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmObject> cData = (RealmResults<RealmObject>) realm.where(c).between(query, start, end).findAll();
        return cData;
    }

    public RealmResults<RealmObject> DataHighlightLoad(Class c, String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmObject> cData = (RealmResults<RealmObject>) realm.where(c).between(query, start, end).equalTo("highlight", true).findAll();
        return cData;
    }


    public RealmResults<GpsData> gpsDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<GpsData> gData = realm.where(GpsData.class).between(query, start, end).equalTo("change", 1).findAll();
        return gData;
    }


    public RealmResults<SmsGroupData> smsGroupDataLoad(String date, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SmsGroupData> SmsData = realm.where(SmsGroupData.class).between("start", start, end).findAll();
        return SmsData;
    }


    public RealmResults<NotifyGroupData> notifyGroupDataLoad(String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NotifyGroupData> ngData = realm.where(NotifyGroupData.class).between("start", start, end).findAll();
        return ngData;
    }


    public void photodataDelete(RealmObject item) {

        Realm realm = Realm.getDefaultInstance();

        final PhotoData photoData = (PhotoData) item;
        final DayData dayData = getDayObject(realm, ((PhotoData) item).getDate());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                long id = photoData.getPhotoGroupId();
                PhotoGroupData photoGroupData = realm.where(PhotoGroupData.class).equalTo("id", id).findFirst();
                int count = photoGroupData.getCount();
                if (count == 1) {
                    if (photoGroupData.getStart() == dayData.getPhotoLast()) {
                        dayData.setPhotoLast(0);
                    }
                    photoGroupData.deleteFromRealm();

                } else {
                    photoGroupData.setCount(count - 1);
                }
                photoData.deleteFromRealm();
            }
        });
    }

    public void photoGroupDataDelete(RealmObject item) {
        Realm realm = Realm.getDefaultInstance();
        final PhotoGroupData photoGroupData = (PhotoGroupData) item;
        final DayData dayData = getDayObject(realm, photoGroupData.getDate());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                photoGroupData.getPhotoss().deleteAllFromRealm();
                if (photoGroupData.getStart() == dayData.getPhotoLast()) {
                    dayData.setPhotoLast(0);
                }
                photoGroupData.deleteFromRealm();

            }
        });
    }

    public static void notifyUnitDataDelete(final RealmObject item) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                NotifyUnitData notifyUnitData = (NotifyUnitData) item;
                long id = notifyUnitData.getNotifyGroupId();
                NotifyGroupData notifyGroupData = realm.where(NotifyGroupData.class).equalTo("id", id).findFirst();
                int count = notifyGroupData.getUnits().size();
//                if(count==1){
//                    notifyGroupData.deleteFromRealm();
//                }
                notifyUnitData.getNotifys().deleteAllFromRealm();
                notifyUnitData.deleteFromRealm();
            }
        });
    }

    public static void smsUnitDataDelete(final RealmObject item) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                SmsUnitData smsUnitData = (SmsUnitData) item;
                long id = smsUnitData.getSmsGroupId();
                SmsGroupData smsGroupData = realm.where(SmsGroupData.class).equalTo("id", id).findFirst();
                int count = smsGroupData.getUnits().size();
//                if(count==1){
//                    notifyGroupData.deleteFromRealm();
//                }
                smsUnitData.getSmss().deleteAllFromRealm();
                smsUnitData.deleteFromRealm();
            }
        });
    }

    public static void DataDelete(final RealmObject item) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.deleteFromRealm();
            }
        });
    }

}
