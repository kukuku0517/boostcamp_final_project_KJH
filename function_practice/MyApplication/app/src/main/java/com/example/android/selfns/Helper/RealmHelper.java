package com.example.android.selfns.Helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.android.selfns.Data.DTO.Group.NotifyUnitDTO;
import com.example.android.selfns.Data.DTO.Group.SmsUnitDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.BaseDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Data.RealmData.DayData;
import com.example.android.selfns.Data.RealmData.GroupData.GpsGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.NotifyGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.NotifyUnitData;
import com.example.android.selfns.Data.RealmData.GroupData.PhotoGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.SmsGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.SmsUnitData;
import com.example.android.selfns.Data.RealmData.UnitData.CallData;
import com.example.android.selfns.Data.RealmData.UnitData.CustomData;
import com.example.android.selfns.Data.RealmData.UnitData.GpsData;
import com.example.android.selfns.Data.RealmData.UnitData.NotifyData;
import com.example.android.selfns.Data.RealmData.UnitData.PhotoData;
import com.example.android.selfns.Data.RealmData.UnitData.SmsData;
import com.example.android.selfns.Data.RealmData.UnitData.SmsTradeData;

import java.util.StringTokenizer;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by samsung on 2017-07-27.
 */

public class RealmHelper {
    private static Context context;
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
        Realm realm2 = Realm.getDefaultInstance();
        realm2.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dayData[0] = realm.where(DayData.class).equalTo("start", today[0]).findFirst();
                if (dayData[0] == null) {
                    dayData[0] = realm.createObject(DayData.class, nextId(DayData.class, realm));
                    dayData[0].setStart(today[0]);
                    dayData[0].setEnd(today[1]);
                } else {

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
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

//            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,Manifest.permission.READ_CONTACTS)) {
//                return null;
//            } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, 1);

//            }
        }
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
        }else{
            lat[0] = 37.5609532;
            lng[0] = 126.9795367;
            place[0] = "";
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
//                dayData.setGpsNew(0);
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

                //gpsNew : 0이 아니면 gpsNew에 저장되어있는 id값의 gpsgroup으로 gpsData가 저장됨
                //gpsNew는 15분동안 아무 기록이 없으면 갱신됨
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
                        if (gpsGroupData.getEnd() + 1000 * 60 * 15 < date) {

                            //clean up last group
                            GpsGroupData gpsGroupDataEnd = realm.createObject(GpsGroupData.class, nextId(GpsGroupData.class, realm));
                            GpsData last = gpsGroupData.getGpsDatas().last();

                            gpsGroupDataEnd.setEnd(last.getDate());
                            gpsGroupDataEnd.setPlace(last.getPlace());
                            gpsGroupDataEnd.setLat(last.getLat());
                            gpsGroupDataEnd.setLng(last.getLng());
//
                            gpsGroupDataEnd.setStartId(gpsGroupData.getId());
                            gpsGroupData.setEndId(gpsGroupDataEnd.getId());

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
        final DayData dayData = getDayObject(realm, date);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PhotoData photo = realm.createObject(PhotoData.class, nextId(PhotoData.class, realm));
                if (lat[0] == 0.0) { //TODO for contentProviderData
                    getPlaceName(realm, lat, lng, place);
                    //TODO for contentProviderData
                }else{
                    RealmResults<GpsData> gpsDatas = realm.where(GpsData.class).findAll();
                    if (gpsDatas.size() != 0) {
                        GpsData last = gpsDatas.last();

                        place[0] = last.getPlace();

                    }else{

                        place[0] = "";
                    }
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
//                dayData.setGpsNew(0);
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

                    long rangeMillis = 0;
                    switch (DateHelper.getInstance().getRangeOfDay(date)) {
                        case 0:
                            rangeMillis = start;
                            break;
                        case 1:
                            rangeMillis = start + QUARTER;
                            break;
                        case 2:
                            rangeMillis = start + QUARTER * 2;
                            break;
                        case 3:
                            rangeMillis = start + QUARTER * 3;
                            break;

                    }

                    SmsGroupData sgData = realm.where(SmsGroupData.class).equalTo("start", rangeMillis).findFirst();
                    if (sgData == null) {
                        sgData = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
                        sgData.setTime(rangeMillis, rangeMillis + QUARTER);
                    }

                    //3
//                    RealmResults<SmsGroupData> smsGroupDatas = smsGroupDataLoad("start", start, end - 1);
//                    if (smsGroupDatas.size() == 0) {
//                        SmsGroupData sgData1 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
//                        SmsGroupData sgData2 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
//                        SmsGroupData sgData3 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
//                        SmsGroupData sgData4 = realm.createObject(SmsGroupData.class, nextId(SmsGroupData.class, realm));
//                        sgData1.setTime(start, start + QUARTER);
//                        sgData2.setTime(start + QUARTER, start + QUARTER * 2);
//                        sgData3.setTime(start + QUARTER * 2, start + QUARTER * 3);
//                        sgData4.setTime(start + QUARTER * 3, start + QUARTER * 4);
//                        smsGroupDatas = smsGroupDataLoad("start", start, end - 1);
//                    }
//
//                    //4
//                    int index;
//                    if (smsData.getDate() < start + QUARTER) {
//                        index = 0;
//                    } else if (smsData.getDate() < start + QUARTER * 2) {
//                        index = 1;
//                    } else if (smsData.getDate() < start + QUARTER * 3) {
//                        index = 2;
//                    } else {
//                        index = 3;
//                    }
//                    SmsGroupData sgData = smsGroupDatas.get(index);

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

                long rangeMillis = 0;
                switch (DateHelper.getInstance().getRangeOfDay(date)) {
                    case 0:
                        rangeMillis = start;
                        break;
                    case 1:
                        rangeMillis = start + QUARTER;
                        break;
                    case 2:
                        rangeMillis = start + QUARTER * 2;
                        break;
                    case 3:
                        rangeMillis = start + QUARTER * 3;
                        break;

                }

                NotifyGroupData ngData = realm.where(NotifyGroupData.class).equalTo("start", rangeMillis).findFirst();
                if (ngData == null) {
                    ngData = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
                    ngData.setTime(rangeMillis, rangeMillis + QUARTER);
                }

//
//                RealmResults<RealmObject> ngDatas = DataLoad(NotifyGroupData.class, "start", start, end - 1);
//
//                if (ngDatas.size() == 0) {
//                    NotifyGroupData ngData1 = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
//                    NotifyGroupData ngData2 = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
//                    NotifyGroupData ngData3 = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
//                    NotifyGroupData ngData4 = realm.createObject(NotifyGroupData.class, nextId(NotifyGroupData.class, realm));
//                    ngData1.setTime(start, start + QUARTER);
//                    ngData2.setTime(start + QUARTER, start + QUARTER * 2);
//                    ngData3.setTime(start + QUARTER * 2, start + QUARTER * 3);
//                    ngData4.setTime(start + QUARTER * 3, start + QUARTER * 4);
//
//                    ngDatas = DataLoad(NotifyGroupData.class, "start", start, end - 1);
//                }
//                int index;
//                if (notifyData.getDate() < start + QUARTER) {
//                    index = 0;
//                } else if (notifyData.getDate() < start + QUARTER * 2) {
//                    index = 1;
//                } else if (notifyData.getDate() < start + QUARTER * 3) {
//                    index = 2;
//                } else {
//                    index = 3;
//                }
//
//                NotifyGroupData ngData = (NotifyGroupData) ngDatas.get(index);


                if (notifyData.getPerson() == null) {
                    return;
                }
                int i = ngData.checkName(notifyData.getPerson());
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

    public void customDataSave(final String title, final String comment, final String place, final double lat, final double lng, final long date, final String tag) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomData cd = realm.createObject(CustomData.class, nextId(CustomData.class, realm));
                cd.setTitle(title);
                cd.setComment(comment);
                cd.setPlace(place);
                cd.setTag(tag);
                cd.setLat(lat);
                cd.setLng(lng);
                cd.setDate(date);
            }
        });
    }

    public RealmResults<RealmObject> DataCommentQuery(Class c, String commentQuery, String dateQuery, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmObject> cData = (RealmResults<RealmObject>) realm.where(c).contains("comment", commentQuery).between(dateQuery, start, end).findAll();
        return cData;
    }

    public RealmResults<RealmObject> DataContentQuery(Class c, String contentQuery, String dateQuery, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmObject> cData = (RealmResults<RealmObject>) realm.where(c).contains("content", contentQuery).between(dateQuery, start, end).findAll();
        return cData;
    }


    public RealmResults<RealmObject> DataLoad(Class c, String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmObject> cData = (RealmResults<RealmObject>) realm.where(c).between(query, start, end).findAll();
        return cData;
    }

    public RealmResults<RealmObject> DataHighlightLoad(Class c, String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmObject> cData = (RealmResults<RealmObject>) realm.where(c).between(query, start, end).equalTo("highlight", 1).findAll();
        return cData;
    }

    public RealmResults<RealmObject> DataHighlightAndShareLoad(Class c, String query, long start, long end) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmObject> cData = (RealmResults<RealmObject>) realm.where(c).between(query, start, end).equalTo("highlight", 1).or().between(query, start, end).equalTo("share", 1).findAll();
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

                photoGroupData.getPhotoss().deleteAllFromRealm();
                if (photoGroupData.getStart() == dayData.getPhotoLast()) {
                    dayData.setPhotoLast(0);
                }
                photoGroupData.deleteFromRealm();

    }

    public void gpsGroupDataDelete(RealmObject item) {
        Realm realm = Realm.getDefaultInstance();
        final GpsGroupData gpsGroupData = (GpsGroupData) item;
        final DayData dayData = getDayObject(realm, gpsGroupData.getDate());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                gpsGroupData.getGpsDatas().deleteAllFromRealm();
                if (gpsGroupData.getStart() == dayData.getGpsNew()) {
                    dayData.setGpsNew(0);
                }
                gpsGroupData.deleteFromRealm();

            }
        });
    }

    public void notifyUnitDataDelete(final BaseDTO item) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                NotifyUnitDTO notifyUnitDTO = (NotifyUnitDTO) item;
                long id = notifyUnitDTO.getNotifyGroupId();
                NotifyGroupData notifyGroupData = realm.where(NotifyGroupData.class).equalTo("id", id).findFirst();
                int count = notifyGroupData.getUnits().size();
//                if(count==1){
//                    notifyGroupData.deleteFromRealm();
//                }
                NotifyUnitData notifyUnitData = realm.where(NotifyUnitData.class).equalTo("id", item.getId()).findFirst();
                notifyUnitData.getNotifys().deleteAllFromRealm();
                notifyUnitData.deleteFromRealm();
            }
        });
    }

    public void smsUnitDataDelete(final BaseDTO item) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                SmsUnitDTO smsUnitDTO = (SmsUnitDTO) item;
                long id = smsUnitDTO.getSmsGroupId();
                SmsGroupData smsGroupData = realm.where(SmsGroupData.class).equalTo("id", id).findFirst();
                int count = smsGroupData.getUnits().size();
//                if(count==1){
//                    notifyGroupData.deleteFromRealm();
//                }
                SmsUnitData smsUnitData = realm.where(SmsUnitData.class).equalTo("id", item.getId()).findFirst();

                smsUnitData.getSmss().deleteAllFromRealm();
                smsUnitData.deleteFromRealm();
            }
        });
    }

    public void DataDelete(final RealmObject item) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.deleteFromRealm();
            }
        });
    }

    public void setDate() {

    }


}
