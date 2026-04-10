# JENNIFER 뷰 확장 튜토리얼 (JENNIFER View Extension Tutorial)

JENNIFER 뷰 서버 확장 모듈 개발을 위한 올인원 튜토리얼 프로젝트입니다.  
Java와 Kotlin 두 가지 언어로 어댑터 구현 예시를 제공합니다.

## 요구 사항

- Java 17+
- Maven 3.x (또는 포함된 Maven Wrapper 사용)
- `com.aries.extension` 1.5.8

## 프로젝트 구조

```
src/
├── main/
│   ├── java/com/aries/tutorial/adapter/     # Java 어댑터 예시
│   │   ├── EventAdapter.java                # 이벤트 핸들러
│   │   ├── LoginAdapter.java                # 로그인 핸들러
│   │   ├── SSOLoginAdapter.java             # SSO 로그인 핸들러
│   │   ├── SystemEventAdapter.java          # 시스템 이벤트 핸들러
│   │   └── TransactionAdapter.java          # 트랜잭션 핸들러
│   └── kotlin/com/aries/tutorial2/adapter/  # Kotlin 어댑터 예시
│       ├── EventAdapter.kt
│       ├── LoginAdapter.kt
│       ├── SSOLoginAdapter.kt
│       ├── SystemEventAdapter.kt
│       └── TransactionAdapter.kt
dist/                                        # 빌드 출력물 (JAR)
```

## 빌드 방법

```bash
# Maven Wrapper 사용 시
./mvnw clean package

# 시스템 Maven 사용 시
mvn clean package
```

빌드 결과물은 `dist/extension-tutorial-1.0.1.jar` 경로에 생성됩니다.

## 뷰 서버 제공 라이브러리 (패키징 제외 대상)

제니퍼 뷰 서버에는 아래 라이브러리들이 이미 포함되어 있습니다. 확장 모듈 개발 시 해당 라이브러리를 사용할 경우, `pom.xml`에서 의존성 범위(scope)를 `provided`로 설정하여 빌드 결과물(JAR)에 포함되지 않도록 해야 합니다.

**⚠️ 주의: 아래 목록에 없는 외부 라이브러리를 추가로 사용하는 경우에는 의존성 범위를 기본값(`compile`)으로 설정하여 빌드 시 JAR 파일에 함께 패키징되도록 해야 합니다.**

| 분류 | 라이브러리 (Artifact ID) | 버전 |
|-----------|------------------------------------|-------------|
| **Extension API** | `extension` | 1.5.8 |
| **Servlet API** | `javax.servlet-api` | 3.1.0 |
| **Spring Framework** | `spring-web`, `spring-webmvc` | 5.3.39 |
| **JSON / Serialization** | `jackson-databind`, `gson` | 2.15.0 / 2.10.1 |
| **Utilities** | `guava` | 33.5.0-jre |
| **Apache Commons** | `commons-io`, `commons-codec`, `commons-collections4`, `commons-csv` | 2.14.0 / 1.13 / 4.1 / 1.8 |
| **Template Engine** | `velocity-engine-core` | 2.3 |
| **Excel Library** | `poi`, `poi-ooxml` | 5.4.0 |
| **Security / Auth** | `jjwt` | 0.9.1 |
| **Logging** | `logback-classic`, `jcl-over-slf4j` | (내장) |
| **Kotlin** | `kotlin-stdlib-jdk8` | (내장) |

## 어댑터 인터페이스 가이드

### 1. EventHandler — 이벤트 어댑터

애플리케이션 이벤트(에러, 경고 등)가 발생했을 때 호출됩니다.

```java
import com.aries.extension.handler.EventHandler;
import com.aries.extension.data.EventData;

public class EventAdapter implements EventHandler {
    @Override
    public void on(EventData[] events) {
        // 이벤트 정보가 events 배열로 전달됩니다.
        // EventData 주요 필드:
        //   domainId, instanceName, businessName,
        //   txid, serviceName, errorType, eventLevel,
        //   instanceData (InstanceData & K8s 섹션 참조)
    }
}
```

### 2. TransactionHandler — 트랜잭션 어댑터

트랜잭션이 완료되었을 때 호출됩니다.

```java
import com.aries.extension.handler.TransactionHandler;
import com.aries.extension.data.TransactionData;

public class TransactionAdapter implements TransactionHandler {
    @Override
    public void on(TransactionData[] transactions) {
        // TransactionData 주요 필드:
        //   domainId, instanceName, txid,
        //   responseTime, applicationName,
        //   instanceData (InstanceData & K8s 섹션 참조)
    }
}
```

### 3. LoginHandler — 로그인 어댑터

제니퍼 뷰 서버 로그인의 인증 방식을 커스터마이징합니다.

```java
import com.aries.extension.handler.LoginHandler;
import com.aries.extension.data.UserData;

public class LoginAdapter implements LoginHandler {
    @Override
    public UserData preHandle(String id, String password) {
        // 성공 시: new UserData(id, password, role, name) 반환
        // 실패 시: null 반환
        return null;
    }

    @Override
    public String redirect(String id, String password) {
        // 로그인 성공 후 리다이렉트할 경로 반환
        return "/dashboard/realtimeAdmin";
    }
}
```

`UserData` 생성자 파라미터:

| 파라미터 | 설명 |
|-----------|-------------|
| `id` | 사용자 ID |
| `password` | 비밀번호 |
| `role` | 권한 (예: `"admin"`) |
| `name` | 표시 이름 |

### 4. SSOLoginHandler — SSO 로그인 어댑터

SSO(Single Sign-On) 인증을 통한 로그인을 처리합니다.

```java
import com.aries.extension.handler.SSOLoginHandler;
import com.aries.extension.data.UserData;
import javax.servlet.http.HttpServletRequest;

public class SSOLoginAdapter implements SSOLoginHandler {
    @Override
    public UserData preHandle(HttpServletRequest request) {
        // HTTP 요청 헤더에서 SSO 인증 정보 추출
        String ssoId = request.getHeader("SSO_ID");
        String ssoPassword = request.getHeader("SSO_PASSWORD");

        // 성공 시: new UserData(id, password) 반환
        // 실패 시: null 반환
        return new UserData(ssoId, ssoPassword);
    }
}
```

### 5. SystemEventHandler — 시스템 이벤트 어댑터

제니퍼 시스템 이벤트(서버 상태 변경, 사용자 조작 등)가 발생했을 때 호출됩니다.

```java
import com.aries.extension.handler.SystemEventHandler;
import com.aries.extension.data.SystemEventData;

public class SystemEventAdapter implements SystemEventHandler {
    @Override
    public void on(SystemEventData[] events) {
        for (SystemEventData data : events) {
            // data.name: 이벤트 종류 (아래 목록 참조)
            // data.messages: 이벤트 상세 메시지
            // data.dataServer: 관련 데이터 서버 정보
        }
    }
}
```

#### 주요 시스템 이벤트 종류 (`name`)

| 이벤트 이름 (Name) | 설명 |
|-------------------|------|
| `DATA_SERVER_DOWN` | 데이터 서버와의 연결이 중단됨 |
| `LICENSE_EXPIRE_SOON` | 라이선스 만료 임박 |
| `USER_SIGNED_UP` | 새로운 사용자 등록 |
| `DATASERVER_NOT_ENOUGH_DISK` | 데이터 서버 디스크 공간 부족 |
| `PENDING_AGENT_OPTION` | 에이전트 옵션 적용 대기 중 |
| `SETTING_CHANGED` | 서버 설정 변경 (사용자 조작 로그) |
| `TALK_ACTIVITY` | 제니퍼 톡(Talk) 활동 발생 |

### 6. InstanceData & K8s — 쿠버네티스 메타데이터

확장 모듈 1.5.8 버전부터 `EventData`와 `TransactionData`에 `instanceData` 필드가 포함되어 쿠버네티스 메타데이터를 포함한 상세 인스턴스 컨텍스트를 제공합니다.

#### InstanceData 필드

| 필드 | 타입 | 설명 |
|-------|------|-------------|
| `domainId` | `short` | 도메인 ID |
| `domainGroupHierarchy` | `List<String>` | 도메인 그룹 계층 |
| `domainName` | `String` | 도메인 이름 |
| `domainDescription` | `String` | 도메인 설명 |
| `instanceId` | `int` | 인스턴스 ID |
| `instanceName` | `String` | 인스턴스 이름 |
| `version` | `String` | 에이전트 버전 |
| `description` | `String` | 인스턴스 설명 |
| `ipAddress` | `String` | IP 주소 |
| `platform` | `String` | 플랫폼 정보 |
| `hostName` | `String` | 호스트 이름 |
| `configFilePath` | `String` | 에이전트 설정 파일 경로 |
| `k8s` | `K8s` | 쿠버네티스 메타데이터 (nullable) |

#### K8s 필드

| 필드 | 타입 | 설명 |
|-------|------|-------------|
| `containerIdHint` | `String` | 컨테이너 ID 힌트 |
| `podUid` | `String` | Pod UID |
| `containerName` | `String` | 컨테이너 이름 |
| `nodeName` | `String` | 노드 이름 |

#### 사용 예시

```java
public class EventAdapter implements EventHandler {
    @Override
    public void on(EventData[] events) {
        for (EventData data : events) {
            if (data.instanceData != null && data.instanceData.k8s != null) {
                LogUtil.info("Pod UID: " + data.instanceData.k8s.podUid);
                LogUtil.info("Container: " + data.instanceData.k8s.containerName);
                LogUtil.info("Node: " + data.instanceData.k8s.nodeName);
            }
        }
    }
}
```

## 유틸리티 클래스

### PropertyUtil

제니퍼 뷰 서버에 설정된 어댑터별 속성(Property) 값을 읽습니다.

```java
// PropertyUtil.getValue(adapterId, key, defaultValue)
String value = PropertyUtil.getValue("event_adapter", "subject", "기본값");
```

### LogUtil

제니퍼 뷰 서버의 로그 시스템에 로그를 기록합니다.

```java
LogUtil.info("정보 메시지");
LogUtil.error("에러 메시지");
```

## 배포 단계

1. `./mvnw clean package` 명령어로 빌드 수행
2. `dist/extension-tutorial-1.0.1.jar` 파일을 제니퍼 뷰 서버의 확장 모듈 디렉토리로 복사
3. 제니퍼 뷰 서버 관리 콘솔에서 어댑터를 등록하고 활성화

## Kotlin 예시

모든 어댑터는 Kotlin으로도 구현되어 있습니다. `com.aries.tutorial2.adapter` 패키지를 참조하세요.

```kotlin
class EventAdapter : EventHandler {
    override fun on(events: Array<EventData>) {
        for (data in events) {
            LogUtil.info("도메인 ID : ${data.domainId}")
        }
    }
}
```

## 라이선스 (License)

Copyright (c) JenniferSoft Inc.
