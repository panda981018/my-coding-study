# 모든 개발자를 위한 HTTP 웹 기본 지식 - 김영한

## Section 1. 인터넷 네트워크
    [요약]
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

__URN 이름만으로 실제 리소스를 찾을 수 있는 방법이 보편화 되지 않음__

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

### 클라이언트 서버 구조
- Request-Response 구조
- 클라이언트가 HTTP메세지로 서버에 Request를 보냄. 응답을 대기
- 서버가 요청에 대한 결과를 만들어서 응답
- 비즈니스 로직, 데이터를 서버에 몰아넣음. 클라이언트는 UI와 사용성에 집중 <br/>**=> 클라이언트/서버가 각각 독립적으로 진화 가능**

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

---
