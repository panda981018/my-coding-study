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

MVC 패턴을 적용한 덕분에 컨트롤러의 역할과 뷰를 렌더링하는 역할을 명확하게 구분할 수 있다.

뷰는 화면을 그리는 역할에 충실한 덕분에, 코드가 깔끔&직관적.

그런데 컨트롤러는 중복이 많고, 필요하지 않는 코드를 많이 사용한다.

#### MVC 컨트롤러의 단점
**포워드 중복**
View로 이동하는 코드가 항상 중복 호출되어야 한다. 

**ViewPath 중복**
prefix : `/WEB-INF/views/`
suffix: `.jsp`

그리고 만약 jsp가 아닌 thymeleaf 같은 뷰로 변경한다면 전체 코드를 다 변경해야함.

**사용하지 않는 코드**
`HttpServletRequest request, HttpServletResponse reponse`에서 response는 사용하지 않을 때도 있음에도 항상 작성해야함.

그리고 `HttpServletRequest`, `HttpServletResponse`를 사용하면 테스트 케이스를 작성하기도 어려움.

**공통 처리가 어렵다**
기능이 복잡해질수록 컨트롤러에서 공통으로 처리해야 하는 부분이 점점 더 많이 증가할 것이다.

공통 기능으로 빼낸다고 해도, 매번 해당 메서드를 호출해야하기 때문에 이 또한 중복임.

**=> 정리하면 공통 처리가 어렵다는 문제가 있다.**

## Section 4. MVC 프레임워크 만들기
### 프론트 컨트롤러 패턴 소개

#### FrontController 패턴 특징
- 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
- 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출
- 입구를 하나로! (공통처리 가능)
- 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨

#### 스프링 웹 MVC의 프론트 컨트롤러
스프링 웹MVC의 핵심도 바로 **FrontController**
스프링 웹MVC의 **DispatcherServlet**이 FrontController 패턴으로 구현되어 있음.

---
### 유연한 컨트롤러 - v5

#### 어댑터 패턴(Adaptor Pattern)
어댑터를 이용하면 인터페이스 호환성 문제 때문에 같이 쓸 수 없는 클래스들을 연결해서 쓸 수 있다.

## Section 5. 스프링 MVC - 구조 이해
### 스프링 MVC 전체 구조

**직접 만든 프레임워크 -> 스프링 MVC 비교**
- FrontController -> DispatcherServlet
- handlerMappingMap -> HandlerMapping
- MyHandlerAdapter -> HandlerAdapter
- ModelView -> ModelAndView
- viewResolver -> ViewResolver
- MyView -> View

#### DispatcherServlet 구조 살펴보기
스프링 MVC도 프론트 컨트롤러 패턴으로 구현되어 있음.

스프링 MVC의 프론트 컨트롤러 = **DispatcherServlet**

**DispatcherServlet 서블릿 등록**
스프링 부트는 DispatcherServlet을 서블릿으로 자동으로 등록하면서 *모든 경로* 에 대해서 매핑한다.

#### 요청 흐름
- 서블릿이 호출되면 `HttpServlet`이 제공하는 `service()`가 호출된다.
- DispatcherServlet의 부모인 `FrameworkServlet.service()`를 시작으로 여러 메서드가 호출되면서 `DispatcherServlet.doDispatch()`가 호출된다.

**doDispatch 분석**

```
1. 핸들러 조회
2. 핸들러 어댑터 조회 - 핸들러를 처리할 수 있는 어댑터 조회
3. 핸들러 어댑터 실행
4. 핸들러 어댑터를 통해 핸들러 실행
5. ModelAndView 반환
6. viewResolver를 통해서 뷰 찾기
7. View 반환
8. View 렌더링
```

**MVC 구조**

```
1. 핸들러 조회 : 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.
2. 핸들러 어댑터 조회 : 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다.
3. 핸들러 어댑터 실행 : 핸들러 어댑터를 실행한다.
4. 핸들러 실행 : 핸들러 어댑터가 실제 핸들러를 실행한다.
5. ModelAndView 반환 : 핸들러 어댑터는 핸들러가 반환하는 정보를 ModelAndView로 변환해서 반환한다.
6. ViewResolver 호출 : 뷰 리졸버를 찾고 실행한다.
7. View 반환 : 뷰 리졸버는 뷰의 논리 이름을 물리 이름으로 바꾸고, 렌더링 역할을 담당하는 뷰 객체를 반환한다.
8. 뷰 렌더링 : 뷰를 통해서 뷰를 렌더링 한다.
```

**인터페이스 살펴보기**
- 스프링MVC의 강점은 `DispatcherServlet` 코드의 변경 없이, 원하는 기능을 변경하거나 확장할 수 있다는 점이다. (대부분 확장 가능할 수 있게 인터페이스로 제공한다.)

**주요 인터페이스 목록**
- 핸들러 매핑 : `org.springframework.web.servlet.HandlerMapping`
- 핸들러 어댑터 : `org.springframework.web.servlet.HandlerAdapter`
- 뷰 리졸버 : `org.springframework.web.servlet.ViewResolver`
- 뷰 : `org.springframework.web.servlet.View`

---

### 핸들러 매핑과 핸들러 어댑터
지금은 전혀 사용하지 않지만, 과거에 주로 사용했던 스프링이 제공하는 간단한 컨트롤러로 핸들러 매핑과 어댑터를 이해해보자.

#### Controller 인터페이스
`org.springframework.web.servlet.mvc.Controller`

**Controller 인터페이스는 `@Controller`랑은 다르다.**

이 Controller 인터페이스를 상속받은 컨트롤러는 어떻게 호출이 될까?

- HandlerMapping(핸들러 매핑)
    - 핸들러 매핑에서 이 컨트롤러를 찾을 수 있어야 한다.
- HandlerAdapter(핸들러 어댑터)
    - 핸들러 매핑을 통해서 찾은 핸들러를 실행할 수 있는 핸들러 어댑터가 필요하다.

**스프링 부트가 자동 등록하는 핸들러 매핑과 핸들러 어댑터**

**HandlerMapping**

    0 = RequestMappingHandlerMapping // 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
    1 = BeanNameUrlHandlerMapping // 스프링 빈의 이름으로 핸들러를 찾는다.

**HandlerAdapter**

    0 = RequestMappingHandlerAdapter // 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
    1 = HttpRequestHandlerAdapter // HttpRequestHandler 처리
    2 = SimpleControllerHandlerAdapter // Controller 인터페이스(애노테이션X, 과거에 사용) 처리

핸들러 매핑도, 핸들러 어댑터도 모두 순서대로 조회하고 없으면 다음 순서로 넘어간다.

1. 핸들러 매핑으로 핸들러 조회
    - HandlerMapping을 순서대로 실행해서, 핸들러를 찾는다.
    - 이때 빈 이름으로 핸들러를 찾아야 하기 때문에 이름 그대로 빈 이름으로 핸들러를 찾아주는 `BeanNameUrlHandlerMapping`이 실행에 성공하고 핸들러인 `OldController`가 반환된다.
2. 핸들러 어댑터 조회
    - `HandlerAdapter`의 `supports()`를 순서대로 호출
    - `SimpleControllerHandlerAdapter`가 `Controller` 인터페이스를 지원하므로 대상이 된다.
3. 핸들러 어댑터 실행
    - 디스패처 서블릿이 조회한 `SimpleControllerHandlerAdapter`를 실행하면서 핸들러 정보도 함께 넘겨준다.
    - `SimpleControllerHandlerAdapter`는 핸들러인 `OldController`를 내부에서 실행하고, 그 결과를 반환한다.

**정리 - OldController 핸들러매핑, 어댑터**
`HandlerMapping = BeanNameUrlHandlerMapping`
`HandlerAdapter = SimpleControllerHandlerAdapter`

**HttpRequestHandler**

Controller가 아닌 다른 핸들러를 알아보자.

`HttpRequestHandler` 핸들러(컨트롤러)는 **서블릿과 가장 유사한 형태의 핸들러이다.**

**실행순서**
1. 핸들러 매핑으로 핸들러 조회
    - `HandlerMapping`을 순서대로 실행해서, 핸들러를 찾는다.
    - 이 경우 빈 이름으로 핸들러를 찾아야 하기 때문에 이름 그대로 빈 이름으로 핸들러를 찾아주는 `BeanNameUrlHandlerMapping`가 실행에 성공하고 핸들러인 `MyHttpRequestHandler`를 반환한다.
2. 핸들러 어댑터 조회
    - `HandlerAdapter`의 `supports()`를 순서대로 호출한다.
    - `HttpRequestHandlerAdapter`가 `HttpRequestHandler` 인터페이스를 지원하므로 대상이 된다.
3. 핸들러 어댑터 실행
    - DispatcherServlet이 조회한 `HttpRequestHandlerAdapter`를 실행하면서 핸들러 정보도 함께 넘겨준다.
    - `HttpRequestHandlerAdapter`는 핸들러인 `MyHttpRequestHandler`를 내부에서 실행하고, 그 결과를 반환한다.

**정리 - MyHttpRequestHandler 핸들러 매핑, 어댑터**
`HandlerMapping = BeanNameUrlHandlerMapping`
`HandlerAdapter = HttpRequestHandlerAdapter`

#### @RequestMapping
가장 우선순위가 높은 핸들러매핑과 어댑터는 `RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter`이다.

현재 스프링에서 주로 사용하는 애노테이션 기반의 컨트롤러를 지원하는 매핑과 어댑터이다. 실무에서는 99.9%가 이 방식의 컨트롤러를 사용한다.

---
### 뷰 리졸버

View가 나올 수 있도록 다음 코드를 추가한다.
`return new ModelAndView("new-form");`

`application.properties`에 다음 코드를 추가한다.
    spring.mvc.view.prefix=/WEB-INF/views
    spring.mvc.view.suffix=.jsp

#### 뷰 리졸버 - InternalResourceViewResolver

스프링부트는 `InternalResourceViewResolver`라는 뷰 리졸버를 자동으로 등록하는데, 이때 `application.properties`에 등록한 prefix, suffix 설정 정보를 사용해서 등록한다.

#### 뷰 리졸버 동작 방식

**스프링부트가 자동으로 등록하는 뷰 리졸버**

    1 = BeanNameViewResolver // 빈 이름으로 뷰를 찾아서 반환한다.(예 : 엑셀 파일 생성 기능에 사용)
    2 = InternalResourceViewResolver // JSP를 처리할 수 있는 뷰를 반환한다.

1. 핸들러 어댑터 호출
핸들러 어댑터를 통해서 `new-form`이라는 논리 뷰 이름을 획득한다.
2. ViewResolver 호출
- `new-form`이라는 뷰 이름으로 viewResolver를 순서대로 호출한다.
- `BeanNameViewResolver`는 `new-form`이라는 이름의 스프링 빈으로 등록된 뷰를 찾아야 하는데 없음.
- `InternalResourceViewResolver`가 호출됨.
3. InternalResourceViewResolver
이 뷰 리졸버는 `InternalResourceView`를 반환한다.
4. 뷰 - InternalResourceView
`InternalViewResolver`는 JSP처럼 포원드 `forward()`를 호출해서 처리할 수 있는 경우에 사용한다.
5. view.render()
`view.render()`가 호출되고 `InternalResourceView`는 `forward()`를 사용해서 JSP를 실행한다.

**참고**
```
InternalResourceViewResolver는 만약 JSTL 라이브러리가 있으면 InternalResourceView를 상속받은 JstlView를 반환한다.

다른 뷰는 실제 뷰를 랜더링하지만, JSP의 경우 forward()를 통해서 JSP로 이동(실행)해야 렌더링이 된다.
JSP를 제외한 나머지 뷰 템플릿들은 forward() 과정 없이 바로 렌더링 된다.

Thymeleaf 뷰 템플릿을 사용하면 ThymeleafViewResolver를 틍록해야 한다. 최근에는 라이브러리만 추가하면 스프링 부트가 이런 작업도 모두 자동화해준다.
```

---
### 스프링 MVC - 시작하기

**@RequestMapping**
- `RequestMappingHandlerMapping`
- `RequestMappingHandlerAdapter`

**@Controller**
- 내부에 `@Component`가 있어서 스프링이 자동으로 스프링 빈으로 등록한다.
- 스프링 MVC에서 애노테이션 기반 컨트롤러로 인식한다.

**@RequestMapping**
- 요청 정보를 매핑한다. 해당 URL이 호출되면 이 메서드가 호출된다. 애노테이션을 기반으로 동작하기 때문에, 메서드 이름은 임의로 지으면 된다.

**ModelAndView**
- 모델과 뷰 정보를 담아서 반환하면 된다.


`RequestMappingHandlerMapping`은 스프링 빈 중에서 `@RequestMapping` 또는 `@Controller`가 클래스 레벨에 붙어 있는 경우에 매핑 정보로 인식한다.
따라서 `@Component`가 클래스 레벨에 붙어 있지 않아도 스프링 빈으로 직접 등록해도 동작한다.

```java
// 변경 전
@Component // 컴포넌트 스캔을 통해서 스프링 빈으로 등록
@RequestMapping
public class A {

    @RequestMapping("/spingmvc/v1/members")
    public ModelAndView process() {
        ...
    }
}

// 변경 후
@RequestMapping
public class A {

    @RequestMapping("/spingmvc/v1/members")
    public ModelAndView process() {
        ...
    }
}
```

**주의! 스프링 3.0 이상**
스프링부트 3.0(스프링프레임워크 6.0) 이상부터는 클래스 레벨에 `@RequestMapping`이 있어도 컨트롤러로 인식하지 않는다. 오직 `@Controller`가 있어야만 스프링 컨트롤러로 인식한다. (`@RestController`는 내부에 `@Controller`가 있으니 당연히 인식됨.)

---
### 스프링MVC - 컨트롤러 통합

컨트롤러 각 메서드 별로 중복됐던 `/springmvc/v2/members` 부분을 클래스 단위로 통합할 수 있다.

```java
@Controller
@RequestMapping("/springmvc/v2/members")
public class A {

    @RequestMapping("/new-form")
    public ModelAndView newForm() {
        return ...;
    }

    @RequestMapping("/save")
    public ModelAndView save() {
        return ...;
    }

    @RequestMapping
    public ModelAndView members() {
        return ...;
    }
}
```
`@RequestMapping`은 결국 `클레스 레벨에 붙은 path + 메서드 레벨에 붙은 path` 이런 형태로 view를 찾아간다.

---
### 스프링MVC - 실용적인 방식
- Model을 메서드 파라미터로 받음.
- Controller 내부 메서드에서 View Name을 직접 반환
- @RequestMapping 대신 `@GetMapping, @PostMapping` 사용

```java
@Controller
@RequestMapping("/springmvc/v3/members")
public class A {
    
    @GetMapping("/new-form")
    public String newForm() {
        return "new-form";
    }

    @PostMapping("/save")
    public String save(@RequestParam("username") String username, @RequestParam("age") int age, Model model) {
        ...
        return "save-result";
    }

    @GetMapping
    public String members(Model model) {
        model.addAttribute("members", ...);

        return "members";
    }
}
```
**Model 파라미터**

`save(), members()`에서 파라미터로 Model을 밭았다. 스프링 MVC도 이런 편의 기능을 제공한다.

<br/>

**ViewName 반환**

뷰의 논리 이름을 반환하였다.

<br/>

**@RequestParam**

스프링의 HTTP 요청 파라미터를 `@RequestParam`으로 받을 수 있다.
GET 쿼리 파라미터, POST Form 방식을 모두 지원함.

**@RequestMapping -> @GetMapping, @PostMapping**

`@RequestMapping`은 URL만 매칭하는 것이 아니라 HTTP Method도 구분할 수 있다.

    1단계 : @RequestMapping("/new-form")
    2단계 : @RequestMapping(value = "/new-form", method = HttpMethod.GET)
    3단계 : @GetMapping("/new-form")

Get, Post 뿐만 아니라 Delete, Put, Patch 모두 애노테이션이 있다.

## Section 6. 스프링 MVC - 기본 기능

### 로깅 간단히 알아보기

운영에서는 `System.out.println()` 같은 시스템 콘솔은 사용하지 않는다.

별도의 로깅 라이브러리를 추가해서 로그를 찍는다.

#### 로깅 라이브러리

스프링 부트 라이브러리를 사용하면 스프링 부트 로깅 라이브러리 `spring-boot-starter-loggin`가 함께 포함된다.

- SLF4J(인터페이스) : http://www.slf4j.org
- Logback(구현체) : http://logback.qos.ch

#### 로그 선언
`private Logger log = LoggerFactory.getLogger(A.class);`
`private static final Logger log = LoggerFactory.getLogger(A.class)`
`@Slf4j` 롬복으로 선언하여 사용 가능

#### 로그 호출
`log.info("hello")`
`System.out.println("hello")`

#### 매핑 정보
`@RestController`
- `@Controller`는 반환 값이 String이면 뷰 이름으로 인식.
- `@RestController`는 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력. (@RestController = @ResponseBody + @Controller)

#### 테스트
- 로그가 출력되는 포맷 확인
    - 시간, 로그 레벨, 프로세스ID(PID), 스레드 명, 클래스 명, 로그 메시지
- 로그 레벨 설정을 변경해서 출력 결과를 보자.
    - LEVEL : `TRACE < DEBUG < INFO < WARN < ERROR`
    - 개발 서버는 `DEBUG`부터 출력
    - 운영 서버는 `INFO`부터 출력
- `@Slf4j`로 변경

#### 로그 레벨 설정
`application.properties`

```properties
# 전체 로그 레벨 설정 (기본 info)
logging.level.root=info

# hello.springmvc 패키지와 그 하위 로그 레벨 설정
logging.level.hello.springmvc=debug
```

#### 올바른 로그 사용법
`log.debug("data = {}", data)`
- 로그 레벨 출력을 info로 설정하면 별다른 연산 작업이 발생하지 않는다. 따라서, 더하기 연산이 아니라 중괄호({ })를 사용하여 로그를 출력하자.

#### 로그 사용시 장점
- 스레드 정보, 클래스 이름 같은 부가 정보를 함께 볼 수 있고, 출력 모양을 조정할 수 있다.
- 개발 서버에서는 `DEBUG`, 운영 서버에서는 `INFO` 이런 식으로 실행 환경에 따라서 로그 레벨을 조절할 수 있다.
- 시스템 콘솔에만 출력하는 것이 아니라, **파일이나 네트워크 등 로그를 별도의 위치에 남길 수 있다.** 특히 파일로 남길 때는 일별, 특정 용량에 따라 로그를 분할할 수도 있다.
- 성능도 일반 System.out보다 좋다.

---
### 요청 매핑
- `@RestController`
    - `@Controller`는 반환값이 String이면 뷰 이름으로 인식. **따라서 뷰를 찾고 렌더링 된다.**
    - `@RestController`는 반환값을 HTTP Body에 바로 입력. (`@RestController = @ResponseBody + @Controller`)
- `@RequestMapping("/hello-basic)`
    - `/hello-basic` URL 호출이 오면 이 메소드가 실행되도록 매핑됨.
    - 대부분의 속성은 배열([])로 제공되므로 다중 설정이 가능하다. (`{"/hello-basic", "/hello-go"}`)

#### 스프링 3.0 이후 변화
```text
URL 예시 : /hello-basic, /hello-basic/

스프링 3.0 이전 : 마지막 슬래시(/)를 제거했음.
스프링 3.0 이후 : 마지막의 슬래시(/)를 유지한다.
따라서 다음과 같이 매핑됨.
매핑 : /hello-basic -> URL 요청 : /hello-basic
매핑 : /hello-basic/ -> URL 요청 : /hello-basic/
```

#### HTTP 메서드

`@RequestMapping`에 method 속성을 주지 않으면 모든 HTTP Method 요청에 대해서 열려있는 것이다.
GET, HEAD, POST, PUT, PATCH, DELETE

#### 특정 파라미터 조건 매핑
```java
/**
 * 파라미터로 추가 매핑
 * params="mode"
 * params="!mode"
 * params="mode=debug"
 * params="mode!=debug"
 * params={"mode=debug", "data=good"}
 */
 @GetMapping(value = "/mapping-param", params="mode=debug") 
 public String mappingParam() {
    log.info("mappingParam");
    return "ok";
 }
```
특정 파라미터가 있거나 없는 조건을 추가할 수 있다. (하지만 잘 사용하지는 않음.)

#### 특정 헤더 조건 매핑
```java
/**
 * 특정 헤더로 추가 매핑
 * headers="mode"
 * headers="!mode"
 * headers="mode=debug"
 * headers="mode!=debug"
 */
@GetMapping(value = "/mapping-header", headers = "mode=debug")
public String mappingHeader() {
    log.info("mappingHeader");
    return "ok";
}
```

특정 파라미터 조건 매핑과 비슷하지만 HTTP 헤더를 사용한다.

#### 미디어타입 조건 매핑 - HTTP 요청 Content-Type, consume
```java
/**
 * Content-Type 헤더 기반 매핑 Media Type
 * consumes="application/json"
 * consumes="!application/json"
 * consumes="application/*"
 * consumes="*\/*"
 * MediaType.APPLICATION_JSON_VALUE
 */
@PostMapping(value = "/mapping-consume", consumes = "application/json")
public String mappingConsumes() {
    log.info("mappingConsumes");
    return "ok";
}
```

HTTP 요청의 헤더값에 Content-Type 헤더를 기반으로 미디어 타입으로 매핑한다.
만약 헤더에 Content-Type을 세팅하지 않고 요청하면 `HTTP 415(Unsupported Media Type) 상태코드를 반환.`

#### 미디어타입 조건 매핑 - HTTP 요청 Accept, produce
```java
/**
 * Accept 헤더 기반 Media Type
 * produces="text/html"
 * produces="!text/html"
 * produces="text/*"
 * produces="*\/*"
 */
@PostMapping(value = "/mapping-produces", produces = "text/html")
public String mappingProduces() {
    log.info("mappingProduces");
    return "ok";
}
```

HTTP 요청의 Accept 헤더를 기반으로 미디어 타입을 매핑한다.
만약 맞지 않으면 `HTTP 406(Not Acceptable) 상태코드를 반환한다.`

---
### HTTP 요청 파라미터 - @RequestParam

    참고

    애노테이션을 너무 생략하는 것도 과하다.
    @RequestParam이 있으면 명확하게 요청 파라미터에서 데이터를 읽는다는 것을 알 수 있다.

**주의! 스프링 부트 3.2 파라미터 이름 인식 문제**
#### 발생 예외
`java.lang.IllegalArgumentException: Name for argument of type [java.lang.String]
not specified, and parameter name information not found in class file either.`

스프링 부트 3.2에서
- `@RequestParam("username")`에서 `("username")`을 생략하고 싶다
- `@RequestParam`을 생략하고 싶다

자바 컴파일러에 `-parameters 욥션`을 넣어줘야 애노테이션에 적는 이름을 생략할 수 있다.

#### 파라미터 필수 여부 - requestParamRequired
```java
@ResponseBody
@RequestMapping("/request-param-required")
public String requestParamRequired(
    @RequestParam(required = true) String username,
    @RequestParam(required = false) int age) {
    log.info("username={}, age={}", username, age);
    return "ok";
}
```

**주의! 파라미터 이름만 사용**

`/request-param-required?username=`

파라미터 이름만 있고 값이 없는 경우 -> **빈 문자로 통과됨**

<br/>

**주의! 기본형(primitive)에 null 입력**

- `/request-param` 요청
- `@RequestParam(required = false) int age`
int 같은 기본형에 null은 입력할 수 없다.

따라서 null을 입력받을 수 있게 하려면 `Integer`로 선언하거나 `defaultValue`를 사용하자.

#### 기본 값 적용 - requestParamDefault
```java
@ResponseBody
@RequestMapping("/request-param-default")
public String requestParamDefault(
    @RequestParam(required = true, defaultValue = "guest") String username,
    @RequestParam(required = false, defaultValue = "-1") int age) {
    log.info("username={}, age={}", username, age);
    return "ok";
}
```

`defaultValue`는 빈문자의 경우에도 기본값으로 설정되도록 만들어짐.
따라서 `/request-param-default?username=` 통과

#### 파라미터를 Map으로 조회하기 - requestParamMap
```java
@ResponseBody
@RequestMapping("/request-param-map")
public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
    log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
    return "ok";
}
```

파라미터를 Map, MultiValueMap으로 조회할 수 있다.
- `@RequestParam Map`
    - `Map(key=value)`
- `@RequestParam MultiValueMap`
    - `MultiValueMap(key=[value1, value2, ...])`

---
### HTTP 요청 파라미터 - @ModelAttribute

Lombok `@Data`
- `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode`, `@RequiredArgsConstructor`를 자동으로 적용해준다.

```java
@ResponseBody
@RequestMapping("/mode-attribute-v1")
public String modelAttributeV1(@ModelAttribute HelloData helloData) {
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
    return "ok";
}
```

스프링MVC는 `@ModelAttribute`가 있으면 다음을 실행한다.
- `HelloData` 객체를 생성
- 요청 파라미터의 이름으로 `HelloData` 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력한다.

#### 프로퍼티

객체에 `getUsername()`, `setUsername()` 메소드가 있으면 이 객체는 `username`이라는 프로퍼티를 가지고 있다.

#### 바인딩 오류
`age=abc` 처럼 숫자가 들어가야 할 곳에 문자를 넣으면 `BindException`이 발생한다. 이런 바인딩 오류 처리는 검증 부분에서 다룬다.

#### `@ModelAttribute`는 생략 가능!

그런데 `@RequestParam`도 생략할 수 있으니 혼란이 발생할 수 있다.


