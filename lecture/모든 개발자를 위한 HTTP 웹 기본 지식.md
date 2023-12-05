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

---
## Section 2. URI와 웹 브라우저 요청 흐름
    [목차]
    - URI
    - 웹 브라우저 요청 흐름

### URI (Uniform Resource Identifier)

#### URI? URL? URN?
"URLDMS 로케이터(Locator), 이름(Name) 또는 둘 다 추가로 분류될 수 있다"

URL(Uniform Resource Locator) : 자원의 위치

URN(Uniform Resource Name) : 자원의 이름

```
Uniform : 리소스 식별하는 통일된 방식
Resource : 자원, URI로 식별할 수 있는 모든 것(제한 없음) ex) HTML파일, 실시간 교통정보 등
Identifier : 다른 항목과 구분하는데 필요한 정보
```

URL - Locator : 리소스가 있는 위치를 지정

URN - Name : 리소스에 이름을 부여

위치는 변할 수 있지만, 이름은 변하지 않는다.

urn:isbn:8960777331 (어떤 책의 isbn URN)

URN 이름만으로 실제 리소스를 찾을 수 있는 방법이 보편화 되지 않음

#### URL 전체 문법
scheme://[userinfo@]host[:port][/path][?query][#fragment]

ex) https://www.google.com:443/search?q=hello?hl=ko

    - 프로토콜 : https
    - 호스트명 : www.google.com
    - 포트 번호 : 443
    - 패스 : /search
    - 쿼리 파라미터 : q=hello&hl=ko

scheme : 주로 프로토콜 사용 (https)
- 프로토콜(protocol) : 어떤 방식으로 자원에 접근할 것인가 하는 약속 규칙 ex) http, https, ftp 등

[기본 포트]
http는 80, https는 443, 포트는 생략 가능

https는 http에 보안이 추가됨 (HTTP Secure)

