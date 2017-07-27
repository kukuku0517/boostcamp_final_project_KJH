#170725

https://docs.google.com/spreadsheets/d/1grs9lrhq8CYRSI8bHxooGQWIaX9tMeAWCQgiyZFT-v0/edit#gid=442900108

TODO> 정보수집단계 조사하기

content provider : 사진, 통화, 문자

-	사진 : 오늘날짜인 사진들 모두 => 적당한 범위내로 한이벤트 그룹으로 처리/ 시간,장소,path =>장소 DB와 매칭
-	통화 : 오늘 통화 => 각각 한이벤트/ 시간,길이,사람 => 사람 DB와 매칭 문자 : 오늘
-	문자 => 각각 한이벤트/ 시간, 텍스트, 사람 => 텍스트 분석

**content resolver** http://shygiants.github.io/android/2016/01/13/contentresolver.html

**content provider관련 udacity 강의** https://classroom.udacity.com/courses/ud851/lessons/950e6939-1786-4659-89de-5af2dec70716/concepts/a18a2db2-8676-4300-945e-285ad9dce0a7

content provider를 사용하는 이유 :

> -	abstraction
> -	other apps to use data from my apps\*

content resolver : 프로세스간 통신 동기화

URI : uniform resource identifier prefix + authority + specific data

AsyncTask with the following generic types <Void, Void, Cursor>

#170726

TODO>

-	Realm, DB 구조 설계
-	broadcast : 메신저(카카오톡, LINE, instagram 알람 등)
-	SNS 기록 가져오기 : facebook, instagram
-	UI다시보기

이슈 :

1.	자동수집데이터 -> content observer를 통한 부분 업데이트 구현 https://developer.android.com/reference/android/database/ContentObserver.html

2.	수정데이터 -> 별개 DB(?)

3.	캘린더 UI -> 튜터님이 찾아보신다고함!

##Realm

-	migration

> https://realm.io/docs/java/latest/#migrations
>
> 최초의 경우에는 상관없으나 이미 같은클래스로 저장되어있는 데이터가 있을경우 schema충돌이 생긴다.
>
> 그래서 migrate version 관리? 를 해주거나 개발시에는 그냥 싹다 지우고 시작하면 된다.

**SMS**

-	0: (\_\_\__)id
-	1: thread_id
-	2: address
-	3: person
-	4: date
-	5: protocol
-	6: read  
-	7: status
-	8: type
-	9: reply_path_present
-	10: subject
-	11: body
-	12: service_center
-	13: locked

**CALL** https://developer.android.com/reference/android/provider/CallLog.Calls.html

-	CACHED_NAME
-	DATE
-	DURATION
-	GEOCODED_LOCATION
-	NUMBER
-	REJECTED_TYPE(수신관련 행동 도?)

**IMAGE**

recycler view view type 구현

https://medium.com/@ruut_j/a-recyclerview-with-multiple-item-types-bce7fbd1d30e

1.	item의 parent class 는 type return
2.	viewholder의 parent class는 bindtype메소드로 각각 bindViewholder시 item을 넘겨받아 data를 반영

expandable layout

#170727

TODO>

-	Realm, DB 구조 설계
-	broadcast : 메신저(카카오톡, LINE, instagram 알람 등)

-	SNS 기록 가져오기 : facebook, instagram

-	UI다시보기 //

-	DB정책 정하기

-	content observer

-	broadcast

-	sns

-	realm & firebase

| 배터리의 상태가 바뀔 때                                             | ACTION_BATTERY_CHANGED      | android.intent.action.BATTERY_CHANGED      |
|---------------------------------------------------------------------|-----------------------------|--------------------------------------------|
| 배터리의 잔여 용량이 낮을 때                                        | ACTION_BATTERY_LOW          | android.intent.action.BATTERY_LOW          |
| 배터리의 낮은 용량으로부터 벗어날 때                                | ACTION_BATTERY_OKAY         | android.intent.action.BATTERY_OKAY         |
| 부팅이 완료될 때                                                    | ACTION_BOOT_COMPLETED       | android.intent.action.BOOT_COMPLETED       |
| 날짜가 바뀔 때                                                      | ACTION_DATE_CHANGED         | android.intent.action.DATE_CHANGED         |
| 시스템이 dreaming을 시작할 때                                       | ACTION_DREAMING_STARTED     | android.intent.action.DREAMING_STARTED     |
| 시스템이 dreaming을 멈출 때                                         | ACTION_DREAMING_STOPPED     | android.intent.action.DREAMING_STOPPED     |
| 지역 설정이 바뀔 때                                                 | ACTION_LOCALE_CHANGED       | android.intent.action.LOCALE_CHANGED       |
| 미디어 버튼을 누를 때                                               | ACTION_MEDIA_BUTTON         | android.intent.action.MEDIA_BUTTON         |
| 통화를 발신하려고 할 때                                             | ACTION_NEW_OUTGOING_CALL    | android.intent.action.NEW_OUTGOING_CALL    |
| 앱이 처음 시작할 때                                                 | ACTION_PACKAGE_FIRST_LAUNCH | android.intent.action.PACKAGE_FIRST_LAUNCH |
| 사용자가 패키지를 재시작하고, 그것의 프로세스의 모든 것이 종료될 때 | ACTION_PACKAGE_RESTARTED    | android.intent.action.PACKAGE_RESTARTED    |
| 화면이 꺼질 때                                                      | ACTION_SCREEN_OFF           | android.intent.action.SCREEN_OFF           |
| 화면이 켜질 때                                                      | ACTION_SCREEN_ON            | android.intent.action.SCREEN_ON            |

###**data update 방식**

앱 최초 실행시 : 현재까지의 모든 데이터 가져오기

앱 이후 실행시 : 가장 최근 업데이트 시간을 저장해두고, 그때부터 현재까지의 데이터를 가져오기

앱 실행중 : observer를 통해 데이터 추가 알림

--> 수정

앱 최초 실행시 : 현재까지의 모든 데이터 가져오기

앱 이후 실행시 : boot 시 service on. notify, ContentObserver

앱 실행중 : observer를 통해 데이터 추가 알림

issue :

content observer 여러번 call하는 현상

realm notification 관련 데이터는 남기고싶은데.. migration 관리하는법
