# A-ger [아일랜드 거래 플랫폼 - 아거]

The used trading platform for student who living in Ireland🍀

아일랜드 유학생들을 위한 중고거래 웹 어플리케이션 플랫폼 프로젝트

------

### ⭐️ 프로젝트 목표

------
- 아일랜드 중고 거래 플랫폼 제작
- AWS를 사용한 배포 및 운영
- TDD, DDD 기반 개발
------
### 🙋‍♂️ 팀원
------

|                | Jacob  | Potter  | Frank | Guguri | Kevin |    
|----------------|:--------------:|:--------------:|:--------------:|:--------------:|:--------------:|
| **Github**     | [<img src="https://avatars.githubusercontent.com/u/46801877?v=4" width="200px;" alt=""/>](https://github.com/Supreme-YS) | [<img src="https://avatars.githubusercontent.com/u/72914519?v=4" width="200px;" alt=""/>](https://github.com/jhdl0157) | [<img src="https://avatars.githubusercontent.com/u/58693617?v=4" width="200px;" alt=""/>](https://github.com/dhkstnaos) | [<img src=https://user-images.githubusercontent.com/72914519/154930806-a8275c1f-1f55-4e36-a4e2-bddd16d7562b.jpeg width="200px;" alt=""/>](https://github.com/dhkstnaos) | [<img src="https://avatars.githubusercontent.com/u/58078994?v=4" width="200px;" alt=""/>](https://github.com/hx2ryu) |
| **E-mail**     | dudtjr1225@gmail.com | jhdl0157@naver.com | dhkstnaos@gmail.com | ds5anc900@naver.com  | hx2ryu@gmail.com  |
| **Github**     | https://github.com/Supreme-YS  | https://github.com/jhdl0157 | https://github.com/dhkstnaos    |     | https://github.com/hx2ryu |
| **Blog**       | https://supreme-ys.tistory.com/ |https://thisisthat-it.tistory.com/ | https://crazy-horse.tistory.com/ |https://limgayeon.imweb.me/ | 🐊 |
| **Position**   | Backend | Backend | Backend | Designer | Frontend
 

------

### 🛠 사용기술

------
|            | <center>Backend                     | <center>DevOps                      | <center>Collaboration & Tools | 
|----------|:---------------------------:|:---------------------------:|:-------------------------------:| 
| ▷ | Java 1.8 | Postgresql | Notion         |
| ▷ | SpringBoot 2.5.9| Kafka | GitHub       |
| ▷ | Spring Data JPA| Redis | GitHub Flow   |
| ▷ | Spring Security| EC2 | Slack           |
| ▷ | Oauth2| S3 | KakaoTalk                 |
| ▷ | QueryDSL | GitHub Action|  🇨🇮          |
| ▷ | Web Socket | ELK | 🇨🇮                  |
| ▷ | STOMP | Docker| 🇨🇮                     |
| ▷ | Gradle | 🇨🇮 | 🇨🇮                        |

------

### 🎊 Architecture & Pipeline

------
![시각화](https://user-images.githubusercontent.com/72914519/154930546-4b8234a1-9010-40c3-9a10-90bd376b0b96.png)
### 🔑**CI & CD Pipeline**
- Develop 브랜치 기능 개발 완료 후, Main 브랜치에 Pull Request 전송
- Main 브랜치 PR 이후, Merge and Push
- GitHub Actions 동작
- Main 브랜치 빌드
- 빌드 결과물 Jar 파일 -> Docker 빌드 -> 도커 이미지 생성
- 도커 이미지 -> 도커 허브 이미지 Push
- 원격 서버 EC2 도커 실행
- 도커 허브 이미지 Pull
- 도커 컨테이너 실행
------
### 🏠 ERD 구조
------
![ager](https://user-images.githubusercontent.com/58693617/154427811-28ddef62-739f-49be-9c4f-0f2f25728108.png)

------
### 🧰 주요기능
------

- 회원 - 카카오 API 로그인을 통한 회원가입, 회원 정보 수정, 회원 탈퇴
- 카카오 API 로그인
- 중고 거래 - 상품 등록, 상품 정보 수정, 상품 삭제, 상품 검색, 상품 상태 변경(판매중, 거래예약, 판매완료)
- 중고 거래 상품 사진 등록, 수정, 삭제
- 웹 채팅으로 사용자간 채팅 기능
- 게시판 - 등록, 정보 수정, 삭제, 검색, 댓글 등록, 댓글 수정, 댓글 삭제
- 구매자 및 판매자 구매 후기
- 구매자 및 판매자 평가 시스템
- 모든 유저 인기 검색어, 사용자별 최근 검색어
- 상품 및 게시판 조회수

------

### 💁🏻 USE-CASE

------
- 모든 사용자
  - 모든 사용자는 카카오 로그인 API를 통해 회원가입을 할 수 있다.
  - 모든 회원은 상품을 등록할 수 있다.
  - 모든 회원은 게시물을 등록할 수 있다.
  - 모든 회원은 게시물에 댓글을 달 수 있다.
  - 모든 회원은 판매자 및 구매자가 될 수 있다.
- 판매자
  - 판매자는 중고 상품 판매를 위해 물건을 등록, 수정, 삭제 할 수 있다.
  - 판매자는 거래 완료 이후 구매자를 평가할 수 있다.
- 구매자
  - 구매자는 중고 상품 조회 및 상품 구매를 위해 회원가입, 회원수정, 회원탈퇴를 할 수 있다.
  - 구매자는 중고 상품을 클릭하면, 상세 정보를 볼 수 있다.
  - 구매자는 중고 상품 구매를 위해 판매자와 채팅을 시작할 수 있다.
  - 구매자는 거래 완료 이후 판매자를 평가할 수 있다.
- 관리자
  - 관리자는 불건전한 판매자를 관리할 수 있다.
