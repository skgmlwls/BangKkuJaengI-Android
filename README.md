# 🏡 방꾸쟁이 - 감성 인테리어 쇼핑몰

**오늘도 방꾸쟁이와 감성 충전! ✨**  
**방꾸쟁이**는 감각적인 인테리어 제품과 소품을 제안하는 쇼핑몰입니다.  
취향에 꼭 맞는 디자인으로 당신만의 특별한 공간을 손쉽게 완성해보세요. 🏡

<br>

![방꾸쟁이 대표 이미지](https://firebasestorage.googleapis.com/v0/b/projectimage-bafe9.firebasestorage.app/o/BangKkuJaengI_Image%2F%EB%B0%A9%EA%BE%B8%EC%9F%81%EC%9D%B4_%EB%A9%94%EC%9D%B8%EC%9D%B4%EB%AF%B8%EC%A7%80.png?alt=media&token=a4980092-423c-4041-a546-ab65bde5c2b1)

---

## 📌 프로젝트 개요
- **📌 프로젝트명**: 방꾸쟁이
- **📅 개발 기간**: XX일
- **👥 팀원**: 김혜인, 정지은, 정지석, 나희진
- **🛍️ 주요 기능**:
  - ✅ 인테리어 제품 및 소품 판매
  - 📸 사용자 커뮤니티를 통한 인테리어 공유
  - ⭐ 리뷰 및 상품 태그 기능
  - 🔒 비회원/회원 기능 차별화
  - 🚚 배송 조회 및 주소 검색 기능

---

## 👥 팀 소개

### **정지석**
- 팀장
- 홈 화면 개발
- 상품 리스트 화면 개발
- 상품 좋아요 기능 구현
- 마이페이지 개발
- 쿠폰 시스템 개발
- 상품 및 쿠폰 관리 기능

### **김혜인 ️**
- 부팀장
- 회의록 작성 및 프로젝트 문서화
- 소셜 발견 및 랭킹 게시판 개발
- 소셜 팔로잉 및 팔로워 관리 기능 개발
- 소셜 내 프로필 화면 개발
- 소셜 글쓰기 및 게시글 상세 페이지 구현

### **나희진**
- 장바구니 기능 개발
- 결제 페이지 구현
- 주문 내역 관리 기능
- 배송 조회 API 연동
- 비회원 주문 조회 기능 개발

### **정지은**
- 로그인 및 회원가입 기능 개발
- 아이디/비밀번호 찾기 기능 구현
- 상품 리뷰 시스템 개발
- 관리자 주문 관리 시스템 구현

---

## 🎯 기획 의도
📌 **인테리어 제품 수요 증가**  
- 코로나 팬데믹과 1인 가구 증가로 인해 **집을 개성 있는 공간으로 꾸미려는 니즈 증가**
- 인테리어 쇼핑몰과 커뮤니티 기능을 결합하여 **소셜 프루프(Social Proof) 효과 극대화**

📌 **커뮤니티 기반 쇼핑몰**  
- 사용자가 자신의 인테리어를 공유하고 **게시글 내 제품 태그 기능 추가**
- 다른 사용자의 인테리어를 참고하여 자연스럽게 **제품 구매 유도**
- 팔로우 기능을 활용하여 **개인화된 피드 형성 가능**

📌 **타겟 레퍼런스**  
- 무신사의 스냅 서비스, 오늘의집의 발견 서비스 참고
- 사용자가 직접 올린 인테리어 사진에 제품 태그 기능 제공

---

## 🛠️ 사용 기술

<br>

![방꾸쟁이 대표 이미지](https://firebasestorage.googleapis.com/v0/b/projectimage-bafe9.firebasestorage.app/o/BangKkuJaengI_Image%2F%EB%B0%A9%EA%BE%B8%EC%9F%81%EC%9D%B4_%EA%B8%B0%EC%88%A0%EC%8A%A4%ED%83%9D.png?alt=media&token=ce091707-7d0d-4dd0-b7bc-011e5074b36b)

### **Frontend**
- **📝 언어**: Kotlin
- **🏗️ 아키텍처**: MVVM, Hilt
- **🎨 UI**: Material Components, ViewBinding
- **🌐 네트워크**: Retrofit, WebKit
- **⚡ 비동기 처리**: Coroutines
- **🖼️ 이미지 처리**: Glide, Shimmer
- **📌 네비게이션**: Jetpack Navigation, SafeArgs

### **Backend & API**
- **🔑 인증**: Firebase Authentication (문자 인증)
- **📂 데이터베이스**: Firebase Firestore, Room Database
- **🖼️ 파일 및 이미지 관리**: Firebase Storage
- **🔗 API 연동**:
  - 📍 카카오 주소 찾기 API
  - 🚚 Sweet Tracker API (배송 조회)

---

## 📌 주요 기능
### ✅ **회원 가입 & 로그인**
- 🆔 아이디, 닉네임 중복 확인
- 📱 전화번호 문자 인증
- 🔐 비밀번호 일치 확인 및 버튼 활성화

### 🛒 **상품 검색 & 구매**
- 📂 카테고리별 상품 검색 기능
- 🏷️ 태그를 활용한 맞춤 추천
- 🛍️ 장바구니 및 주문 기능

### 🚚 **배송 조회 & 주소 검색**
- 📦 **배송 상태 조회**: Sweet Tracker API 연동
- 📍 **주소 검색**: 카카오 주소 API 활용

### 👥 **커뮤니티 기능**
- 🔄 팔로우 기능
- 📝 게시글 작성 및 상품 정보 공유
- 🏷️ 게시글 내 상품 태그 기능

### 🚫 **비회원 모드**
- 🔒 할인가격 블라인드 처리
- ⛔ 일부 기능 제한 (예: 소셜 화면 접근 불가)

---

## 📽️ 시연 영상
## 🎥 *[시연 영상 링크](https://youtu.be/h-uTBJCkDJ8)*

---

## 📱 화면 구조도

![1 화면](https://firebasestorage.googleapis.com/v0/b/projectimage-bafe9.firebasestorage.app/o/BangKkuJaengI_Image%2F%EA%B8%B0%ED%9A%8D%EC%84%9C-1.png?alt=media&token=c221aa83-9b7c-47aa-ba41-70ca1d2ba15f)
![1 화면](https://firebasestorage.googleapis.com/v0/b/projectimage-bafe9.firebasestorage.app/o/BangKkuJaengI_Image%2F%EA%B8%B0%ED%9A%8D%EC%84%9C-2.png?alt=media&token=bf04f7a0-fb19-4a16-a578-71428ae7d3e0)
![1 화면](https://firebasestorage.googleapis.com/v0/b/projectimage-bafe9.firebasestorage.app/o/BangKkuJaengI_Image%2F%EA%B8%B0%ED%9A%8D%EC%84%9C-3.png?alt=media&token=2aed820e-b6ba-4fbd-8891-16c4da115c94)
![1 화면](https://firebasestorage.googleapis.com/v0/b/projectimage-bafe9.firebasestorage.app/o/BangKkuJaengI_Image%2F%EA%B8%B0%ED%9A%8D%EC%84%9C-4.png?alt=media&token=cc1d26aa-d787-414b-afd7-e6f2662e8e84)
![1 화면](https://firebasestorage.googleapis.com/v0/b/projectimage-bafe9.firebasestorage.app/o/BangKkuJaengI_Image%2F%EA%B8%B0%ED%9A%8D%EC%84%9C-5.png?alt=media&token=a5f599c5-c161-4caf-8284-1072bf8ef1d8)
![1 화면](https://firebasestorage.googleapis.com/v0/b/projectimage-bafe9.firebasestorage.app/o/BangKkuJaengI_Image%2F%EA%B8%B0%ED%9A%8D%EC%84%9C-6.png?alt=media&token=8b91b909-1895-453e-a2eb-04c6a6ef77b0)

---

## 🔥 기대 효과
✅ UX 개선 및 편리한 쇼핑 환경 제공  
✅ 신뢰 기반의 쇼핑 환경 조성 (리뷰, 배송 조회 등)  
✅ 커뮤니티 기능을 통한 사용자 경험 확대  
✅ 회원 유치를 통한 매출 증대 효과
