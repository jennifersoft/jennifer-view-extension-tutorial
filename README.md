# JENNIFER 뷰 확장 튜토리얼 (JENNIFER View Extension Tutorial)

JENNIFER 뷰 서버 확장 모듈 개발을 위한 올인원 튜토리얼 프로젝트입니다.  
Java와 Kotlin 두 가지 언어로 어댑터 구현 예시를 제공합니다.

> **참고**: 이 프로젝트는 **JENNIFER 뷰 서버 5.6.5.8** 버전을 기준으로 작성되었습니다.

## 요구 사항

- Java 17+
- Maven 3.x (또는 포함된 Maven Wrapper 사용)
- `com.aries.extension` 1.5.8

## 프로젝트 구조

```
src/
├── main/
│   ├── java/com/aries/tutorial/
│   │   ├── adapter/                         # Java 어댑터 예시
│   │   │   ├── EventAdapter.java            # 이벤트 핸들러
│   │   │   ├── LoginAdapter.java            # 로그인 핸들러
│   │   │   ├── SSOLoginAdapter.java         # SSO 로그인 핸들러
│   │   │   ├── SystemEventAdapter.java      # 시스템 이벤트 핸들러
│   │   │   └── TransactionAdapter.java      # 트랜잭션 핸들러
│   │   └── util/
│   │       └── AdapterFormatter.java        # 어댑터 로그 한 줄 포매터
│   └── kotlin/com/aries/tutorial2/
│       ├── adapter/                         # Kotlin 어댑터 예시
│       │   ├── EventAdapter.kt
│       │   ├── LoginAdapter.kt
│       │   ├── SSOLoginAdapter.kt
│       │   ├── SystemEventAdapter.kt
│       │   └── TransactionAdapter.kt
│       └── util/
│           └── AdapterFormatter.kt          # 어댑터 로그 한 줄 포매터
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

#### 어댑터 ID 설정 방법
1.  **제니퍼 관리 콘솔** 접속: `설정` > `SMTP 및 어댑터 (+DB Plan)` 메뉴로 이동합니다.
2.  **어댑터 설정 진입**: 어댑터 테이블 우측의 버튼을 클릭하고 나오는 메뉴에서 **[옵션]**을 선택합니다.
3.  **어댑터 ID 입력**: 설정 팝업창에서 **[어댑터 ID]** 필드에 값을 입력합니다 (예: `event_adapter`).
4.  **속성 등록**: 동일한 팝업창의 [사용자 정의 속성] 영역에 Key(예: `subject`)와 Value를 입력하고 저장합니다.
5.  소스 코드에서 위에서 설정한 **[어댑터 ID]**를 첫 번째 파라미터로 전달하여 값을 읽어옵니다.

### LogUtil

제니퍼 뷰 서버의 로그 시스템에 로그를 기록합니다.

```java
LogUtil.info("정보 메시지");
LogUtil.error("에러 메시지");
```

### AdapterFormatter (튜토리얼 자체 유틸)

어댑터마다 데이터 필드를 한 줄씩 따로 출력하면 로그가 길어지고 어떤 어댑터에서 나온 줄인지 구분하기 어렵습니다. 이 튜토리얼은 `com.aries.tutorial.util.AdapterFormatter` (Java) / `com.aries.tutorial2.util.AdapterFormatter` (Kotlin) 라는 자체 유틸을 두어, 데이터 1건을 한 줄로 포매팅하면서 어댑터 종류를 시각적으로 구분합니다.

#### 어댑터별 글리프 + 태그

| 어댑터 | 글리프 | 태그 | 포매터 메서드 |
|---|---|---|---|
| EventAdapter | `▸` | `EVT` | `formatEvent(idx, EventData)` |
| TransactionAdapter | `◆` | `TXN` | `formatTransaction(idx, TransactionData)` |
| SystemEventAdapter | `■` | `SYS` | `formatSystemEvent(idx, SystemEventData)` |
| LoginAdapter | `★` | `LGN` | `formatLogin(attemptedId, UserData?)` |
| SSOLoginAdapter | `☆` | `SSO` | `formatSsoLogin(ssoHeaderId, UserData?)` |

`grep`은 태그(`EVT`, `TXN` 등)로, 사람 눈은 글리프(`▸`, `◆` 등)로 구분합니다. 박스 드로잉 문자(`│`)와 앵글 브래킷(`⟨⟩`)은 일반 텍스트와 시각적으로 구분되는 효과를 줍니다. 소스 인코딩이 UTF-8(`pom.xml` 명시)이라 안전합니다.

#### 출력 예시

```
[EventAdapter] - subject (events=2)
▸ EVT  #1  ⟨ERROR⟩  domain#1·my-was  │  biz=order-api  │  tx=918273645  │  svc=/api/orders  │  NullPointerException
▸ EVT  #2  ⟨WARN⟩   domain#1·my-was  │  biz=order-api  │  tx=918273646  │  svc=/api/items   │  TimeoutException

[TransactionAdapter] - subject (transactions=1)
◆ TXN  #1  domain#1·my-was  │  tx=918273645  │  app=order-api  │  120ms  │  err=-

[SystemEventAdapter] - subject (events=1)
■ SYS  #1  domain#1  │  subject=DATA_SERVER_DOWN  │  ds=ds01  │  message=connection lost
```

#### 호출 사이트 (Java 예시)

```java
import com.aries.tutorial.util.AdapterFormatter;

@Override
public void on(EventData[] events) {
    int idx = 0;
    for (EventData data : events) {
        idx++;
        LogUtil.info(AdapterFormatter.formatEvent(idx, data));
    }
}
```

#### 보안 결정이 남아 있는 부분

`formatLogin` / `formatSsoLogin` 두 메서드는 보안에 민감한 결정 — 실패 시 ID 노출/마스킹, 성공/실패 표시 글리프, 출력할 `UserData` 필드 선택 — 이 학습자의 몫으로 남겨져 있습니다. 두 파일 안의 TODO 주석에 trade-off가 정리되어 있으니, 본인의 보안 정책에 맞게 본문을 채워 보세요. 비밀번호와 그 길이/해시는 어떤 형태로도 출력하지 마세요.

## 주의 사항 및 명명 규칙 (Naming Conventions)

확장 모듈은 JENNIFER 뷰 서버의 클래스로더에 의해 동적으로 로드됩니다. 안정적인 동작을 위해 아래 규칙을 반드시 준수해야 합니다.

### 1. 고유한 패키지명 사용
JENNIFER 서버 내부 클래스와의 충돌을 방지하기 위해, 서버가 사용하는 패키지명(`com.aries.*`, `aries.*`)은 사용하지 마십시오.
*   **권장**: `com.yourcompany.jennifer.extension.*`
*   **피해야 할 예**: `com.aries.view.*`, `aries.core.*`

### 2. 기본 패키지(Default Package) 사용 금지
모든 클래스는 반드시 명시적인 패키지 내에 선언되어야 합니다. 패키지가 없는 클래스는 서버에서 인식하지 못하거나 로드 에러가 발생할 수 있습니다.

### 3. 전체 클래스명(FQCN) 입력
제니퍼 관리 콘솔에서 어댑터를 등록할 때, [클래스명] 필드에는 반드시 패키지명을 포함한 **전체 클래스명**을 입력해야 합니다.
*   예: `com.aries.tutorial.adapter.EventAdapter` (O)
*   예: `EventAdapter` (X)

### 4. 라이브러리 중복 주의
`provided` 스코프로 설정된 라이브러리 외에 추가적인 외부 라이브러리를 포함할 경우, 해당 라이브러리가 이미 서버에 존재하는지 확인하십시오. 버전이 다른 동일 라이브러리가 중복될 경우 클래스 충돌이 발생할 수 있습니다.

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
