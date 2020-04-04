# Rihno-Cook

## 작품 소개
Rihno Cook은 사용자들끼리 자기만의 독창적인 레시피를 공유하고, 만든 음식들에 대해 서로 소통할 수 있는 어플입니다.

## 개발환경
* Android(Kotlin)
* Node.js
* MySQL
* Retrofit2
* Apache2

## 주요기능
* 레시피 작성하기
  1. 레시피 작성 : 여러 장의 사진이랑 글로 레시피를 작성한다.
  1. 비디오 레시피 : 비디오 영상이랑 글로 레시피를 작성한다.

* 인기, 추천 레시피
  1. 인기 레시피 : 좋아요가 높은 레시피 3개를 보여준다.
  1. 추천 레시피 : 사용자가 좋아요 누른 레시피 종류(한식, 중식 등)를 랜덤으로 3개 보여준다.
* 토크 작성하기
* 좋아요, 댓글작성
* 나의 관심
  1. 좋아요 보관함 : 사용자가 좋아요 표시한 레시피들을 모아서 볼 수 있다.

## 데모영상
[데모영상](https://drive.google.com/file/d/1vO-suTNcBleavfgPvTFw7jFkVduZwjz0/view?usp=sharing)

## 기능별 이미지
* 로그인 및 회원가입

![로그인 부분](https://user-images.githubusercontent.com/58352779/77731786-ee85f700-7046-11ea-8338-81375d3f6917.PNG)
![회원가입 부분](https://user-images.githubusercontent.com/58352779/77731790-efb72400-7046-11ea-820f-98558d6b19ef.PNG)

* 메인메뉴들(메인, 레시피, 쿡TV, 쿡토크)

![리프레쉬 메뉴1](https://user-images.githubusercontent.com/58352779/77731328-0a3ccd80-7046-11ea-855b-62593e91a9fa.png)
![리프레쉬 메뉴2](https://user-images.githubusercontent.com/58352779/77731380-26d90580-7046-11ea-913f-280858a456ba.png)
![리프레쉬 메뉴3 (2)](https://user-images.githubusercontent.com/58352779/77731336-0d37be00-7046-11ea-9be8-132e54525e9d.png)
![리프레쉬 메뉴4](https://user-images.githubusercontent.com/58352779/77731338-0d37be00-7046-11ea-8c87-d20a20c87d3d.png)

* 레시피 업로드

![레시피 업로드1](https://user-images.githubusercontent.com/58352779/77642898-bd072000-6fa1-11ea-9345-840fcb2a9d07.PNG) 
![레시피 업로드2](https://user-images.githubusercontent.com/58352779/77643353-8bdb1f80-6fa2-11ea-9143-a976a32fe068.PNG)
![쿡토크 업로드](https://user-images.githubusercontent.com/58352779/77731870-1bd2a500-7047-11ea-873c-80585aec3e13.PNG)

* 레시피 상세보기

![상세보기 완](https://user-images.githubusercontent.com/58352779/77732132-93a0cf80-7047-11ea-9c89-acdfe4f62306.PNG)
![상세보기2 - 복사본](https://user-images.githubusercontent.com/58352779/77732135-94d1fc80-7047-11ea-883f-0291a2878613.PNG)

* 내정보 및 나의관심

![메뉴6](https://user-images.githubusercontent.com/58352779/77732209-b59a5200-7047-11ea-9057-8d8ce64bd5d6.PNG)
![메뉴6 나의관심창](https://user-images.githubusercontent.com/58352779/77732214-b6cb7f00-7047-11ea-8556-7e10c4034467.PNG)

## 후기
첫 작품과는 다르게 서버와 연동해서 데이터들을 MySQL에 저장하는 형식으로 만들었다. 물론 처음에 비하면 액티비티가 많이 줄었지만 아직까지도 많이 부족한 것 같다. 기능이 중복되는 함수들이 많다. 재사용할 수 있는 함수들이 있는데 무분별하게 생성함으로 코드 수가 늘어나고 한 눈에 파악하기 힘들어 진것이다. 일단은 최대한 재활용할 수 있는 함수들을 골라내고 수정해야 할 것 같다.
