# JENNIFER ビュー拡張チュートリアル (JENNIFER View Extension Tutorial)

JENNIFER ビューサーバー拡張モジュールの開発のためのオールインワンチュートリアルプロジェクトです。  
JavaとKotlinの2つの言語でアダプターの実装例を提供します。

> **備考**: 本プロジェクトは **JENNIFER ビューサーバー 5.6.5.8** バージョンを基準に作成されました。

## 要求事項

- Java 17+
- Maven 3.x (または同梱の Maven Wrapper を使用)
- `com.aries.extension` 1.5.8

## プロジェクト構造

```
src/
├── main/
│   ├── java/com/aries/tutorial/adapter/     # Java アダプター例
│   │   ├── EventAdapter.java                # イベントハンドラー
│   │   ├── LoginAdapter.java                # ログインハンドラー
│   │   ├── SSOLoginAdapter.java             # SSO ログインハンドラー
│   │   ├── SystemEventAdapter.java          # システムイベントハンドラー
│   │   └── TransactionAdapter.java          # トランザクションハンドラー
│   └── kotlin/com/aries/tutorial2/adapter/  # Kotlin ア다プター例
│       ├── EventAdapter.kt
│       ├── LoginAdapter.kt
│       ├── SSOLoginAdapter.kt
│       ├── SystemEventAdapter.kt
│       └── TransactionAdapter.kt
dist/                                        # ビルド出力物 (JAR)
```

## ビルド方法

```bash
# Maven Wrapper 使用時
./mvnw clean package

# システム Maven 使用時
mvn clean package
```

ビルド結果は `dist/extension-tutorial-1.0.1.jar` に生成されます。

## ビューサーバー提供ライブラリ (パッキング除外対象)

JENNIFER ビューサーバーには以下のライブラリが既に含まれています。拡張モジュール開発時にこれらのライブラリを使用する場合、`pom.xml` で依存関係のスコープ (scope) を `provided` に設定し、ビルド結果 (JAR) に含まれないようにする必要があります。

**⚠️ 注意: 以下のリストにない外部ライブラリを追加で使用する場合は、依存関係のスコープをデフォルト (`compile`) に設定し、ビルド時に JAR ファイルに同梱されるようにしてください。**

| 分類 | ライブラリ (Artifact ID) | バージョン |
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
| **Logging** | `logback-classic`, `jcl-over-slf4j` | (内蔵) |
| **Kotlin** | `kotlin-stdlib` | (内蔵) |

## アダプターインターフェースガイド

### 1. EventHandler — イベントアダプター

アプリケーションイベント (エラー、警告など) が発生したときに呼び出されます。

```java
import com.aries.extension.handler.EventHandler;
import com.aries.extension.data.EventData;

public class EventAdapter implements EventHandler {
    @Override
    public void on(EventData[] events) {
        // イベント情報が events 配列で渡されます。
        // EventData の主なフィールド:
        //   domainId, instanceName, businessName,
        //   txid, serviceName, errorType, eventLevel,
        //   instanceData (InstanceData & K8s セクション参照)
    }
}
```

### 2. TransactionHandler — トランザクションアダプター

トランザクションが完了したときに呼び出されます。

```java
import com.aries.extension.handler.TransactionHandler;
import com.aries.extension.data.TransactionData;

public class TransactionAdapter implements TransactionHandler {
    @Override
    public void on(TransactionData[] transactions) {
        // TransactionData の主なフィールド:
        //   domainId, instanceName, txid,
        //   responseTime, applicationName,
        //   instanceData (InstanceData & K8s セクション参照)
    }
}
```

### 3. LoginHandler — ログインアダプター

JENNIFER ビューサーバーログインの認証方式をカスタマイズします。

```java
import com.aries.extension.handler.LoginHandler;
import com.aries.extension.data.UserData;

public class LoginAdapter implements LoginHandler {
    @Override
    public UserData preHandle(String id, String password) {
        // 成功時: new UserData(id, password, role, name) を返す
        // 失敗時: null を返す
        return null;
    }

    @Override
    public String redirect(String id, String password) {
        // ログイン成功後にリダイレクトするパスを返す
        return "/dashboard/realtimeAdmin";
    }
}
```

`UserData` コンストラクタパラメータ:

| パラメータ | 説明 |
|-----------|-------------|
| `id` | ユーザー ID |
| `password` | パスワード |
| `role` | 権限 (例: `"admin"`) |
| `name` | 表示名 |

### 4. SSOLoginHandler — SSO ログインアダプター

SSO (Single Sign-On) 認証によるログインを処理します。

```java
import com.aries.extension.handler.SSOLoginHandler;
import com.aries.extension.data.UserData;
import javax.servlet.http.HttpServletRequest;

public class SSOLoginAdapter implements SSOLoginHandler {
    @Override
    public UserData preHandle(HttpServletRequest request) {
        // HTTP リクエストヘッダーから SSO 認証情報を抽出
        String ssoId = request.getHeader("SSO_ID");
        String ssoPassword = request.getHeader("SSO_PASSWORD");

        // 成功時: new UserData(id, password) を返す
        // 失敗時: null を返す
        return new UserData(ssoId, ssoPassword);
    }
}
```

### 5. SystemEventHandler — システムイベントアダプター

JENNIFER システムイベント (サーバーの状態変更、ユーザー操作など) が発生したときに呼び出されます。

```java
import com.aries.extension.handler.SystemEventHandler;
import com.aries.extension.data.SystemEventData;

public class SystemEventAdapter implements SystemEventHandler {
    @Override
    public void on(SystemEventData[] events) {
        for (SystemEventData data : events) {
            // data.name: イベントの種類 (以下のリストを参照)
            // data.messages: イベントの詳細メッセージ
            // data.dataServer: 関連するデータサーバー情報
        }
    }
}
```

#### 主なシステムイベントの種類 (`name`)

| イベント名 | 説明 |
|-------------------|------|
| `DATA_SERVER_DOWN` | データサーバーとの接続が中断された |
| `LICENSE_EXPIRE_SOON` | ライセンスの期限切れが間近 |
| `USER_SIGNED_UP` | 新しいユーザーが登録された |
| `DATASERVER_NOT_ENOUGH_DISK` | データサーバーのディスク容量不足 |
| `PENDING_AGENT_OPTION` | エージェントオプションの適用待機中 |
| `SETTING_CHANGED` | サーバー設定の変更 (ユーザー操作ログ) |
| `TALK_ACTIVITY` | JENNIFER Talk アクティビティの発生 |

### 6. InstanceData & K8s — クバネティスメタデータ

拡張モジュール 1.5.8 バージョンから `EventData` と `TransactionData` に `instanceData` フィールドが含まれ、クバネティスメタデータを含む詳細なインスタンスコンテキストを提供します。

#### InstanceData フィールド

| フィールド | 型 | 説明 |
|-------|------|-------------|
| `domainId` | `short` | ドメイン ID |
| `domainGroupHierarchy` | `List<String>` | ドメイングループ階層 |
| `domainName` | `String` | ドメイン名 |
| `domainDescription` | `String` | ドメイン説明 |
| `instanceId` | `int` | インスタンス ID |
| `instanceName` | `String` | インスタンス名 |
| `version` | `String` | エージェントバージョン |
| `description` | `String` | インスタンス説明 |
| `ipAddress` | `String` | IP アドレス |
| `platform` | `String` | プラットフォーム情報 |
| `hostName` | `String` | ホスト名 |
| `configFilePath` | `String` | エージェント設定ファイルのパス |
| `k8s` | `K8s` | クバネティスメタデータ (nullable) |

#### K8s フィールド

| フィールド | 型 | 説明 |
|-------|------|-------------|
| `containerIdHint` | `String` | コンテナ ID ヒント |
| `podUid` | `String` | Pod UID |
| `containerName` | `String` | コンテナ名 |
| `nodeName` | `String` | ノード名 |

#### 使用例

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

## ユーティリティクラス

### PropertyUtil

JENNIFER ビューサーバーに設定されたアダプターごとのプロパティ (Property) 値を読み取ります。

```java
// PropertyUtil.getValue(adapterId, key, defaultValue)
String value = PropertyUtil.getValue("event_adapter", "subject", "デフォルト値");
```

#### アダプター ID の設定方法
1.  **JENNIFER 管理コンソール**にアクセス: `構成管理` > `拡張モジュール` メニューに移動します。
2.  **アダプターの登録**: 各アダプター (イベント、トランザクションなど) の設定画面で、**[アダプター ID]** フィールドに値を入力します (例: `event_adapter`)。
3.  **プロパティの登録**: 下部の [カスタムプロパティ] 領域に Key (例: `subject`) と Value を入力して保存します。
4.  ソースコードで上記で設定した **[アダプター ID]** を第 1 パラメータとして渡して値を読み取ります。

### LogUtil

JENNIFER ビューサーバーのログシステムにログを記録します。

```java
LogUtil.info("情報メッセージ");
LogUtil.error("エラーメッセージ");
```

## 注意事項と命名規則 (Naming Conventions)

拡張モジュールは JENNIFER ビューサーバーのクラスローダーによって動的にロードされます。安定した動作のために、以下の規則を必ず遵守してください。

### 1. 固有のパッケージ名を使用
JENNIFER サーバー内部クラスとの衝突を防ぐために、サーバーが内部で使用するパッケージ名 (`com.aries.*`, `aries.*`) は使用しないでください。
*   **推奨**: `com.yourcompany.jennifer.extension.*`
*   **避けるべき例**: `com.aries.view.*`, `aries.core.*`

### 2. デフォルトパッケージ (Default Package) の使用禁止
すべてのクラスは必ず明示的なパッケージ内で宣言される必要があります。パッケージがないクラスはサーバーで認識されないか、ロードエラーが発生する可能性があります。

### 3. 完全修飾クラス名 (FQCN) の入力
JENNIFER 管理コンソールでアダプターを登録する際、[クラス名] フィールドには必ずパッケージ名を含む **完全修飾クラス名** を入力してください。
*   例: `com.aries.tutorial.adapter.EventAdapter` (O)
*   例: `EventAdapter` (X)

### 4. ライブラリの重複に注意
`provided` スコープで設定されたライブラリ以外の外部ライブラリを追加する場合、そのライブラリが既にサーバーに存在するか確認してください。バージョンの異なる同一ライブラリが重複すると、クラスの衝突が発生する可能性があります。

## デプロイ手順

1. `./mvnw clean package` コマンドでビルドを実行します。
2. `dist/extension-tutorial-1.0.1.jar` ファイルを JENNIFER ビューサーバーの拡張モジュールディレクトリにコピーします。
3. JENNIFER ビューサーバー管理コンソールでアダプターを登録し、有効化します。

## Kotlin 例

すべての変アダプターは Kotlin でも実装されています。`com.aries.tutorial2.adapter` パッケージを参照してください。

```kotlin
class EventAdapter : EventHandler {
    override fun on(events: Array<EventData>) {
        for (data in events) {
            LogUtil.info("ドメイン ID : ${data.domainId}")
        }
    }
}
```

## ライセンス

Copyright (c) JenniferSoft Inc.
