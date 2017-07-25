package com.example.android.contentproviderbroadcastreceiver;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.R.attr.start;
import static android.media.CamcorderProfile.get;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        readSMSMessage();
//        phone();
//        fetchAllImages();
    }

    public long[] getStartOfToday() {
        long[] today = new long[2];

        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), 0, 0, 0);
        end.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE) + 1, 0, 0, 0);
        today[0] = start.getTimeInMillis();
        today[1] = end.getTimeInMillis();
        return today;
    }

    public int readSMSMessage() {
        Uri allMessage = Uri.parse("content://sms");

        ContentResolver cr = getContentResolver();

        long[] today = getStartOfToday();
        long startMillis = today[0];
        long endMillis = today[1];

        Cursor c = cr.query(allMessage, new String[]{"_id", "thread_id", "address", "person", "date", "body"}, "date > " + startMillis + " and date < " + endMillis, null, "date DESC");

        String string = "";
        int count = 0;
        while (c.moveToNext()) {
            long messageId = c.getLong(0);
            long threadId = c.getLong(1);
            String address = c.getString(2);
            long contactId = c.getLong(3);
            String contactId_string = String.valueOf(contactId);
            long timestamp = c.getLong(4);
            String body = c.getString(5);

            string = String.format("msgid:%d, threadid:%d, address:%s, " + "contactid:%d, contackstring:%s, timestamp:%d, body:%s", messageId, threadId, address, contactId,
                    contactId_string, timestamp, body);

//            Log.d("heylee", ++count + "st, Message: " + body);
            tv.append("\n" + body);
        }

        return 0;
    }

    void phone() {


//    uri : 'content://' scheme를 가지고 가져올 Content Provider를 결정함.
//    projection : 리턴받을 칼럼이름, null이면 전체 데이터 가져옴
//    selection : Where절에 들어갈 칼럼
//    selectionArgs : selection의 arguments 들을 나열함.
//    sortOrder : SQLite의 ORDER BY와 같은 정렬을 결정함.

        String[] projection = {CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE}; //이름,전화번호,날
        String selection = CallLog.Calls.DATE + "=?"; //WHERE절 타입이
        String[] selectionArgs = {CallLog.Calls.OUTGOING_TYPE + ""}; //수신 발신 모두  CallLog.Calls.INCOMING_TYPE, CallLog.Calls.OUTGOING_TYPE
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor cursor = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, selection,
                selectionArgs, CallLog.Calls.DEFAULT_SORT_ORDER); //최근 통화 목록중에서


        if (cursor == null || cursor.getCount() == 0)
            return;

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            //   long datetimeMillis = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            tv.append("\n" + name + " : " + phone);


            cursor.moveToNext();
        }
        cursor.close();
    }

    List<Uri> fetchAllImages() {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor imageCursor = getApplicationContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        ArrayList<Uri> result = new ArrayList<>(imageCursor.getCount());
        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);

        if (imageCursor == null) {
            // Error 발생
            // 적절하게 handling 해주세요
        } else if (imageCursor.moveToFirst()) {
            do {
                String filePath = imageCursor.getString(dataColumnIndex);
                Uri imageUri = Uri.parse(filePath);
                result.add(imageUri);
                tv.append("filePath:" + filePath);
            } while (imageCursor.moveToNext());
        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();
        return result;
    }


}
