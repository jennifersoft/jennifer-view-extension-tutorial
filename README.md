# JENNIFER View Extension Tutorial

JENNIFER 뷰서버 확장 모듈 개발을 위한 올인원 튜토리얼 프로젝트입니다.  
Java와 Kotlin 두 가지 언어로 어댑터 구현 예제를 제공합니다.

## 요구 사항

- Java 1.8+
- Maven 3.x (또는 포함된 Maven Wrapper 사용)
- `com.aries.extension` 1.5.8

## 프로젝트 구조

```
src/
├── main/
│   ├── java/com/aries/tutorial/adapter/     # Java 어댑터 예제
│   │   ├── EventAdapter.java                # 이벤트 핸들러
│   │   ├── LoginAdapter.java                # 로그인 핸들러
│   │   ├── SSOLoginAdapter.java             # SSO 로그인 핸들러
│   │   ├── SystemEventAdapter.java          # 시스템 이벤트 핸들러
│   │   └── TransactionAdapter.java          # 트랜잭션 핸들러
│   └── kotlin/com/aries/tutorial2/adapter/  # Kotlin 어댑터 예제
│       ├── EventAdapter.kt
│       ├── LoginAdapter.kt
│       ├── SSOLoginAdapter.kt
│       ├── SystemEventAdapter.kt
│       └── TransactionAdapter.kt
dist/                                        # 빌드 결과물 (JAR)
```

## 빌드

```bash
# Maven Wrapper 사용
./mvnw clean package

# 또는 시스템 Maven 사용
mvn clean package
```

빌드 결과물은 `dist/extension-tutorial-1.0.1.jar`에 생성됩니다.

## 어댑터 인터페이스 가이드

### 1. EventHandler — 이벤트 어댑터

애플리케이션 이벤트(에러, 경고 등) 발생 시 호출됩니다.

```java
import com.aries.extension.handler.EventHandler;
import com.aries.extension.data.EventData;

public class EventAdapter implements EventHandler {
    @Override
    public void on(EventData[] events) {
        // events 배열에 이벤트 정보가 전달됨
        // EventData 주요 필드:
        //   domainId, instanceName, businessName,
        //   txid, serviceName, errorType, eventLevel
    }
}
```

### 2. TransactionHandler — 트랜잭션 어댑터

트랜잭션 완료 시 호출됩니다.

```java
import com.aries.extension.handler.TransactionHandler;
import com.aries.extension.data.TransactionData;

public class TransactionAdapter implements TransactionHandler {
    @Override
    public void on(TransactionData[] transactions) {
        // TransactionData 주요 필드:
        //   domainId, instanceName, txid,
        //   responseTime, applicationName
    }
}
```

### 3. LoginHandler — 로그인 어댑터

JENNIFER 뷰서버 로그인 시 인증을 커스터마이징합니다.

```java
import com.aries.extension.handler.LoginHandler;
import com.aries.extension.data.UserData;

public class LoginAdapter implements LoginHandler {
    @Override
    public UserData preHandle(String id, String password) {
        // 인증 성공 시: new UserData(id, password, role, name) 반환
        // 인증 실패 시: null 반환
        return null;
    }

    @Override
    public String redirect(String id, String password) {
        // 로그인 성공 후 리다이렉트 경로 반환
        return "/dashboard/realtimeAdmin";
    }
}
```

`UserData` 생성자 파라미터:

| 파라미터 | 설명 |
|---------|------|
| `id` | 사용자 ID |
| `password` | 비밀번호 |
| `role` | 권한 (예: `"admin"`) |
| `name` | 사용자 표시 이름 |

### 4. SSOLoginHandler — SSO 로그인 어댑터

SSO(Single Sign-On) 인증을 통한 로그인을 처리합니다.

```java
import com.aries.extension.handler.SSOLoginHandler;
import com.aries.extension.data.UserData;
import javax.servlet.http.HttpServletRequest;

public class SSOLoginAdapter implements SSOLoginHandler {
    @Override
    public UserData preHandle(HttpServletRequest request) {
        // HTTP 요청 헤더에서 SSO 인증 정보를 추출
        String ssoId = request.getHeader("SSO_ID");
        String ssoPassword = request.getHeader("SSO_PASSWORD");

        // 인증 성공 시: new UserData(id, password) 반환
        // 인증 실패 시: null 반환
        return new UserData(ssoId, ssoPassword);
    }
}
```

### 5. SystemEventHandler — 시스템 이벤트 어댑터

JENNIFER 시스템 이벤트(서버 상태 변경 등) 발생 시 호출됩니다.

```java
import com.aries.extension.handler.SystemEventHandler;
import com.aries.extension.data.SystemEventData;

public class SystemEventAdapter implements SystemEventHandler {
    @Override
    public void on(SystemEventData[] events) {
        // SystemEventData 주요 필드:
        //   subject, message, dataServer
    }
}
```

## 유틸리티 클래스

### PropertyUtil

JENNIFER 뷰서버에서 설정한 어댑터별 프로퍼티 값을 읽습니다.

```java
// PropertyUtil.getValue(어댑터ID, 키, 기본값)
String value = PropertyUtil.getValue("event_adapter", "subject", "Unknown subject");
```

### LogUtil

JENNIFER 뷰서버 로그 시스템에 로그를 기록합니다.

```java
LogUtil.info("정보 메시지");
LogUtil.error("에러 메시지");
```

## 배포

1. `./mvnw clean package`로 빌드
2. `dist/extension-tutorial-1.0.1.jar` 파일을 JENNIFER 뷰서버의 확장 모듈 디렉토리에 복사
3. JENNIFER 뷰서버 관리 화면에서 어댑터를 등록하고 활성화

## Kotlin 예제

모든 어댑터는 Kotlin으로도 동일하게 구현되어 있습니다. `com.aries.tutorial2.adapter` 패키지를 참고하세요.

```kotlin
class EventAdapter : EventHandler {
    override fun on(events: Array<EventData>) {
        for (data in events) {
            LogUtil.info("Domain ID : ${data.domainId}")
        }
    }
}
```

## 라이선스

Copyright (c) JenniferSoft Inc.
