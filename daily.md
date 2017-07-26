#170724

1.	커리큘럼 보충
	-	1주차
		-	android base (기본개념, 버전, XML, manifest ... )
		-	constraint
	-	2주차
		-	notification
		-	fragment (Mapsfragment처럼 view그대로 사용하기?)
		-	DB(sqLite, sqLiteHelper?) => Realm + 3주차 - dagger
2.	UDACITY
3.	documentation STP > 사용 기술 > 기능 명세

#170724 기술세미나

*RxJava* 알아보기

-	**Retrofit2**
-	**Glide** : gif에 사용하기 좋음. memory,cache 처리.
-	**Realm** : sqlite 보다 뛰어남. 객체자체가 실시간성 동기화(알림), 암호화. 프로젝트 단계에 설정 ex) Counter class의 static methods

-	**parceler** : parcellable 라이브러리 >다양한 type 지원 >보일러 플레이트 줄여줌 POJO베이스라이브러리와 호환 wrap, unwrap

> *보일러 플레이트
>
> -	최소한의 변경으로 재사용할 수 있는 것
>
> -	적은 수정만으로 여러 곳에 활용 가능한 코드, 문구
>
> -	각종 문서에서 반복적으로 인용되는 문서의 한 부분 )
>

-	**eventbus** : 컴포넌트간의 이벤트 전달

https://github.com/greenrobot/EventBus

> subscribe형태로 전달받음 (observer형태로 대기)
>
> main,mocker thread 지정가능 BR보다 저렴

Event 클래스 생성. post로 넘기고 subscribe로 받을 클래스임

보내는 쪽 : EventBus.getDefault().post(new MessageEvent(**변수**));

받는 쪽 : @subscribe, 전달받은 message변수를 이용해서 함수 호출

-	**databinding** : 로직과 레이아웃간의 데이터를 바인딩

공식 라이브러리 데이터, 리스너 바인딩 observable 통한 동기화 가능 (XML상에서) expression 제공

-	**guava**

ex) collection util : Joiner multivalue : same key collection value making immutable

(optional)

-	**leakcanary** : memory leak 검출, 분석 가능
-	**stetho** : debug lib. NW, DB, view, sharedpref, JS

(extras - UI 관련)

-	**alerter**(notification)

https://github.com/Tapadoo/Alerter

-	**shimmerrecycler**(mockup view)
-	**switchicon**(icon selector 꾸미기)
-	**bubblepicker**(bubble 형 ui button)

> *예제 코드 구조*
>
> api/app 구분 모듈화 해서 재사용가능 bundle builder, sharedPref 등 사용하기쉽게 유틸화 해놓음

#170726

###조별회의

태준 :

> 배운거윽지로 쓰지말자!
>
> constraint 써보기
