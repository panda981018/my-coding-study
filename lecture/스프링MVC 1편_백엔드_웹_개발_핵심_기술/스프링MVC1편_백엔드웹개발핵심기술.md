# 스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술

[강의 링크](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1/dashboard)

## Section 1. 웹 애플리케이션 이해
### 웹 서버, 웹 애플리케이션 서버

#### 웹 서버(Web Server)
- HTTP 기반으로 동작
- 정적 리소스 제공, 기타 부가기능
- 예) NGINX, APACHE

#### 웹 애플리케이션 서버(WAS - Web Application Server)
- HTTP 기반으로 동작
- 웹 서버 기능 포함됨
- 프로그램 코드를 실행해서 로직을 수행할 수 있음
    - 동적 HTML, HTTP API(JSON)
    - 서블릿, JSP, 스프링 MVC
- 예) 톰캣(Tomcat), Jetty, Undertow

#### Web Server VS WAS
- 웹 서버는 정적 / WAS는 애플리케이션 로직
- 자바는 서블릿 컨테이너 기능을 제공하면 `WAS`
    - 서블릿 없이 자바코드를 실행하는 서버 프레임워크도 있음
- WAS는 `애플리케이션 코드를 실행하는데 더 특화`

#### 웹 시스템 구성 - WAS, DB
- WAS, DB만으로도 구성 가능
- WAS만 이쓰면 너무 많은 역할을 담당 **=> 서버 과부하 우려**
- 가장 비싼 애플리케이션 로직이 정적 리소스 때문에 수행이 어려울 수 있음
- WAS 장애시 오류 화면도 노출 불가능

#### 웹 시스템 구성 - WEB, WAS, DB
- 정적 리소스는 웹 서버가 처리
- 웹 서버는 애플리케이션 로직같은 동적인 처리가 필요하면 WAS에 요청을 위임
- WAS는 `중요한 애플리케이션 로직 처리 전담`
- 효율적인 리소스 관리
    - 정적 리소스가 많이 사용되면 Web 서버 증설
    - 애플리케이션 리소스가 많이 사용되면 WAS 증설
- WAS, DB 장애시 WEB 서버가 오류 화면 제공 가능

---

### 서블릿
**특징**
- `urlPatterns(/hello)`의 URL이 호출되면 서블릿 코드가 실행
- `HttpServletRequest` : HTTP 요청 정보를 편리하게 사용할 수 있음 
- `HttpServletResponse` : HTTP 응답 정보를 편리하게 제공할 수 있음 
- 개발자는 HTTP 스팩을 매우 편리하게 사용

#### 서블릿 컨테이너
- 톰캣처럼 서블릿을 지원하는 WAS를 `서블릿 컨테이너`라고 함
- 생성, 호출, 관리를 모두 담당
- 서블릿 객체는 **싱글톤으로 관리 (공유 변수는 사용 주의)**
- JSP도 서블릿으로 변환되어서 사용
- 동시 요청을 위한 `멀티 쓰레드 처리 지원`

---
### 동시 요청 - 멀티 쓰레드 (백엔드 개발자한테 매우매우 중요)

#### 쓰레드
애플리케이션 코드를 하나하나 순차적으로 실행하는 것
- 쓰레드가 없다면 자바 애플리케이션 실행이 불가능
- 쓰레드는 한번에 하나의 코드 라인만 수행
- 동시 처리가 필요하면 쓰레드를 추가로 생성

#### 요청마다 쓰레드를 생성한다?
**장점**
- 동시 요청을 처리할 수 있음
- 리소스(CPU, 메모리)가 허용할 때까지 처리가능
- 하나의 쓰레드가 지연되어도, 나머지 쓰레드는 정상 동작한다.

**단점**
- **생성 비용이 매우 비싸다**
- **컨텍스트 스위칭 비용이 발생한다**
- 쓰레드 생성에 제한이 없다.
    - 고객 요청이 많이 오면, CPU, 메모리 임계점을 넘어서 서버가 죽을 수 있다.


#### 쓰레드 풀
요청마다 쓰레드 생성의 단점 보완

**특징**
- 필요한 쓰레드를 쓰레드 풀에 보관하고 관리한다.
- 쓰레드 풀에 생성 가능한 쓰레드의 최대치를 관리한다. 톰캣은 최대 200개가 기본값.

**사용**
- 쓰레드가 필요하면 이미 생성된 풀에서 꺼내서 사용함
- 사용을 종료하면 풀에 쓰레드를 반납함
- 최대 쓰레드가 모두 사용중이어서 풀에 사용할 수 있는 쓰레드가 없으면 기다리는 요청은 거절하거나 특정 숫자만큼만 대기하도록 설정할 수 있다.

**장점**
- 쓰레드가 미리 생성되어 있으므로, 쓰레드를 생성하고 종료하는 비용(CPU)이 절약되고, 응답 시간이 빠름
- 생성 가능한 쓰레드의 최대치가 있으므로 너무 많은 요청이 들어와도 기존 요청은 안전하게 처리할 수 있다

**!!실무 팁!!**
- WAS의 주요 튜닝 포인트는 `max thread 수`
- 너무 낮게 설정하면 리소스는 여유롭지만 클라이언트는 금방 응답 거절당함
- 너무 높게 설정하면 CPU, 메모리 리소스 임계점 초과로 서버 다운
- 장애 발생시
    - 클라우드면 서버부터 늘리고 이후에 튜닝
    - 온프레미스는 열심히 튜닝
- 적정 숫자는 애플리케이션의 복잡도, CPU, 메모리, IO 리소스 상황에 따라 모두 다름
- 성능 테스트 : 최대한 실제 서비스와 유사하게 성능 테스트 시도
    - 툴 : APACHE AB, JMETER, nGrinder(추천)

#### WAS 멀티 쓰레드 지원의 핵심
- 개발자가 멀티 쓰레드 관련 코드를 신경쓰지 않아도 됨
- 싱글 쓰레드 프로그램이을 하듯이 편리하게 소스 코드 개발
- 싱글톤 객체는 주의해서 사용하기

---

### HTML, HTTP API, CSR, SSR

#### HTTP API
- HTML이 아니라 데이터를 전달
- 주로 `JSON 형식` 사용
- 다양한 시스템에서 호출
- 웹 클라이언트(React.js, Vue.js 등)-서버 / 서버-서버 / 앱 클라이언트-서버

#### SSR, CSR

1. SSR(Server Side Rendering)
- HTML 최종 결과를 서버에서 만들어서 웹 브라우저에 전달
- 주로 정적인 화면에 사용
- JSP, Thymeleaf -> **백엔드 개발자가 개발**

2. CSR(Client Side Rendering)
- HTML 결과를 JS를 사용해 웹 브라우저에서 동적으로 생성해서 적용
- 주로 동적인 화면에 사용.
- 예) 구글지도, Gmail, 구글 캘린더
- React, Vue.js -> **웹 프론트엔드 개발자가 개발**

> 참고 : React, Vue.js를 CSR + SSR 동시에 지원하는 웹 프레임워크도 있음
SSR을 사용하더라도, JS를 사용해서 화면 일부를 동적으로 변경 가능

---

### 자바 백엔드 웹 기술 역사

#### 과거 기술
- Servlet(1997) : HTML 생성이 어려움
- JSP(1999) : HTML 생성은 편리하지만, 비즈니스 로직까지 너무 많은 역할을 담당
- Servlet, JSP 조합 MVC 패턴 사용 : Model, View Controller로 역할을 나누어 개발
- MVC 프레임워크 춘추 전국시대(2000년초~2010년초)
    - MVC패턴 자동화, 복잡한 웹 기술을 편리하게 사용할 수 있는 다양한 기능 지원
    - 스트럿츠, 웹워크, 스프링 MVC(과거 버전)


#### 현재 사용 기술
- Annotation 기반의 스프링 MVC 등장
- 스프링 부트의 등장
    - 서버를 내장
    - 과거에는 서버에 WAS를 설치하고, 소스는 war 파일을 만들어서 설치한 WAS에 배포
    - 스프링 부트는 빌드 결과(jar)에 WAS 서버 포함 -> 빌드 배포 단순화

#### 최신 기술 - 스프링 웹 기술의 분화
- Web Servlet : Spring MVC (일반적으로 사용하는 것)
- Web Reactive : Spring WebFlux

#### Spring WebFlux
- 특징
    - 비동기 Non-blocking
    - 최소 쓰레드로 최대 성능 - 쓰레드 컨텍스트 스위칭 비용 효율화
    - 함수형 스타일로 개발 - 동시처리 코드 효율화
    - 서블릿 기술 사용 X (Netty라는 웹 프레임워크를 사용해서 구현됨)
- 단점
    - **기술적 난이도 매우 높음**
    - 아직은 RDB 지원 부족 (Redis, Elasticsearch, DynamoDB, MongoDB는 지원 가능)
    - 일반 MVC의 쓰레드 모델도 충분히 빠르다
    - 실무에서 아직 많이 사용하지는 않음(전체 1% 이하)

#### 자바 뷰 템플릿 역사
HTML을 편리하게 생성하는 뷰 기능
- JSP : 속도 느림, 기능 부족
- 프리마커(FreeMarker), 벨로시티(Velocity) : 속도 문제 해결, 다양한 기능
- 타임리프(Thymeleaf)
    - 내추럴 템플릿 : HTML의 모양을 유지하면서 뷰 템플릿 적용 가능
    - 스프링 MVC와 강력한 기능 통합
    - **최선의 선택**, 단 성능은 프리마커, 벨로시티가 더 빠름

## Section 2. 서블릿
### 프로젝트 생성

war를 선택해야 JSP를 사용할 수 있음.
war는 보통 톰캣 서버를 따로 설치할 때 사용.

---

### Hello 서블릿

> 참고 : 서블릿은 '톰캣 같은 WAS를 직접 설치 -> 그 위에 서블릿 코드를 .class로 빌드한 것을 올린다 -> 톰캣 서버를 실행한다' 이 과정을 해야하지만 너무 번거롭기 때문에 스프링 부트 위에 서블릿 코드를 실행할 것이다.

`@WebServlet` 서블릿 애노테이션

- name : 서블릿 이름 **(중복 X)**
- urlPatterns : URL 매핑 **(중복 X)**

---

### HttpServletRequest - 개요
#### HttpServletRequest의 역할
서블릿은 개발자가 HTTP 요청 메시지들을 편리하게 사용할 수 있도록 개발자 대신에 HTTP 요청 메시지를 파싱하고 그 결과를 `HttpServletRequest` 객체에 담아서 제공한다.

#### 추가 기능
- 임시 저장소 기능 : HTTP 요청의 시작부터 끝까지 유지되는 임시 저장소 기능
    - 저장 : `request.setAttribute(name, value)`
    - 조회 : `request.getAttribute(name)`

#### 세션 관리 기능
- `request.getSession(create: true)`

---
### HTTP 요청 데이터 - 개요
클라이언트에서 서버로 데이터를 전달하는 방법

1. GET - 쿼리 파라미터
- message body 없이 URL의 쿼리 파라미터에 데이터를 포함해서 전달
- 예) 검색, 필터, 페이징 등에서 많이 사용하는 방식

2. POST - HTML Form
- content-type: application/x-www-form-urlencoded
- message body에 쿼리 파라미터 형식으로 전달
- 예) 회원 가입, 상품 주문, HTML Form 사용

**❗ HTML Form 데이터를 전송할 때 PUT or PATCH 메서드를 사용해서는 안된다. POST만 가능 ❗**

3. HTTP message body에 데이터를 직접 담아서 요청
- HTTP API에서 주로 사용. (JSON, XML, TEXT)
- 데이터 형식은 주로 JSON 사용 (POST, PUT, PATCH)

---
### HTTP 요청 데이터 - GET 쿼리 파라미터

message body 없이, URL의 쿼리 파라미터를 사용해서 데이터를 전달하자.

URL 뒤에 `?`를 붙이면 **쿼리파라미터 시작**, `&`를 붙이면 **추가 파라미터 작성**

`request.getParameter()` : 하나의 파라미터 이름에 대해서 하나의 값만 있을 때 사용.

`request.getParameterValues()` : 하나의 파라미터 이름에 대한 2개 이상의 값이 있을 때 사용.

---
### HTTP 요청 데이터 - POST HTML Form

**특징**
- content-type: `application/x-www-form-urlencoded`
- message body에 쿼리 파라미터 형식으로 데이터를 전달한다.

`application/x-www-form-urlencoded` 형식은 앞서 GET에서 살펴본 쿼리 파라미터 형식과 같다. 따라서 **쿼리 파라미터 조회 메서드를 그대로 사용**하면 된다.

> 참고 : content-type은 HTTP 메시지 바디의 데이터 형식을 지정한다.
**GET URL 쿼리 파라미터 형식**으로 클라이언트에서 서버로 데이터를 전달할 때는 HTTP 메시지 바디를 사용하지 않기 때문에 content-type이 없다.
**POST HTML Form 형식**으로 데이터를 전달하면 HTTP 메시지 바디에 해당 데이터를 포함해서 보내기 때문에 바디에 포함된 데이터가 어떤 형식인지 content-type을 꼭!! 지정해야 한다.

---
### HTTP 요청 데이터 - API 메시지 바디 - 단순 텍스트

HTTP message body에 데이터를 직접 담아서 요청
- HTTP API에서 주로 사용, JSON, XML, TEXT(POST, PUT, PATCH 메서드에서 사용)
- 데이터 형식 대부분 JSON
- HTTP 메시지 바디의 데이터를 InputStream을 사용해서 직접 읽을 수 있다.

> 참고 : InputStream은 byte코드를 리턴한다. 따라서 우리가 보기 쉬울려면 인코딩
---
### HTTP 요청 데이터 - API 메시지 바디 - JSON

#### JSON 형식 전송
- content-type: application/json
- message body : {"username" : "hello", "age" : 20}

---
### HttpServletResponse - 기본 사용법

#### [역할]

**HTTP 응답 메시지 생성**
- HTTP 응답코드 지정
- 헤더 생성
- 바디 생성

**편의기능 제공**
- Content-Type, 쿠키, Redirect

---
### HTTP 응답 데이터 - 단순 텍스트, HTML
HTTP 응답 메시지에는 아래와 같은 방식으로 데이터를 전송한다.
- 단순 텍스트 응답
- HTML 응답
- HTTP API - MessageBody JSON 응답

```java
@WebServlet(name = "responseHtmlServlet", urlPatterns = "/response-html")
public class ResponseHtmlServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Content-Type : text/html;charset=utf-8;
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<body>");
        writer.println("    <div>안녕?</div>");
        writer.println("</body>");
        writer.println("</html>");
    }
}
```

---
### HTTP 응답 데이터 - API JSON

> 참고 : `application/json`은 스펙상 utf-8 형식을 사용하도록 정의되어 있다. 그래서 charset=utf-8 같은 파라미터를 추가해주지 않아도 된다.(지원하지도 않음)

## Section 3. 서블릿, JSP, MVC 패턴
### 회원 관리 웹 애플리케이션 요구사항
**회원 정보**

이름: `username`

나이: `age`

**기능 요구사항**
- 회원 저장
- 회원 목록 조회


---
### 서블릿으로 회원 관리 웹 애플리케이션 만들기
(servlet으로 만든 예제 코드는 [여기](https://github.com/panda981018/my-coding-study/tree/lecture/spring-mvc-1/lecture/%EC%8A%A4%ED%94%84%EB%A7%81MVC%201%ED%8E%B8_%EB%B0%B1%EC%97%94%EB%93%9C_%EC%9B%B9_%EA%B0%9C%EB%B0%9C_%ED%95%B5%EC%8B%AC_%EA%B8%B0%EC%88%A0/project/servlet/src/main/java/hello/servlet/basic)에 있습니다!)

#### 서블릿으로만 구현하면서 겪은 불편함
서블릿과 자바 코드만으로 HTML을 만들어보니, 동적으로 HTML파일을 생성할 수 있었으나 **매우 복잡하고 비효율적**이다.
차라리 HTML에서 동적인 부분만 자바 코드로 변경한다면 조금 더 편할 것이다.

이러한 이유로 `템플릿 엔진이 등장한 것이다.` 템플릿 엔진을 사용하면 HTML 문서에서 필요한 부분에만 코드를 적용해서 동적으로 변경할 수 있다.

템플릿 엔진의 예시) JSP, Thymeleaf, Freemarker, Velocity 등

> 참고 : JSP는 성능, 기능면에서 다른 템플릿엔진에 밀리는 상황이라 권장하지 않는 중이다.

---
### JSP로 회원 관리 웹 애플리케이션 만들기

**스프링부트 3.0 미만**
```gradle
implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
implementation 'javax.servlet:jstl'
```

**스프링부트 3.0 이상**
```gradle
implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
implementation 'jakarta.servlet:jakarta.servlet-api'
implementation 'jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api'
implementation 'org.glassfish.web:jakarta.servlet.jsp.jstl'
```
#### JSP 문법
- `<%@ page import="" %>` : 자바의 import문과 같음
- `<% ~~ %>` : 내부에 자바 코드를 입력할 수 있다.
- `<%= ~~ %>` : 자바 코드를 출력할 수 있다.

#### 서블릿과 JSP의 한계
서블릿으로 개발할 때는 뷰 화면을 위한 HTML을 만드는 작업이 자바 코드에 섞여서 지저분하고 복잡했다.
그리고 JSP로 개발할 때는 HTML 작업은 깔끔해졌으나, 중간에 자바 코드가 들어가면서 JSP가 너무 많은 역할을 한다.

#### MVC 패턴의 등장
서블릿은 비즈니스 로직을 수행하는데에 특화되게, JSP는 뷰 화면만 그리도록 변경해보자.

이때 등장한 것이 `MVC 패턴!!`

---
### MVC 패턴 - 개요
#### 너무 많은 역할
하나의 서블릿이나 JSP만으로 비즈니스 로직과 뷰까지 모두 처리하면 유지보수가 어려워진다!!

#### 변경의 라이프 사이클
UI수정과 비즈니스 로직 수정의 라이프 사이클은 다르다.

라이프 사이클이 다른 부분을 하나의 코드로 관리하는 것은 유지보수하기 좋지 않다.

#### 기능 특화
특히 JSP 같은 뷰 템플릿은 화면ㅇ르 렌더링 하는데 최적화 되어 있기 때문에 이 부분의 업무만 담당하는 것이 가장 효율적이다.

#### MVC(Model View Controller)
서블릿이나 JSP 하나에서만 하던 일을 `모델과 뷰` 두 가지로 나눈 것을 MVC 패턴이라고 한다.

**Controller** : HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행.
뷰에 전달할 데이터를 조회하여 모델에 담는다.
**Model** : 뷰에 출력할 데이터를 담고 있는 곳.
**View** : 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다.

    컨트롤러에 비즈니스 로직을 두면 컨트롤러에서 너무 많은 일을 처리하게 된다.
    그래서 일반적으로는 서비스 계층을 하나 더 두어 여기서 비즈니스 로직이 실행되게 하고, 컨트롤러는 앞단에서 파라미터 검증을 수행하고 서비스 계층을 호출한다.


---
### MVC 패턴 - 적용
- 서블릿 : 컨트롤러(Controller)
- JSP : 뷰(View)
- HttpServletRequest : 모델(Model)
    - request는 내부에 데이터 저장소를 갖고 있기 때문에 `request.setAttribute() / request.getAttribute()`를 통해 데이터를 보관/조회 할 수 있다.

`RequestDispatcher.forward()` : 다른 서블릿이나 JSP로 이동할 수 있는 기능. 서버 내부에서 다시 호출이 발생한다.

    WEB-INF : 이 경로 안에 JSP가 있으면 외부에서 직접 JSP를 호출할 수 없다.

<br>

    redirect VS forward
    - redirect : 실제 클라이언트에 응답이 나갔다가 클라이언트가 redirect 경로로 다시 요청
    - forward : 서버 내부에서 일어나는 호출이기 때문에 클라이언트가 전혀 인지하지 못한다.

#### form에서 절대경로, 상대경로

form에서 상대경로를 사용하면 폼 전송시 현재 URL이 속한 계층 경로 + save가 호출된다.

현재 계층 경로 : `/servlet-mvc/members/`

결과 : `/servlet-mvc/members/save`

---
### MVC 패턴 - 한계

