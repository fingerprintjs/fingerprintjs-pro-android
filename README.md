<p align="center">
  <a href="https://fingerprintjs.com">
    <img src="res/logo.svg" alt="FingerprintJS" width="312px" />
  </a>
</p>
<p align="center">
  <a href="https://jitpack.io/#fingerprintjs/fingerprintjs-pro-android">
    <img src="https://jitpack.io/v/fingerprintjs/fingerprintjs-pro-android.svg" alt="Latest release">
  </a>
  <a href="https://discord.gg/39EpE2neBg">
    <img src="https://img.shields.io/discord/852099967190433792?style=logo&label=Discord&logo=Discord&logoColor=white" alt="Discord server">
  </a>
    <a href="https://android-arsenal.com/api?level=21">
    <img src="https://img.shields.io/badge/API-21%2B-brightgreen.svg" alt="Android minAPI status">
  </a>
</p>

# FingerprintJS Pro Android 
### Official Androd library for 100% accurate device identification, created for the FingerprintJS Pro API.

```kotlin
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory
...

// Trust your user's identifiers with the FingerprintJS Pro

val fpjsClient = FPJSProFactory(applicationContext).createInstance(
     Configuration(
        apiToken = "MY_API_TOKEN"
  )
)

fpjsClient.getVisitorId { visitorId ->
  // Prevent fraud cases in your apps with a unique
  // sticky and reliable ID provided by FingerprintJS Pro.
}

```

## Introduction

FingerprintJS Pro is a professional visitor identification service that processes all information server-side and transmits it securely to your servers using server-to-server APIs.

This identification library generates an accurate, sticky, and stable [FingerprintJS Pro](https://fingerprintjs.com/) visitor identifier in Android apps. The identifier is linked to a device, i.e. it is the same in all the apps on a device, even if you reinstall or clone them. This library requires a [free API key](https://dashboard.fingerprintjs.com/signup) to connect to the FingerprintJS Pro API. 

For local Android fingerprinting and identifying without making requests to API take a look at the [fingerprintjs-android](https://github.com/fingerprintjs/fingerprintjs-android) repository. 

If you are interested in the iOS platform, you can also check our [FingerprintJS Pro iOS](https://github.com/fingerprintjs/fingerprintjs-pro-ios).


## Quick start

#### Add the repository to the gradle.

If your version of Gradle is earlier than 7, add these lines to your `build.gradle`.


```gradle
allprojects {	
  repositories {
  ...
  maven { url 'https://jitpack.io' }	
}}
```

If your version of Gradle is 7 or newer, add these lines to your `settings.gradle`.
```gradle
repositories {
  ...
  maven { url "https://jitpack.io" }
}
```
#### Add a dependency to your `build.gradle` file

```gradle
dependencies {
  implementation 'com.github.fingerprintjs:fingerprintjs-pro-android:v1.2.0'

  // If you use Java for you project, add also this line
  implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
```

When using Kotlin, also make sure you have specified Kotlin version in your `build.gradle` file:
```gradle
buildscript {
    ext.kotlin_version = 'your-kotlin-version'
    ...
```
*Note: You can find your Kotlin version in Android Studio > File > Settings > Languages & Frameworks > Kotlin.*

Sync gradle settings.

*Note: The library depends on [kotlin-stdlib](https://kotlinlang.org/api/latest/jvm/stdlib/). If your application is written in Java, add `kotlin-stdlib` dependency first (it's lightweight and has excellent backward and forward compatibility).*

#### Get the visitor identifier

Retrieve the visitor identifier using browser token. You can find your [browser token](https://dev.fingerprintjs.com/docs) in your [dashboard](https://dashboard.fingerprintjs.com/subscriptions/).

##### Kotlin example
```kotlin
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory
...

// Initialization
val factory = FPJSProFactory(applicationContext)
val configuration = Configuration(
    apiToken = "TOKEN"
  )
 
val fpjsClient = factory.createInstance(
    configuration
)

// Usage
fpjsClient.getVisitorId { visitorId ->
    // Use visitorId
}

```
##### Java example

```java
import com.fingerprintjs.android.fpjs_pro.Configuration;
import com.fingerprintjs.android.fpjs_pro.FPJSProClient;
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory;
...

FPJSProFactory factory = new FPJSProFactory(this.getApplicationContext());
Configuration configuration = new Configuration(
    "TOKEN"
    );

FPJSProClient fpjsClient = factory.createInstance(
    configuration
);

fpjsClient.getVisitorId(new Function1<String, Unit>() {
    @Override
    public Unit invoke(String visitorId) {
        // Use visitorId
        return null;
    }
});
```

## Additional Resources
- [Full API reference](docs/client_api.md).
- [Server-to-Server API](https://dev.fingerprintjs.com/docs/server-api)
- [FingerprintJS Pro documentation](https://dev.fingerprintjs.com/docs)

## License
This library is MIT licensed. Copyright FingerprintJS, Inc 2021-2022 (c)
