#170725

https://docs.google.com/spreadsheets/d/1grs9lrhq8CYRSI8bHxooGQWIaX9tMeAWCQgiyZFT-v0/edit#gid=442900108

ToDo> 정보수집단계 조사하기 content provider : 사진, 통화, 문자 사진 : 오늘날짜인 사진들 모두 => 적당한 범위내로 한이벤트 그룹으로 처리/ 시간,장소,path =>장소 DB와 매칭 통화 : 오늘 통화 => 각각 한이벤트/ 시간,길이,사람 => 사람 DB와 매칭 문자 : 오늘 문자 => 각각 한이벤트/ 시간, 텍스트, 사람 => 텍스트 분석

http://shygiants.github.io/android/2016/01/13/contentresolver.html

관련 udacity 강의 https://classroom.udacity.com/courses/ud851/lessons/950e6939-1786-4659-89de-5af2dec70716/concepts/a18a2db2-8676-4300-945e-285ad9dce0a7

사용하는 이유 : abstraction other apps to use data from my apps

content resolver : 프로세스간 통신 동기화

URI : uniform resource identifier prefix + authority + specific data

AsyncTask with the following generic types <Void, Void, Cursor>

#170726

-	broadcast : 메신저(카카오톡, LINE, instagram 알람 등)
-	SNS 기록 가져오기 : facebook, instagram

-	Realm, DB 구조 설계

-	UI다시보기

이슈 : 자동수집데이터/ 수정데이터 캘린더 UI

달력은 나중에!

통계 라이브러리 realm은 nosql

##Realm

-	migration

https://realm.io/docs/java/latest/#migrations

최초의 경우에는 상관없으나 이미 같은클래스로 저장되어있는 데이터가 있을경우 schema충돌이 생긴다.

그래서 migrate version 관리? 를 해주거나

개발시에는 그냥 싹다 지우고 시작하면 된다.

SMS

-	0: _id
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

CALL https://developer.android.com/reference/android/provider/CallLog.Calls.html

-	CACHED_NAME
-	DATE
-	DURATION
-	GEOCODED_LOCATION
-	NUMBER
-	REJECTED_TYPE(수신관련 행동 도?)

IMAGE

https://developer.android.com/reference/android/database/ContentObserver.html

이미지 저장방식? observer를 통한 지켜보기?

recycler view view type

https://medium.com/@ruut_j/a-recyclerview-with-multiple-item-types-bce7fbd1d30e

expandable layout content observer
