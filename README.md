# birman-cat
Simple Account Management 

[![Build Status](https://github.com/stray-cat-developers/birman-cat/actions/workflows/gradle.yml/badge.svg)](https://github.com/stray-cat-developers/birman-cat/actions/workflows/gradle.yml)

# New Features!
### 0.0.1 
- Redis Session 기반 로그인 / 로그아웃
- 어드민 계정 추가 / 조회
- Session 갱신
- 패스워드 초기화 / 패스워드 변경

# Installation
### Quick start
바로 시작을 하기 위해서는 Java 21, Docker가 설치되어 있어야 합니다.
Docker 기반 Mysql DB, Redis Cluster를 스토리지로 사용 중 입니다.

```sh
git clone https://github.com/stray-cat-developers/birman-cat.git
./quick-start.sh
```
Swagger: http://localhost:4300/swagger-ui/index.html

# Requirements
JDK 21

# How to Run
```
docker-compose up -d
./gradlew bootRun
```
