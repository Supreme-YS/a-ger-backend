# A-ger [아일랜드 거래 플랫폼 - 아거]

The used trading platform for student who living in Ireland🍀

아일랜드 유학생들을 위한 중고거래 웹 플랫폼 토이 프로젝트

------

### ⭐️ 프로젝트 목표

------

- 아일랜드 중고 거래 플랫폼 제작
- AWS를 사용한 배포 및 운영
- 메인 기능 구현 이후, 기능 추가 예정
- TDD, DDD 기반 개발

------
<!-- 

### 🙋‍♂️ 팀원
|[<img src="https://avatars.githubusercontent.com/u/46801877?v=4" width="230px;" alt=""/>](https://github.com/Supreme-YS)| 
|:---:|
|[🏆심영석](https://github.com/Supreme-YS)|

|[<img src="https://avatars.githubusercontent.com/u/72914519?v=4" width="230px;" alt=""/>](https://github.com/Supreme-YS)|
|:---:|
|[🏆이재호](https://github.com/Supreme-YS)| -->

### 🛠 사용기술

------

- Java 1.8
- Spring boot 2.6.1
- JPA
- JWT
- Lombok
- Spring-Security
- O-auth2
- Postgresql
- Gradle

------

### 🏠 ERD 구조

------

![erd](https://user-images.githubusercontent.com/72914519/146857407-32af3c9e-10ed-4ee3-97f0-ce4c930562bf.png)

------

### 🧰 주요기능

------

- 회원 - 가입, 수정, 탈퇴
- 로그인
- 중고 거래 - 물품 등록, 물품 정보 수정, 물품 삭제, 물품 검색, 물품 상태 변경(판매중, 거래예약, 판매완료)
- 중고 거래 물품 사진 등록, 수정, 삭제
- 오픈 채팅 링크 연계 서비스를 통한 간편 채팅 시스템 구축
- 구매자 및 판매자 구매 후기
- 구매자 및 판매자 평가 시스템

------

### 💁🏻 USE-CASE

------

- 판매자
  - 판매자는 중고 상품 판매를 위해 물건을 등록, 수정, 삭제 할 수 있다.
  - 판매자는 거래 완료 이후 구매자를 평가할 수 있다.
  - 판매자는 오픈 채팅 링크를 반드시 생성해야 한다.
- 구매자
  - 구매자는 중고 상품 조회 및 상품 구매를 위해 회원가입, 회원수정, 회원탈퇴를 할 수 있다.
  - 구매자는 중고 상품을 클릭하면, 상세 정보를 볼 수 있다.
  - 구매자는 중고 상품 구매를 위해 판매자와 채팅을 시작할 수 있다.
  - 구매자는 거래 완료 이후 판매자를 평가할 수 있다.
- 관리자
  - 관리자는 불건전한 판매자를 관리할 수 있다.

