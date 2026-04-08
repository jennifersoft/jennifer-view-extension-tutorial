# JENNIFER View Extension Tutorial

An all-in-one tutorial project for developing JENNIFER View Server extension modules.  
Provides adapter implementation examples in both Java and Kotlin.

## Requirements

- Java 1.8+
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

# Or using system Maven
mvn clean package
```

The build output is generated at `dist/extension-tutorial-1.0.1.jar`.

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

Customizes authentication for JENNIFER View Server login.

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
        // Return the redirect path after successful login
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
| `name` | Display name |

### 4. SSOLoginHandler — SSO Login Adapter

Handles login via SSO (Single Sign-On) authentication.

```java
import com.aries.extension.handler.SSOLoginHandler;
import com.aries.extension.data.UserData;
import javax.servlet.http.HttpServletRequest;

public class SSOLoginAdapter implements SSOLoginHandler {
    @Override
    public UserData preHandle(HttpServletRequest request) {
        // Extract SSO credentials from HTTP request headers
        String ssoId = request.getHeader("SSO_ID");
        String ssoPassword = request.getHeader("SSO_PASSWORD");

        // On success: return new UserData(id, password)
        // On failure: return null
        return new UserData(ssoId, ssoPassword);
    }
}
```

### 5. SystemEventHandler — System Event Adapter

Called when JENNIFER system events (server status changes, etc.) occur.

```java
import com.aries.extension.handler.SystemEventHandler;
import com.aries.extension.data.SystemEventData;

public class SystemEventAdapter implements SystemEventHandler {
    @Override
    public void on(SystemEventData[] events) {
        // SystemEventData key fields:
        //   subject, message, dataServer
    }
}
```

### 6. InstanceData & K8s — Kubernetes Metadata

Starting with extension 1.5.8, `EventData` and `TransactionData` include an `instanceData` field that provides detailed instance context, including Kubernetes metadata.

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
| `platform` | `String` | Platform info |
| `hostName` | `String` | Host name |
| `configFilePath` | `String` | Agent config file path |
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

```kotlin
class EventAdapter : EventHandler {
    override fun on(events: Array<EventData>) {
        for (data in events) {
            data.instanceData?.k8s?.let { k8s ->
                LogUtil.info("Pod UID: ${k8s.podUid}")
                LogUtil.info("Container: ${k8s.containerName}")
                LogUtil.info("Node: ${k8s.nodeName}")
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
String value = PropertyUtil.getValue("event_adapter", "subject", "Unknown subject");
```

### LogUtil

Writes logs to the JENNIFER View Server log system.

```java
LogUtil.info("Info message");
LogUtil.error("Error message");
```

## Deployment

1. Build with `./mvnw clean package`
2. Copy `dist/extension-tutorial-1.0.1.jar` to the JENNIFER View Server's extension module directory
3. Register and activate the adapter in the JENNIFER View Server admin console

## Kotlin Examples

All adapters are also implemented in Kotlin. See the `com.aries.tutorial2.adapter` package.

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
