# 모든 개발자를 위한 HTTP 웹 기본 지식 - 김영한

## Section 1. 인터넷 네트워크
    [목차]
    - 인터넷 통신
    - IP (Internet Protocol)
    - TCP, UDP
    - Port
    - DNS

### 인터넷 통신
인터넷은 복잡한 노드들로 이루어져 있다.

---
### IP (Internet Protocol)
    [역할]
    - 지정한 IP 주소(IP Address)에 데이터 전달
    - 패킷(Packet)이라는 통신단위로 데이터 전달

IP 패킷 정보
: 출발지IP, 도착지IP, 전송할 데이터 등

서버 패킷 전달
인터넷 망이 복잡하기 때문에 `요청을 보냈을 때`와 `응답을 받을 때` 다른 루트를 통해서 패킷이 이동할 수 있다.

#### IP 프로토콜의 한계
- 비연결성 : 패킷을 받을 대상이 없거나 서비스 불능 상태여도 패킷은 전송됨.
- 비신뢰성
    - 중간에 패킷 소실
    - 패킷 전달 순서 문제 발생
- 프로그램 구분 : 같은 IP로 여러개의 애플리케이션이 실행중이라면?
---
### TCP, UDP
#### 인터넷 프로토콜의 4계층
<img alt="internet protocol 4layers" src="./img/internet%20protocol%204%20layers.png" style="width:70%;height=100%">

#### 프로토콜 계층

<img alt="프로토콜 계층" src="./img/protocol%20layer.png" style="width:70%;height=100%">

    1. 애플리케이션의 'Socket Library'가 전송할 데이터를 OS단의 TCP(전송 계층)로 전달.
    2. 데이터를 TCP 정보가 포함된 포장지(?)로 감싼 뒤에 IP계층(인터넷 계층)로 전달.
    3. IP단에서는 전달받은 데이터에 IP 데이터를 포함한 포장지(?)로 한번 더 감싼 뒤 '네트워크 인터페이스'로 전달.
    4. '네트워크 인터페이스'에서는 'Ethernet Frame'이라는 포장지(?)로 한번 더 감싸고 인터넷을 통해 전송된다.

> 패킷(Packet) = 수화물(Package) + 바구니(Bucket)

#### TCP/IP 패킷 정보
TCP segment = 출발지 Port + 도착지 Port + 전송제어 + 전송순서 + 검증 정보 + ...

#### TCP(Transmission Control Protocol) 특징
- 연결지향(3 Way Handshake) : 연결 확인을 하고 데이터를 보냄. -> 논리적으로만(물리적 X) 연결된 것. (가상연결)
- 데이터 전달 보증 : 데이터 누락을 알 수 있음.
- 순서 보장
- `신뢰할 수 있는` 프로토콜
- 현재는 대부분의 애플리케이션에서 TCP를 사용함.

**[3 Way Handshake]**

<img alt="3 way handshake" src="./img/3way%20handshake.png" style="width:70%;height=100%">

`SYN = Synchronize / ACK = Acknowledge`

**[데이터 전달 보증]**

<img alt="데이터 전달 보증" src="./img/데이터전달보증.png" style="width:70%;height=100%">


**[순서 보장]**

<img alt="순서보장" src="./img/순서보장.png" style="width:70%;height=100%">

#### UDP(User Datagram Protocol) 특징
- 기능이 거의 없음.
- IP와 거의 똑같음. 대신 `PORT와 체크썸` 정도만 추가.
- 데이터 전달 및 순서가 보장되지는 않지만 `단순하고 빠름.`

---
### Port

    IP = 목적지를 찾는 것.
    Port = 목적지 내의 특정 애플리케이션을 찾는 것.

#### Port 특징
- 0~65535 까지 할당 가능.
- 0~1023 : 잘 알려진 포트. `사용하지 않는 것이 좋음.`
    - FTP : 20, 21
    - TELNET : 23
    - HTTP : 80
    - HTTPS : 443

---
### DNS

#### DNS(Domain Name System) 특징
- IP는 기억하기 어려움.
- 도메인명 / IP주소 Pair.


## Section 2. URI와 웹 브라우저 요청 흐름
    [목차]
    - URI
    - 웹 브라우저 요청 흐름

### URI (Uniform Resource Identifier)

```
Uniform : 리소스 식별하는 통일된 방식
Resource : 자원, URI로 식별할 수 있는 모든 것(제한 없음) ex) HTML파일, 실시간 교통정보 등
Identifier : 다른 항목과 구분하는데 필요한 정보
```

URL - Locator : 리소스가 있는 `위치`를 지정

URN - Name : 리소스에 `이름`을 부여

> 위치는 변할 수 있지만, 이름은 변하지 않는다.

**URN 이름만으로 실제 리소스를 찾을 수 있는 방법이 보편화 되지 않음**

#### URL의 전체 문법
scheme://[userinfo@]host[:port][/path][?query][#fragment]

ex) https://www.google.com:443/search?q=hello?hl=ko

    - 프로토콜 : https
    - 호스트명 : www.google.com
    - 포트 번호 : 443
    - 패스 : /search
    - 쿼리 파라미터 : q=hello&hl=ko

**SCHEME**
- 주로 프로토콜 사용 (https)
    - 프로토콜(protocol) : 어떤 방식으로 자원에 접근할 것인가 하는 약속 규칙 ex) http, https, ftp 등

- [기본 포트] http는 80, https는 443, 포트는 생략 가능
- https는 http에 보안이 추가됨 (HTTP Secure)
<br/>

**USERINFO**
- URL에 사용자 정보를 포함해서 인증
- <span style="color : red;">거의 사용하지 않음.</span>
<br/>

**HOST**
- 호스트명
- 도메인명 또는 IP 주소를 직접 입력 가능.
<br/>

**PORT**
- 접속 포트
- 일반적으로 생략, 생략시 http는 80, https는 443
<br/>

**PATH**
- 리소스 경로(path), 보통 계층적 구조로 되어 있음.
[예시]
- /home/file1.jpg
- /members
- /members/100, /items/iphone12
<br/>

**QUERY**
- `key-value` 형태
- ?로 시작, &로 파라미터 추가 가능. ex) ?keyA=valueA&keyB=valueB
- query parameter, query string 등으로 불림. 웹서버에 제공하는 파라미터, 문자 형태.
<br/>

**FRAGMENT**
- HTML 내부 북마크 등에 사용
- 서버에 전송하는 정보는 아님.
---
### 웹 브라우저 요청 흐름

1) DNS에서 IP 조회 -> IP + PORT를 찾아냄.
2) 웹 브라우저에서 `HTTP method + query parameters + HTTP버전 정보, Host` 이런 형식의 HTTP 메세지를 생성함.
3) 소켓 라이브러리를 통해 TCP/IP로 구글 서버와 3 Way Handshake를 하여 연결을 하고, TCP/IP 계층으로 HTTP 요청 메세지를 전달.
4) TCP/IP 패킷 생성(HTTP 요청 메세지를 포함함.)
5) TCP/IP 패킷을 받은 서버는 TCP/IP 정보를 버리고 HTTP 요청 메세지를 해석하여 로직을 수행한 뒤 웹서버에서 응답 메세지를 생성. (HTTP 버전 + Status Code/Message + Content-Type + Content-Length 등이 포함됨.)
6) 요청보낸 클라이언트에게 응답 전달.

## Section 3. HTTP 기본
    [목차]
    - 모든 것이 HTTP
    - 클라이언트 서버 구조
    - Stateful, Stateless
    - 비 연결성(connectionsless)
    - HTTP 메시지

### 모든 것이 HTTP

HTTP (HyperText Transfer Protocol) : HTML을 전송하는 프로토콜로서 시작됨.
- HTML, TEXT
- IMAGE, 음성, 영상, 파일
- JSON, XML (API)
- 거의 모든 형태의 데이터 전송 가능
- 서버간에 데이터를 주고 받을때도 대부분 HTTP 사용
- **지금은 HTTP 시대!**

> HTTP/1.1 1997년 가장 많이 사용하는 버전, 우리에게 가장 중요한 버전

- TCP : HTTP/1.1, HTTP/2
- UDP : HTTP/3
- 현재 HTTP/1.1 주로 사용
    - HTTP/2, HTTP/3도 점점 증가 추세.

#### HTTP 특징
- 클라이언트-서버 구조
- 무상태 프로토콜(Stateless), 비연결성
- HTTP 메시지
- 단순함, 확장 가능

---

### 클라이언트 서버 구조
- Request-Response 구조
- 클라이언트가 HTTP메세지로 서버에 Request를 보냄. 응답을 대기
- 서버가 요청에 대한 결과를 만들어서 응답
- 비즈니스 로직, 데이터를 서버에 몰아넣음. 클라이언트는 UI와 사용성에 집중 <br/>**=> 클라이언트/서버가 각각 독립적으로 진화 가능**

---

### Stateful, Stateless

#### 무상태 프로토콜(stateless)
- 서버가 클라이언트의 상태를 보존x
- 장점 : 서버 확장성 높음(scale out)
- 단점 : 클라이언트가 추가 데이터 전송

Stateful(상태유지) - 항상 같은 서버가 유지되어야 한다.

Stateless(무상태)
- 아무 서버나 호출해도 된다.
- scale out 수평확장 유리

[한계]
- 모든 것을 무상태로 설계 할 수 있는 경우도 있고 없는 경우도 있음.
- 로그인은 상태를 유지해줘야 하는 기능인데 무상태로 어떻게 유지하는가?
    - 일반적으로 브라우저 쿠키와 서버 세션등을 사용해서 상태 유지
    - 상태 유지는 최소한만 사용

---

### 비연결성(Connectionless)
- HTTP는 기본이 연결을 유지하지 않는 모델
- 일반적으로 초 단위 이하의 빠른 속도로 응답
- 1시간 동안 수천명이 서비스를 사용해도 실제 서버에서 동시에 처리하는 요청은 수십개 이하로 매우 작음.
- 서버 자원을 매우 효율적으로 사용할 수 있음.

[한계와 극복]
- TCP/IP 연결을 새로 맺어야 함 - 3 Way Handshake 시간 추가
- 웹 브라우저로 사이트를 요청하면 HTML 뿐만이 아닌 JS, 이미지, CSS 등등 수 많은 자원이 함께 다운로드
- 지금은 HTTP 지속 연결(Persistent Connections)로 문제 해결
- HTTP/2, HTTP/3에서 더 많이 최적화됨.

---

### HTTP 메시지
<img alt="HTTP 메세지 구조" src="./img/HTTP message structure.png" style="width:70%; height:100%;"><br/>

<img alt="HTTP 요청 메세지" src="./img/HTTP request message.png" style="width:70%; height:100%;"><br/>
- start-line : HTTP Method + path + HTTP Version
- Header : Host
- Message Body(option) : 서버로 보낼 데이터가 있다면 포함시킴. 없으면 빈 상태로 보냄.

<img alt="HTTP 응답 메세지" src="./img/HTTP response message.png" style="width:70%; height:100%;"><br/>
- start-line : HTTP Version + Status Code + Reason-phrase
- Header
- Message Body : 요청했던 내용을 담는 곳.

#### Request Start-line
- start-line = **request-line**
- request-line = Method SP(공백) + request-target(path) SP + HTTP-version CRLF(엔터)

    HTTP method
    - 종류 : GET, POST, PUT, DELETE ...
    - 서버가 수행해야 할 동작 지정
        - GET : 리소스 조회
        - POST : 요청 내역 처리

    Request Target
    - absolute-path(절대경로:"/"로 시작하는 경로)[?query]

#### Response Start-line
- start-line = **status-line**
- status-line = HTTP-version SP + status-code SP + reason-phrase CRLF

    HTTP 상태 코드 (HTTP Status-code)
    - 200 : 성공
    - 400 : 클라이언트 요청 오류
    - 500 : 서버 오류

    Reason-phrase
    - 사람이 이해할 수 있는 짧은 상태 코드 설명 글

#### HTTP 헤더
- Header-field = field-name + ":" OWS + field-value OWS (OWS : 띄어쓰기 허용)
- field-name : 대소문자 구분 X

[용도]
- HTTP 전송에 필요한 모든 부가정보
    - 예) message body의 내용, message body의 크기, 압축, 인증, 요청 클라이언트 정보 등
- 표준 헤더가 너무 많음.
- 필요시 임의의 헤더 추가 가능

#### HTTP Message Body
- 실제 전송할 데이터
- HTML 문서, 이미지, 영상, JSON 등 byte로 표현할 수 있는 모든 데이터 전송 가능.

#### 단순함 확장 가능
- HTTP 메시지도 매우 단순.
- **크게 성공하는 표준 기술은 단순하지만 확장 가능한 기술**

#### HTTP 정리
- HTTP 메시지에 모든 것을 전송
- HTTP 역사 HTTP/1.1을 기준으로 공부
- 클라이언트-서버 구조
- 무상태 프로토콜(stateless)
- HTTP 메시지
- 단순함, 확장 가능
- **지금은 HTTP 시대**

## Section 4. HTTP 메서드
    [목차]
    - HTTP API를 만들어보자
    - HTTP 메서드 - GET, POST
    - HTTP 메서드 - PUT, PATCH, DELETE
    - HTTP 메서드의 속성

### HTTP API를 만들어보자
```
요구사항
회원 정보 관리 API를 만들어라.
- 회원 목록 조회
- 회원 조회
- 회원 등록
- 회원 수정
- 회원 삭제
```

```
API URI 설계
- 회원 목록 조회 /read-member-list
- 회원 조회 /read-member-by-id
- 회원 등록 /create-member
- 회원 수정 /update-member
- 회원 삭제 /delete-member
```

#### API URI 고민
- 리소스의 의미는 뭘까?
    - **회원을 등록/수정/조회하는게 리소스가 아니다!**
    - `회원`이라는 개념 자체가 바로 리소스다.
    - 행위의 대상 = 리소스, 행위 = HTTP 메서드
- 리소스를 어떻게 식별하는게 좋을까?
    - 회원을 등록/수정/조회하는 것을 **모두 배제**
    - `회원`이라는 리소스만 식별하면 된다. -> 회원 리소스를 URI에 매핑

<br/>

**[리소스 식별, URI 계층 구조 활용]**
- 회원 목록 조회 /members
- 회원 조회 /members/{id}
- 회원 등록 /members/{id}
- 회원 수정 /members/{id}
- 회원 삭제 /members/{id}
- ***참고: 계층 구조상 상위를 컬렉션으로 보고 복수단어 사용 권장(member -> members)***

그렇다면 `/members/{id}` 를 어떻게 구분할까?

**리소스와 행위를 분리**
가장 중요한 것은 리소스를 식별하는 것
- URI는 `리소스`만 식별!
- 행위(메서드)는 어떻게 구분? => 행위는 HTTP method가 함.

---

### HTTP 메서드 - GET, POST
#### HTTP 메서드 종류
- GET : 리소스 `조회`
- POST : 요청 데이터 처리, 주로 `등록`에 사용
- PUT : 리소스를 `대체`, 해당 리소스가 없으면 생성
- PATCH : 리소스 부분 `변경`
- DELETE : 리소스 `삭제`

[기타 메서드]
- HEAD : GET과 동일. 메시지 부분을 제외학, 상태줄과 헤더만 반환
- OPTIONS : 대상 리소스에 대한 통신 기능 옵션(메서드)를 설명(주로 CORS에서 사용)
- CONNECT, TRACE : 거의 사용 안함.

#### GET
- 리소스 조회
- 서버에 전달하고 싶은 데이터는 query(쿼리 파라미터, 쿼리 스트링)을 통해서 전달
- 메세지 바디를 사용해서 데이터를 전달할 수 있지만, 지원하지 않는 곳이 많아서 권장하지 않음.
- 서버끼리는 `GET으로 보내면 캐싱을 하겠다` 약속을 함.

#### POST
- 요청 데이터 처리
- **메시지 바디를 통해 서버로 요청 데이터 전달**
- 서버는 요청 데이터를 `처리`
    - 메시지 바디를 통해 들어온 데이터를 처리하는 모든 기능을 수행한다.
- 주로 전달된 `데이터로 신규 리소스 등록, 프로세스 처리`에 사용
- 신규로 생성됐을 경우, 201 Created로 / Header에 Location(신규 생성된 path)를 추가해서 응답 메세지 보냄.

**[요청 데이터를 처리하는 예시]**
- HTML FORM에 입력된 필드와 같은 데이터 블록을 데이터 처리 프로세스에 제공
- 게시판, 뉴스 그룹, 메일링 리스트, 블로그 또는 유사한 기사 그룹에 메시지 게시
- 서버가 아직 식별하지 않은 새 리소스 생성
- 기존 자원에 데이터 추가
- 정리 : 리소스 URI에 POST 요청이 오면 요청 데이터를 어떻게 처리할지 리소스마다 따로 정해야 함.(딱 정해진 게 없음)

**[정리]**
1) 새 리소스 생성(등록)
2) 요청 데이터 처리
    - 예) 주문에서 '결제완료 -> 배달시작 -> 배달완료'처럼 단순히 값 변경을 넘어 프로세스의 상태가 변경되는 경우
    - POST의 결과로 새로운 리소스가 생성되지 않을 수도 있음
        - 예) POST /orders/{orderId}/start-delivery **(컨트롤 URI)**
3) 다른 메서드로 처리하기 애매한 경우
    - JSON으로 조회 데이터를 넘겨야하는데, GET 메서드를 사용하기 어려운 경우.

---

### HTTP 메서드 - PUT, PATCH, DELETE
#### PUT
- 리소스를 `완전히` 대체
    - 리소스가 있으면 대체, 없으면 생성.
- **중요! 클라이언트가 리소스를 식별**
    - 클라이언트가 리소스 위치를 알고 URI 지정
    - POST와 차이점(POST는 URI를 모르고 요청을 전송하지만 POST는 리소스의 위치를 전체 알고 전송한다.)
    

**!!주의!!**

리소스가 존재하는 상태에서 PUT을 날리면 리소스가 **완전히 대체된다.**

기존 내용을 삭제하고 새로운 리소스로 바꿈.

#### PATCH
- 리소스 부분 변경
- PATCH를 지원하지 않는 서버의 경우는 POST를 사용하여 리소스를 수정할 수 있다.

#### DELETE
- 리소스 제거

---

### HTTP 메서드의 속성
- 안전(Safe Methods)
- 멱등(Idempotent Methods)
- 캐시가능(Cacheable Methods)

#### 안전(Safe)
- 호출해도 리소스가 변경되지 않는다.
- GET, HEAD(, OPTIONS, TRACE)(O) / POST, DELETE, PUT, PATCH(X)
- 만약 계속 호출하면 로그가 쌓여서 장애가 발생하지 않나요? / 안전은 그런 부분은 고려하지 않음. 리소스 자체가 고려함.

#### 멱등(Idempotent)
- f(f(x)) = f(x)
- N번 호출하면, N번 전부 `결과가 똑같다.`
- **멱등은 같은 사용자가 똑같은 요청을 여러번 했을 때만을 고려한다.**
- 멱등에 해당하는 HTTP 메서드
    - GET : 조회를 여러번 해도 같은 결과가 조회된다.
    - PUT : 결과를 대체한다. 따라서 같은 요청을 여러번 해도 최종 결과는 같다.
    - DELETE : 결과를 삭제한다. 같은 요청을 여러번 해도 삭제된 결과는 똑같다.
    - <span style="color:red;font-weight:bold;">POST</span> : 멱등하지 않음. 두 번 호출하면 같은 결제가 중복해서 발생할 수 있다.
- 활용
    - 자동 복구 메커니즘
    - 서버가 TIMEOUT 등으로 정상 응답을 못주었을 때, `클라이언트가 같은 요청을 다시 해도 되는가`가 판단 근거.

#### 캐시가능(Cacheable)
- 응답결과 리소스를 캐시해서 사용해도 되는가?
- GET, HEAD(, POST, PATCH) 가능.
    - POST, PATCH는 본문까지 캐시 키로 고려해야 하는데, 구현이 쉽지 않음.

## Section 5. HTTP 메서드 활용
    [목차]
    - 클라이언트에서 서버로 데이터 전송
    - HTTP API 설계 예시

### 클라이언트에서 서버로 데이터 전송
데이터 전달 방식은 크게 `2가지`
- 쿼리 파라미터를 통한 데이터 전송
    - GET : 주로 정렬 필터(검색어)
- 메세지 바디를 통한 데이터 전송
    - POST, PUT, PATCH 예) 회원가입, 상품 주문, 리소스 등록/변경

```
예시 4가지 상황
- 정적 데이터 조회
- 동적 데이터 조회
- HTML Form을 통한 데이터 전송
- HTML API를 통한 데이터 전송
```

#### 정적 데이터 조회
쿼리 파라미터 미사용
- 이미지, 정적 텍스트 문서
- 일반적으로 쿼리 파라미터 없이 리소스 경로로 단순하게 조회 가능

#### 동적 데이터 조회
쿼리 파라미터 사용
- 주로 검색, 게시판 목록에서 정렬 필터(검색어)
- 조회 조건을 줄여주는 `필터`, 조회 결과를 정렬하는 `정렬 조건`에 주로 사용
- 조회는 `GET` 사용
- GET은 쿼리 파라미터 사용해서 데이터를 전달

#### HTML Form을 통한 데이터 전송
POST 전송 - 저장

<img alt="웹 브라우저가 생성한 요청 HTTP 메시지" src="./img/HTTP POST request message example.png" style="width:70%; height:100%">

HTTP 요청 메시지에
- Header에 `Content-Type : application/x-www-form-urlencoded`를 추가
- message body에 `username=kim&age=20`를 추가하여 생성.

<br/>

multipart/form-data <- 파일 전송에 사용하는 content-type

<img alt="form data code" src="./img/HTTP POST form data file.png" style="width:70%;height:100%">

<img alt="form data message body" src="./img/HTTP POST form data file-HTTP message.png" style="width:70%;height:100%">

**=> 주로 binary 데이터 전송때 사용함.**


    [정리]
    - HTML Form submit시 POST 전송
    - Content-Type: application/x-www-form-unlencoded 사용
        - form의 내용을 메시지 바디를 통해서 전송(key=value, 쿼리 파라미터 형식)
        - 전송 데이터를 url encoding 처리
    - HTML Form은 GET 전송도 가능
    - Content-Type: multipart/form-data
        - 파일 업로드 같은 바이너리 데이터 전송시 사용
        - 다른 종류의 여러 파일과 폼의 내용 함께 전송 가능(그래서 이름이 multipart)
    - 참고 : HTML Form 전송은 GET, POST만 지원

#### HTML API를 통한 데이터 전송
- 서버 to 서버 (백엔드 시스템 통신)
- 앱 클라이언트 (아이폰, 안드로이드)
- 웹 클라이언트
    - HTML에서 Form 전송 대신 JS를 통한 통신에 사용(AJAX)
    - 예) React, VueJS 같은 웹 클라이언트와 API 통신
- POST, PUT, PATCH : 메시지 바디를 통해 데이터 전송
- GET : 조회, 쿼리 파라미터로 데이터 전달
- Content=Type : application/json을 주로 사용 (사실상 표준)
    - TEXT, XML, JSON 등

---

### HTTP API 설계 예시
- HTTP API - 컬렉션
    - POST 기반 등록
- HTTP API - 스토어
    - PUT 기반 등록
- HTML FORM 사용
    - 웹 페이지 회원 관리 (GET, POST만 지원)

#### 회원 관리 시스템
    API 설계 - POST 기반 등록
    - 회원 목록 /members -> GET
    - 회원 등록 /members -> POST
    - 회원 조회 /members/{id} -> GET
    - 회원 수정 /members/{id} -> PATCH, PUT, POST
    - 회원 삭제 /members/{id} -> DELETE

POST - 신규 자원 등록 특징
- 클라이언트는 등록될 리소스의 URI를 모른다.
    - 회원 등록 /members -> POST
    - POST /members
- 서버가 새로 등록된 리소스 URI를 생성해준다.
    - HTTP/1.1 201 Created<br/>Location: /members/100
- 컬렉션(Collection)
    - 서버가 관리하는 리소스 디렉토리
    - 서버가 리소스의 URI를 생성하고 관리
    - 여기서 컬렉션은 /members

#### 파일 관리 시스템
    API 설계 - PUT 기반 등록
    - 파일 목록 /files -> GET
    - 파일 조회 /files/{filename} -> GET
    - 파일 등록 /files/{filename} -> PUT
    - 파일 삭제 /files/{filename} -> DELETE
    - 파일 대량 등록 /files -> POST

PUT - 신규 자원 등록 특징
- 클라이언트가 리소스 URI를 알고 있어야 한다.
    - 파일 등록 /files/{filename} -> PUT
    - PUT /files/star.jpg
- 클라이언트가 직접 리소스의 URI를 지정한다.
- 스토어(Store)
    - 클라이언트가 관리하는 리소스 저장소
    - 클라이언트가 리소스의 URI를 알고 관리
    - 여기서 스토어는 /files

#### HTML FORM 사용
- HTML FORM은 `GET, POST만 지원`
- AJAX 같은 기술을 사용해서 해결 가능 -> 회원 API 참고
- 여기서는 순수 HTML, HTML FORM 이야기
- GET, POST만 지원하므로 제약이 있음.
- 컨트롤 URI
    - GET, POST만 지원하므로 제약이 있음.
    - 이런 제약을 해결하기 위해 동사로 된 리소스 경로 사용
    - POST의 /new, /edit, /delete가 컨트롤 URI
    - HTTP 메서드로 해결하기 애매한 경우 사용(HTTP API 포함)
<br/><br/>

```
API 설계
- 회원 목록 /members -> GET
- 회원 등록 폼 /members/new -> GET
- 회원 등록 /members/new, /members -> POST
- 회원 조회 /members/{id} -> GET
- 회원 수정 폼 /members/{id}/edit -> GET
- 회원 수정 /members/{id}/edit, /members/{id} -> POST
- 회원 삭제 /members/{id}/delete -> POST
```

#### 정리 <a href="https://restfulapi.net/resource-naming" target="_blank">참고링크</a>
- HTTP API - 컬렉션
    - POST 기반 등록
    - **서버가 리소스 URI 결정**
- HTTP API - 스토어
    - PUT 기반 등록
    - **클라이언트가 리소스 URI 결정**
- HTML FORM 사용
    - 순수 HTML + HTML Form 사용
    - GET, POST만 지원

    [참고하면 좋은 URI 설계 개념]
    - 문서(Document)
        - 단일 개념(파일 하나, 객체 인스턴스, 데이터베이스 row)<br/>예) /members/100, /files/star.jpg
    - 컬렉션(Collection)
        - 서버가 관리하는 리소스 디렉터리
        - 서버가 리소스의 URI를 생성하고 관리<br/>예) /members
    - 스토어(Store)
        - 클라이언트가 관리하는 자원 저장소
        - 클라이언트가 리소스의 URI를 알고 관리<br/>예) /files
    - 컨트롤러(Controller), 컨트롤 URI
        - 문서, 컬렉션, 스토어로 해결하기 어려운 추가 프로세스 실행
        - 동사를 직접 사용<br/>예) /members/{id}/delete

## Section 6. HTTP 상태코드
    [목차]
    - HTTP 상태코드 소개
    - 2XX - 성공
    - 3XX - 리다이렉션1
    - 3XX - 리다이렉션2
    - 4XX - 클라이언트 오류, 5XX - 서버 오류

### HTTP 상태코드 소개
#### 상태 코드
클라이언트가 보낸 요청의 처리 상태를 응답에서 알려주는 기능
- 1XX(Informational) : 요청이 수신되어 처리중 (거의 사용하지 않음)
- 2XX(Successful) : 요청 정상 처리
- 3XX(Redirection) : 요청을 완료하려면 추가 행동이 필요
- 4XX(Client Error) : 클라이언트 오류, 잘못된 문법등으로 서버가 요청을 수행할 수 없음
- 5XX(Server Error) : 서버 오류, 서버가 정상 요청을 처리하지 못함

**만약 모르는 상태 코드가 나타나면?**
- 클라이언트는 상위 상태코드로 해석해서 처리
- 미래에 새로운 상태코드가 추가되어도 클라이언트를 변경하지 않아도 됨.<br/>
예) 299 ??? -> 2XX(Successful)<br/>451 ???-> 4XX(Client Error)<br/>599 ??? -> 5XX(Server Error)

---

### 2XX - 성공
클라이언트의 요청을 성공적으로 처리
- 200 OK
- 201 Created
- 202 Accepted
- 204 No Content

#### 200 OK - 요청 성공

#### 201 Created - 요청 성공해서 새로운 리소스가 생성됨
생성된 리소스는 `응답의 Location 헤더 필드`로 식별

#### 202 Accepted - 요청이 접수되었으나 처리가 완료되지 않았음
- 배치 처리 같은 곳에서 사용

#### 204 No Content - 서버가 요청을 성공적으로 수행했지만, 응답 페이로드 본문에 보낼 데이터가 없음
- 예) 웹 문서 편집기에서 save 버튼
- save 버튼의 결과로 아무 내용이 없어도 된다.
- save 버튼을 눌러도 같은 화면을 유지해야 한다.
- 결과 내용이 없어도 204 메시지(2xx)만으로 성공을 인식할 수 있다.

--- 

### 3XX - 리다이렉션
요청을 완료하기 위해 유저 에이전트의 추가 조치 필요
- 300 Multiple Choices <span style="color:red;font-weight:bold;">거의 쓰지 않음</span>
- 301 Moved Permanently
- 302 Found
- 303 See Ohter
- 304 Not Modified
- 307 Temporary Redirect
- 308 Permanent Redirect

#### 리다이렉션 이해
- 웹 브라우저는 3XX 응답의 결과에 Location 헤더가 있으면, Location 위치로 자동 이동(=리다이렉트)

[종류]
- 영구 리다이렉션 - 특정 리소스의 URI가 영구적으로 이동<br/>
예) /members -> /users, /event -> /new-event
- 일시 리다이렉션 - 일시적인 변경<br/>
예) 주문 완료 후 주문 내역 화면으로 이동<br/>
PRG : Post/Redirect/Get
- 특수 리다이렉션<br/>결과 대신 캐시를 사용

#### 영구 리다이렉션 - 301, 308
- 리소스의 URI가 영구적으로 이동
- 원래의 URL를 사용 X, 검색 엔진 등에서도 변경 인지
- 301 Moved Permanently
    - 리다이렉트시 `요청 메서드가 GET으로 변하고, 본문이 제거될 수 있음(MAY)`
- 308 Permanent Redirect
    - 301과 기능은 같음
    - 리다이렉트시 `요청 메서드와 본문 유지` **(처음 POST를 보내면 리다이렉트도 POST 유지)**

---

### 일시적인 리다이렉션 - 302, 307, 303
- 리소스의 URI가 일시적으로 변경
- 따라서 검색 엔진 등에서 URL을 변경하면 안됨
- 302 Found : 리다이렉트시 `요청 메서드가 GET으로 변하고, 본문이 제거될 수 있음(MAY)`
- 307 Temporary Redirect(302와 유사) : 리다이렉트시 `요청 메서드와 본문 유지`(요청 메서드를 변경하면 안된다. **MUST NOT**)
- 303 See Other(302와 유사) : 리다이렉트시 `요청 메서드가 GET으로 변경`

#### PRG : Post/Redirect/Get
일시적인 리다이렉션 - 예시

**POST로 주문 후에 웹 브라우저를 새로고침하면?**

- 새로고침은 다시 요청 -> 중복 주문이 될 수 있다.
- POST로 주문후에 새로 고침으로 인한 중복 주문 방지
- POST로 주문후에 주문 결과 화면을 GET 메서드로 리다이렉트
- 새로고침해도 결과 화면을 GET으로 조회
- 중복 주문 대신에 결과 화면만 GET으로 다시 요청
- PRG 이후 리다이렉트
    - URL이 이미 POST -> GET으로 리다이렉트 됨
    - 새로고침 해도 GET으로 결과 화면만 조회

#### 그래서 뭘 써야 하나요?
- 정리
    - 302 -> GET으로 변할 수 있음.
    - 307 -> 메서드가 변하면 안됨.
    - 303 -> 메서드가 GET으로 변경
- 역사
    - 처음 302 스펙의 의도는 HTTP 메서드를 유지하는 것
    - 그런데 웹 브라우저들이 대부분 GET으로 바꾸어버림(일부는 다르게 동작)
    - 그래서 모호한 302를 대신하는 307, 303이 등장함(301 대응으로 308도 등장)
- 현실
    - 307, 303을 권장하지만 이미 많은 애플리케이션 라이브러리들이 302를 기본으로 사용.
    - 자동 리다이렉션시에 GET으로 변해도 되면 그냥 302를 사용해도 큰 문제 없음.

#### 기타 리다이렉션 - 300, 304
- 300 Multiple Choiees : 안씀.
- 304 Not Modified
    - `캐시를 목적`으로 사용
    - 클라이언트에게 리소스가 수정되지 않았음을 알려준다.<br/>따라서 클라이언트는 로컬PC에 저장된 캐시를 재사용한다. (캐시로 리다이렉트 한다.)
    - **304 응답은 메시지 바디를 포함하면 안된다.** (로컬 캐시를 사용해야 하므로)
    - 조건부 GET, HEAD 요청시 사용

---

### 4XX - 클라이언트 오류, 5XX - 서버 오류
#### 4XX(Client Error)
- 클라이언트의 요청에 잘못된 문법 등으로 서버가 요청을 수행할 수 없음
- **오류의 원인이 클라이언트에 있음**
- **중요! 클라이언트가 이미 잘못된 요청, 데이터를 보내고 있기 때문에, 똑같은 재시도가 실패함.**

#### 400 Bad Request
클라이언트가 잘못된 요청을 해서 서버가 요청을 처리할 수 없음
- 요청 구문, 메시지 등등 오류
- 클라이언트는 요청 내용을 다시 검토하고, 보내야함<br/>예) 요청 파라미터가 잘못되거나 API 스펙이 맞지 않을 때

#### 401 Unauthorized
클라이언트가 해당 리소스에 대한 인증이 필요함
- 인증(Authentication) 되지 않음
- 401 오류 발생시 응답에 `WWW-Authenticate` 헤더와 함께 인증 방법을 설명
- 참고
    - 인증(Authentication) : 본인이 누구인지 확인(로그인)
    - 인가(Authorization) : 권한부여(ADMIN 권한처럼 특정 리소스에 접근할 수 있는 권한, `인증이 있어야 인가가 있음`)
    - 오류 메시지가 Unauthorized 이지만 인증 되지 않음(<u>이름이 아쉬움</u>)

#### 403 Forbidden
서버가 요청을 이해했지만 승인을 거부함
- 주로 인증 자격 증명은 있지만, 접근 권한이 불충분한 경우
- USER 권한인데 ADMIN 등급의 리소스에 접근하는 경우

#### 404 Not Found
요청 리소스를 찾을 수 없음
- 요청 리소스가 서버에 없음
- 또는 클라이언트가 권한이 부족한 리소스에 접근할 때 해당 리소스를 숨기고 싶을 때

### 5XX(Server Error)
- 서버 문제로 오류 발생
- 서버에 문제가 있기 때문에 재시도하면 성공할 수도 있음(복구가 되거나 등등)

#### 500 Internal Server Error
서버 문제로 오류 발생, 애매하면 500 오류

#### 503 Service Unavailable
서비스 이용 불가
- 서버가 일시적인 과부하 또는 예정된 작업으로 잠시 요청을 처리할 수 없음.
- Retry-After 헤더 필드로 얼마뒤에 복귀되는지 보낼 수도 있음.
