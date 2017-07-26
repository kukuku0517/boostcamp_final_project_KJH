#1주차 과제

-	**Class 따로 분리해서 사용하기**
-	**package, class명 언더바 쓰지않기(naming이 생명이다)**
-	**String xml파일로 분리해서 관리**
-	Stagger 1개 = 리니어
-	viewType 사용해보기

#2주차 과제

CustomAdapter

1.	보통 생성자에서 listData를 세팅하지 않습니다. 따로 setListData메소드로 빼는 편이 좋습니다. (why?)

frament_main.xml

1.	line5 : MainFragment라는 파일은 없음. (사용되지 않는코드, 파일 정리하기)

#3주차 과제

CreateFragment.java

1.	키값들은 전부 상수처리해서 써야함. 오타가 날경우 에러의 가장큰원인이 됨
2.	왠만하면 디비 관련 처리는 따로 클래스를 두어서 처리하는게 보기좋음

MainActivity.java

1.	텍스트는 전부 String.xml에 정리해야됨(어떤건 xml 어떤건 그냥 스트링??)

어떤건 OnClick 버터나이프를 쓰고 어떤건 그냥 onClick을 쓰는데 통일해야될것같음. 전화번호입력란은 숫자만 들어가도록 수정요망 내용만 입력되도 완료가되는데 다른 값이 비어있을떄 체크루틴필요

-	처음부터 fragment replace로 사용
-	backstack 할거면 왜 fragment로 했나?
-	api사용할때 쓸것만 써오기! (multidex문제)
-	interface 따로빼서쓰기
-	interface, 직접통신 섞어쓰지않기? 일관성
-	string formatter 사용하기

url통해서 가져오는것 썸네일, 캐싱, 디코딩 등
