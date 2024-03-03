# Part 1. 기초
## Chapter 1. 자바 8, 9, 10, 11 : 무슨 일이 일어나고 있는가?

**[핵심]**
- 자바가 거듭 변화하는 이유
- 컴퓨팅 환경의 변화
- 자바에 부여되는 시대적 변화 요구
- 자바8과 9의 새로운 핵심 기능 소개
### 1.1. 역사의 흐름은 무엇인가.
    <자바 8에서 제공하는 것>
    - Stream API
    - method에 코드를 전달하는 기법
    - interface와 default method

메서드에 코드를 전달하는 기법을 이용하면 `새롭고 간결한 방식으로 동작 파라미터화` 구현 가능.

### 1.2. 왜 아직도 자바는 변화하는가?
#### 1.2.1. 프로그래밍 언어 생태계에서 자바의 위치
    <자바가 대중성을 갖개 된 이유>
    - JVM(Java Virtual Machine)이 설치된 어디에서든 실행 가능. (Write Once Run Anywhere)
    - 캡슐화
    - 객체지향이라는 모델에 기반한 언어이기 때문.

#### 1.2.2. 스트림 처리
스트림(Stream) : 한 번에 하나씩 만들어지는 연속적인 데이터 항목들의 모임.
#### 1.2.3. 동작 파라미터화로 메서드에 코드 전달하기
#### 1.2.4. 병렬성과 공유 가변 데이터
=> 자바 8 설계의 밑바탕을 이루는 3가지 개념

### 1.3. 자바 함수
in Programing) `function = method = static method`

in Java ) `function = method + 부작용을 일으키지 않는 함수`

    [1급시민, 2급시민]
    - 1급시민 : 값을 자유자재로 바꿀 수 있는 것.
    - 2급시민 : 'method, class' 같은 값을 자유자재로 바꾸기 어려운 것.

#### 1.3.1. 메서드, 람다를 2급시민에서 1급시민으로
```java
// 기존 코드
File[] hiddenFiles = new File(".").listFiles(new FileFilter() {
    public boolean accept(File file) {
        return file.isHidden(); // 숨겨진 파일 필터링 코드.
    }
});

// 메서드 참조 형식으로 변경한 코드
File[] hiddenFiles = new File(".").listFiles(File::isHidden);
```
메서드 참조(method reference) :: = '이 메서드를 값으로 사용하라'는 의미.
람다 : 익명함수 -> 람다를 사용하면 간결하게 코드를 구현할 수 있다.

#### 1.3.2. 코드 넘겨주기(예제)
**변경 전**
```java
// 문제정의1 : 모든 녹색 사과를 선택해서 리스트를 반환하는 프로그램.
public static List<Apple> filterGreenApples(List<Apple> inventory) {
    List<Apple> result = new ArrayList<>();
    for (Apple aaple : inventory) {
        if (apple.getColor().equals(GREEN)) {
            result.add(apple);
        }
    }
    return result;
}

// 문제정의2 : 사과의 무게가 150g 이상인 사과의 리스트를 반환하는 프로그램.
public static List<Apple> filterHeavyApples(List<Apple> inventory) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
        if (apple.getWeight() > 150) {
            result.add(apple);
        }
    }
    return result;
}
```

**변경 후**
```java
public static boolean isGreenApple(Apple apple) {
    return apple.getColor().equals(GREEN);
}

public static boolean isHeavyApple(Apple apple) {
    return apple.getWeight() > 150;
}

public interface Predicate<T> {
    boolean test(T t);
}

static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
        if (p.test(apple)) {
            result.add(apple);
        }
    }
    return result;
}

// 호출하는 부분
filterApples(inventory, Apple::isGreenApple);
filterApples(inventory, Apple::isHeavyApple);
```
* Predicate : 수학에서는 인수로 값을 받아 true or false를 반환하는 함수를 프레디케이트라고 함.

#### 1.3.3. 메서드 전달에서 람다로
한 번 사용할 메서드를 별도로 구현 => 비효율적.
따라서, 람다(익명함수)로 구현하면 조금 더 효율적.

`BUT` 람다로 작성할 함수 로직이 복잡하다면 `코드의 명확성을 위해` 별도의 named method로 구현하는 것이 더 나음.

### 스트림(Stream)

- 외부반복 : for-each를 사용하여 각 요소 반복 작업 수행.
- 내부반복 : (스트림 API에서는) 라이브러리 내부에서 모든 데이터가 처리됨.

이전 자바에서 제공하는 Thread API로 멀티스레딩 코드를 구현해서 병렬성을 이용하는 것은 어려움.

`BUT` 자바 8 Stream API는 아래 두 가지를 해결했음!
- Collection을 처리하면서 발생하는 모호함, 반복적인 코드문제
- 멀티코어 활용 어려움.

Stream API와 기존 Collection API 둘다 비슷한 방식으로 동작함. (둘다 '순차적인 데이터 항목 접근 방법' 제공.)

Collection은 `어떻게 데이터를 저장하고 접근할지에 중점을 둠.`

Stream은 `데이터에 어떤 계산을 할 것인지 묘사하는 것에 중점을 둠.`

    [자바의 병렬성과 공유되지 않은 가변상태]
    1) 큰 스트림은 작은 스트림으로 분열한다.
    2) 라이브러리 메서드로 전달된 메서드가 상호작용을 하지 않는다면 가변공유객체를 통해 공짜로 병렬성을 누릴 수 있다.

### 1.5. 디폴트 메서드와 자바 모듈
- 자바 9에서는 모듈을 정의하는 문법을 제공하므로 이를 이용해 패키지 모음을 포함하는 모듈 정의 가능.

- 자바 8에서는 인터페이스(Interface)를 쉽게 바꿀 수 있는 `디폴트 메서드(Default Method)`를 지원.
=> 구현 클래스에서 구현하지 않아도 되는 메서드를 인터페이스에 추가할 수 있는 기능 탑재.<br/>'default' 키워드를 맨 앞에 써주면 됨.