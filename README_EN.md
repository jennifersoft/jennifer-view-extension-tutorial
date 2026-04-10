# JENNIFER View Extension Tutorial

An all-in-one tutorial project for developing JENNIFER View Server extension modules.  
Provides adapter implementation examples in both Java and Kotlin.

## Requirements

- Java 17+
- Maven 3.x (or use the included Maven Wrapper)
- `com.aries.extension` 1.5.8

## Project Structure

```
src/
├── main/
│   ├── java/com/aries/tutorial/adapter/     # Java adapter examples
│   │   ├── EventAdapter.java                # Event handler
│   │   ├── LoginAdapter.java                # Login handler
│   │   ├── SSOLoginAdapter.java             # SSO login handler
│   │   ├── SystemEventAdapter.java          # System event handler
│   │   └── TransactionAdapter.java          # Transaction handler
│   └── kotlin/com/aries/tutorial2/adapter/  # Kotlin adapter examples
│       ├── EventAdapter.kt
│       ├── LoginAdapter.kt
│       ├── SSOLoginAdapter.kt
│       ├── SystemEventAdapter.kt
│       └── TransactionAdapter.kt
dist/                                        # Build output (JAR)
```

## Build

```bash
# Using Maven Wrapper
./mvnw clean package

# Using system Maven
mvn clean package
```

The build output is generated at `dist/extension-tutorial-1.0.1.jar`.

## Libraries Provided by View Server (Exclude from Packaging)

The JENNIFER View Server already includes the following libraries. If you use these libraries when developing an extension module, you must set the dependency scope to `provided` in `pom.xml` so that they are not included in the build output (JAR).

**⚠️ Caution: For external libraries not listed below, set the dependency scope to the default (`compile`) to ensure they are packaged within the JAR file during the build.**

| Category | Library (Artifact ID) | Version |
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
| **Logging** | `logback-classic`, `jcl-over-slf4j` | (Built-in) |
| **Kotlin** | `kotlin-stdlib` | (Built-in) |

## Adapter Interface Guide

### 1. EventHandler — Event Adapter

Called when application events (errors, warnings, etc.) occur.

```java
import com.aries.extension.handler.EventHandler;
import com.aries.extension.data.EventData;

public class EventAdapter implements EventHandler {
    @Override
    public void on(EventData[] events) {
        // Event information is passed in the events array
        // EventData key fields:
        //   domainId, instanceName, businessName,
        //   txid, serviceName, errorType, eventLevel,
        //   instanceData (see InstanceData & K8s section)
    }
}
```

### 2. TransactionHandler — Transaction Adapter

Called when a transaction completes.

```java
import com.aries.extension.handler.TransactionHandler;
import com.aries.extension.data.TransactionData;

public class TransactionAdapter implements TransactionHandler {
    @Override
    public void on(TransactionData[] transactions) {
        // TransactionData key fields:
        //   domainId, instanceName, txid,
        //   responseTime, applicationName,
        //   instanceData (see InstanceData & K8s section)
    }
}
```

### 3. LoginHandler — Login Adapter

Customizes the authentication method for JENNIFER View Server login.

```java
import com.aries.extension.handler.LoginHandler;
import com.aries.extension.data.UserData;

public class LoginAdapter implements LoginHandler {
    @Override
    public UserData preHandle(String id, String password) {
        // On success: return new UserData(id, password, role, name)
        // On failure: return null
        return null;
    }

    @Override
    public String redirect(String id, String password) {
        // Return the path to redirect to after successful login
        return "/dashboard/realtimeAdmin";
    }
}
```

`UserData` constructor parameters:

| Parameter | Description |
|-----------|-------------|
| `id` | User ID |
| `password` | Password |
| `role` | Role (e.g., `"admin"`) |
| `name` | Display Name |

### 4. SSOLoginHandler — SSO Login Adapter

Handles login through SSO (Single Sign-On) authentication.

```java
import com.aries.extension.handler.SSOLoginHandler;
import com.aries.extension.data.UserData;
import javax.servlet.http.HttpServletRequest;

public class SSOLoginAdapter implements SSOLoginHandler {
    @Override
    public UserData preHandle(HttpServletRequest request) {
        // Extract SSO authentication information from HTTP request headers
        String ssoId = request.getHeader("SSO_ID");
        String ssoPassword = request.getHeader("SSO_PASSWORD");

        // On success: return new UserData(id, password)
        // On failure: return null
        return new UserData(ssoId, ssoPassword);
    }
}
```

### 5. SystemEventHandler — System Event Adapter

Called when JENNIFER system events (server status changes, user operations, etc.) occur.

```java
import com.aries.extension.handler.SystemEventHandler;
import com.aries.extension.data.SystemEventData;

public class SystemEventAdapter implements SystemEventHandler {
    @Override
    public void on(SystemEventData[] events) {
        for (SystemEventData data : events) {
            // data.name: Event type (see list below)
            // data.messages: Detailed event message
            // data.dataServer: Related Data Server info
        }
    }
}
```

#### Major System Event Types (`name`)

| Event Name | Description |
|-------------------|------|
| `DATA_SERVER_DOWN` | Connection to the Data Server is lost |
| `LICENSE_EXPIRE_SOON` | License expiration is imminent |
| `USER_SIGNED_UP` | A new user has registered |
| `DATASERVER_NOT_ENOUGH_DISK` | Insufficient disk space on the Data Server |
| `PENDING_AGENT_OPTION` | Agent option application is pending |
| `SETTING_CHANGED` | Server settings changed (User operation log) |
| `TALK_ACTIVITY` | JENNIFER Talk activity occurred |

### 6. InstanceData & K8s — Kubernetes Metadata

Starting from extension module version 1.5.8, `EventData` and `TransactionData` include an `instanceData` field that provides detailed instance context, including Kubernetes metadata.

#### InstanceData Fields

| Field | Type | Description |
|-------|------|-------------|
| `domainId` | `short` | Domain ID |
| `domainGroupHierarchy` | `List<String>` | Domain group hierarchy |
| `domainName` | `String` | Domain name |
| `domainDescription` | `String` | Domain description |
| `instanceId` | `int` | Instance ID |
| `instanceName` | `String` | Instance name |
| `version` | `String` | Agent version |
| `description` | `String` | Instance description |
| `ipAddress` | `String` | IP address |
| `platform` | `String` | Platform information |
| `hostName` | `String` | Host name |
| `configFilePath` | `String` | Agent configuration file path |
| `k8s` | `K8s` | Kubernetes metadata (nullable) |

#### K8s Fields

| Field | Type | Description |
|-------|------|-------------|
| `containerIdHint` | `String` | Container ID hint |
| `podUid` | `String` | Pod UID |
| `containerName` | `String` | Container name |
| `nodeName` | `String` | Node name |

#### Usage Example

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

## Utility Classes

### PropertyUtil

Reads adapter-specific property values configured in the JENNIFER View Server.

```java
// PropertyUtil.getValue(adapterId, key, defaultValue)
String value = PropertyUtil.getValue("event_adapter", "subject", "Default value");
```

#### How to Set Adapter ID
1.  Access the **JENNIFER Management Console**: Go to `Settings` > `Extension Module`.
2.  **Register Adapter**: In the settings window for each adapter (Event, Transaction, etc.), enter a value in the **[Adapter ID]** field (e.g., `event_adapter`).
3.  **Register Properties**: Enter Key (e.g., `subject`) and Value in the [Custom Properties] section at the bottom and save.
4.  In your source code, pass the **[Adapter ID]** set above as the first parameter to read the value.

### LogUtil

Records logs in the JENNIFER View Server log system.

```java
LogUtil.info("Info message");
LogUtil.error("Error message");
```

## Cautions and Naming Conventions

Extension modules are dynamically loaded by the JENNIFER View Server's class loader. To ensure stable operation, the following rules must be strictly followed.

### 1. Use Unique Package Names
Do not use package names used internally by the JENNIFER server (`com.aries.*`, `aries.*`) to prevent conflicts with the server's internal classes.
*   **Recommended**: `com.yourcompany.jennifer.extension.*`
*   **Avoid**: `com.aries.view.*`, `aries.core.*`

### 2. Do Not Use the Default Package
All classes must be declared within an explicit package. Classes without a package may not be recognized by the server or may cause load errors.

### 3. Enter Full Qualified Class Name (FQCN)
When registering an adapter in the JENNIFER Management Console, you must enter the **full class name** including the package name in the [Class Name] field.
*   Example: `com.aries.tutorial.adapter.EventAdapter` (O)
*   Example: `EventAdapter` (X)

### 4. Beware of Library Duplication
If you include additional external libraries other than those set to the `provided` scope, check if the library already exists on the server. Conflicts may occur if duplicate libraries with different versions are present.

## Deployment Steps

1. Build using the `./mvnw clean package` command.
2. Copy the `dist/extension-tutorial-1.0.1.jar` file to the JENNIFER View Server's extension module directory.
3. Register and activate the adapter in the JENNIFER View Server Management Console.

## Kotlin Examples

All adapters are also implemented in Kotlin. Refer to the `com.aries.tutorial2.adapter` package.

```kotlin
class EventAdapter : EventHandler {
    override fun on(events: Array<EventData>) {
        for (data in events) {
            LogUtil.info("Domain ID : ${data.domainId}")
        }
    }
}
```

## License

Copyright (c) JenniferSoft Inc.
