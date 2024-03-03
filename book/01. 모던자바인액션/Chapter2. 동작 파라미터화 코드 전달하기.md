# Part 1. 기초
## Chapter2. 동작 파라미터화 코드 전달하기

**[핵심]**
- 변화하는 요구사항에 대응
- 동작 파라미터화
- 익명 클래스
- 람다 표현식 미리보기
- 실전 예제 : Comparator, Runnable, GUI

### 2.1. 변화하는 요구사항에 대응하기
시시각각 변하는 요구사항에 대응하기에 좋은 것이 <b>'동작 파라미터화(Behavior Parameterization)'</b>이다.

    동작파라미터화(Behavior Parameterization) : 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블록을 의미


### 2.1. 변화하는 요구사항에 대응하기
요구사항 : 기존 농장 재고목록 애플리케이션에 리스트에서 녹색 사과만 필터링하는 기능을 추가.

#### 2.1.1. 1트 : 녹색 사과 필터링
```java
enum Color { RED, GREEN }
    
// 녹색 사과를 필터링
public static List<Apple> filterGreenApples(List<Apple> inventory) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
        if (GREEN.equals(apple.getColor())) {
            result.add(apple);
        }
    }
    return result;
}
```
<span style="font-size:17px;">만약에</span> 농부가 다양한 색(옅은 녹색, 어두운 빨간색 등)으로 필터링하는 요구사항을 추가한다면, 이에 대해서 적절하게(=유연하게) 대응할 수 없다. 그럴 때는 아래와 같은 규칙을 기억하면 된다.

    거의 비슷한 코드가 반복 존재한다면 그 코드를 추상화한다.

#### 2.1.2. 2트 : 색을 파라미터화
비슷한 코드를 반복하지 않기 위해 
`filterGreenApples 메서드에 파라미터를 추가`하여
변화하는 요구사항에 유연하게 대응하는 코드를 만들 수 있다.

```java
public static List<Apple> filterApplesByColor(List<Apple> inventory, Color color) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
        if (apple.getColor().equals(color)) {
            result.add(apple);
        }
    }
    return result;
}

// 호출
List<Apple> greenApples = filterApplesByColor(inventory, GREEN);
List<Apple> redApples = filterApplesByColor(inventory, RED);
```
<br>
<span style="font-size:17px;">만약에 </span> 또 농부가 
'색 이외에도 무게로 사과를 구분할 수 있다면 정말 좋겠네요. 보통 무게가 150g 이상인 사과가 무거운 사과입니다.'
라고 요구한다면?

```java
public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
        if (apple.getWeight() > 150) {
            result.add(apple);
        }
    }
    return result;
}
```

하지만 목록을 검색하고, 각 요소에 필터링 조건을 적용하는 부분의 코드가 대부분 중복된다.

이는 SW공학의 `DRY(Don't Repeat Yourself)` 원칙을 어기는 것이다.

색깔 필터링 or 무게 필터링을 동시에 할 수 있는 filter 메서드로 합친다면, 색 또는 무게 중 어떤 것을 기준으로 필터링할지 가리키는 플래그를 추가할 수 있다.

<span style="color:red;font-weight:bold;font-size:20px">** 실전에서는 절대 사용하지 말 것. **</span>

#### 2.1.3. 3트 : 가능한 모든 속성으로 필터링

```java
public static List<Apple> filterApples(List<Apple> inventory, Color color, int weight, boolean flag) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
        if ((flag && apple.getColor().equals(color)
            || (!flag && apple.getWeight() > weight))) {
            result.add(apple);
        }
    }
    return result;
}

// 호출
List<Apple> greenApples = filterApples(inventory, GREEN, 0, true);
List<Apple> heavyApples = filterApples(inventory, null, 150, false);
```

    [위 코드의 문제점]
      1. 파라미터의 true/false는 무엇을 의미하는가?
      2. 색깔이나 무게가 아닌 사과의 크기, 모양, 출하지 등으로 사과를 필터링하고 싶다는 요구사항 변경이 일어난다면 대처하기 어려운 코드


### 2.2. 동작 파라미터화
사과의 어떤 속성에 기초해서 boolean값을 반환하는 방법이 있다.
`=> true/false를 반환하는 함수 = Predicate`

선택 조건을 결정하는 인터페이스를 먼저 생성하자.

```java
public interface ApplePredicate {
    boolean test(Apple apple);
}

// 다양한 선택 조건을 대표하는 여러 버전의 ApplePredicate를 정의
// 무거운 사과를 선택하는 predicate
public class AppleHeavyWeightPredicate implements ApplePredicate {
    public boolean test(Apple apple) {
        return apple.getWeight() > 150;
    }
}

// 녹색 사과를 선택하는 predicate
public class AppleGreenColorPredicate implements ApplePredicate {
    public boolean test(Apple apple) {
        return apple.getColor().equals(GREEN);
    }
}
```

이러한 디자인 패턴을 `전략 디자인 패턴(Strategy Design Pattern)` 이라고 부른다.

    전략 디자인 패턴(Strategy Design Pattern) : 각 알고리즘(=전략)을 캡슐화하는 알고리즘 패밀리를 정의해둔 다음에 런타임에 알고리즘(=전략)을 선택하는 기법이다.

그렇다면, filterApples 메서드에서 전략을 선택하도록 코드를 어떻게 바꿀 수 있을까?

-> filterApples 에서 ApplePredicate 객체를 받아 조건을 검사하도록 메서드를 수정해야 함. 이것이 `동작 파라미터화.`

이렇게 변경하면 `메서드 내부에서 Collection을 반복하는 로직과 Collection의 각 요소에 적용할 동작을 분리할 수 있다`는 점에서 소프트웨어 엔지니어링적으로 큰 이득을 얻는다.

#### 2.2.1. 4트 : 추상적 조건으로 필터링
다음은 ApplePredicate를 이용한 필터 메서드다.

```java
public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
        if (p.test(apple)) {
            result.add(apple);
        }
    }
    return result;
}
```

`코드/동작을 전달하고, 한 개의 파라미터로 다양한 동작을 할 수 있도록 메서드를 수정한 결과` 변경에 
**용이한(=유연한) API** 가 완성됐다.

    동작 파라미터화의 강점은 'Collection 탐색로직과 각 항목에 적용할 동작을 분리할 수 있다는 것'이다.


### 2.3. 복잡한 과정 간소화

 지금까지 작성한 코드는 `ApplePredicate`를 만들고 여러 구현 클래스를 정의한 뒤 인스턴스화 해야함.
=> 클래스의 선언 + 인스턴스화 = `익명클래스(Anonymous Class)` 탄생

#### 2.3.1. 익명 클래스

익명 클래스 ≒ 자바의 지역 클래스

#### 2.3.2. 5트 : 익명 클래스 사용

아래 예제는 익명 클래스를 이용해서 `ApplePredicate`를 구현하는 객체를 만드는 방법으로 필터링 예제를 다시 구현한 코드.

```java
List<Apple> redApples = filterApples(inventory, new ApplePredicate() {
    public boolean test(Apple apple) {
        return RED.equals(apple.getColor());
    }
});
```

#### 2.3.3. 6트 : 람다 표현식 사용

#### 2.3.4. 7트 : 리스트 형식으로 추상화
```java
public interface Predicate<T> {
    boolean test(T t);
}

public static <T> List<T> filter(List<T> list, Predicate<T> p) {
    List<T> result = new ArrayList<>();
    for (T e : list) {
        if (p.test(e)) {
            result.add(e);
        }
    }
    return result;
}
```
### 2.4. 실전 예제

#### 2.4.1. Comparator로 정렬하기

#### 2.4.2. Runnable로 코드 블록 실행하기

#### 2.4.3. Callable을 결과로 반환하기

#### 2.4.4. GUI 이벤트 처리하기