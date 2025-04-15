### 여행 일정 관리 플랫폼

# Trigo

## 📓 목차

1. [⭐ 사용 기술 ⭐](#사용-기술)
1. [⭐ 서비스 소개 ⭐](#서비스-소개)
1. [⭐ 시스템 아키텍쳐 ⭐](#시스템-아키텍처)

## 사용 기술

<div>
  <h3>INFRA</h3>
  <img src="https://img.shields.io/badge/Amazon_EC2-FF9900?style=for-the-badge&logo=Amazon-EC2&logoColor=white">
  <img src="https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
  <img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white">
</div>

<div>
  <h3>BACKEND</h3>
  <img src="https://img.shields.io/badge/java-6DB33F?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/spring_boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white">
  <img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
</div>

<div>
  <h3>MONITORING</h3>
  <img src="https://img.shields.io/badge/grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white">
  <img src="https://img.shields.io/badge/prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white">
  <img src="https://img.shields.io/badge/loki-FDC300?style=for-the-badge&logo=grafana&logoColor=white">
  <img src="https://img.shields.io/badge/grafana_alloy-ED5B26?style=for-the-badge&logo=grafana&logoColor=white">
</div>


## 서비스 소개

### 💎 여행지 조회

- 관광지 기본정보, 운영시간, 위치, 연락처 등 세부 정보 조회 기능 포함
- 한국관광공사 TourAPI를 통해 지역 기반, 위치 기반, 키워드 기반으로 관광정보 제공 가능

<br/>

### 💎 여행 일정 관리

- 로그인 사용자의 여행 일정 생성, 수정, 삭제 기능 제공
- 여행지, 날짜, 시간 입력을 통해 새로운 일정 등록 가능
- 등록된 일정 목록 조회 및 상세 정보 확인 가능
- 다른 사용자의 공개 일정 열람 및 ‘좋아요’ 기능 제공

<br/>

### 💎 여행지 리뷰

- 여행지 또는 일정에 대한 평점 및 코멘트 작성 기능 제공
- 리뷰 목록 조회 및 상세 내용 열람 기능 포함
- 본인 작성 리뷰 수정 및 삭제 기능 제공

<br/>


## 시스템 아키텍처
<img src="./assets/system_architecture.png">