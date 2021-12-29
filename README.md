<p align="center">
  <a href="https://fingerprintjs.com">
    <img src="https://user-images.githubusercontent.com/10922372/129346814-a4e95dbf-cd27-49aa-ae7c-f23dae63b792.png" alt="FingerprintJS" width="312px" />
  </a>
</p>
<p align="center">
  <a href="https://jitpack.io/#fingerprintjs/fingerprintjs-pro-android">
    <img src="https://jitpack.io/v/fingerprintjs/fingerprintjs-pro-android.svg" alt="Latest release">
  </a>
  <a href="https://discord.gg/39EpE2neBg">
    <img src="https://img.shields.io/discord/852099967190433792?style=logo&label=Discord&logo=Discord&logoColor=white" alt="Discord server">
  </a>
</p>

# FingerprintJS Pro Android

```kotlin
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory
...

// Trust your user's identifiers with the FingerprintJS Pro library.

val fpjsClient = FPJSProFactory(applicationContext).createInstance(
     Configuration(
        apiToken = "BROWSER_TOKEN"
  )
)

fpjsClient.getVisitorId { visitorId ->
  // Prevent fraud cases in your apps by using
  // a unique stick and reliable ID provided by FingerprintJS Pro.
}

```

**#1 library for Android device identification.**  Retrieve a [FingerprintJS Pro](https://fingerprintjs.com/) visitor identifier in a native mobile app. This library communicates with the FingerprintJS Pro API and requires [browser token](https://dev.fingerprintjs.com/docs). For client-side only Android fingerprinting take a look at [fingerprint-android](https://github.com/fingerprintjs/fingerprint-android) repository instead. If you are interested in the iOS platform, you can also check our [FingerprintJS Pro iOS](https://github.com/fingerprintjs/fingerprintjs-pro-ios).


## Installation

#### 1. Add the repository to the gradle.

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
#### 2. Add a dependency to your `build.gradle` file

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

#### 3. Get the visitor identifier

Retrieve the visitor identifier using browser token. You can find your [browser token](https://dev.fingerprintjs.com/docs) in your [dashboard](https://dashboard.fingerprintjs.com/subscriptions/).

##### 3.1 Kotlin example
```kotlin
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory
...

// Initialization
val factory = FPJSProFactory(applicationContext)
val configuration = Configuration(
    apiToken = "BROWSER_TOKEN"
  )
 
val fpjsClient = factory.createInstance(
    configuration
)

// Usage
fpjsClient.getVisitorId { visitorId ->
    // Use visitorId
}

```
##### 3.2 Java example

```java
import com.fingerprintjs.android.fpjs_pro.Configuration;
import com.fingerprintjs.android.fpjs_pro.FPJSProClient;
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory;
...

FPJSProFactory factory = new FPJSProFactory(this.getApplicationContext());
Configuration configuration = new Configuration(
    "BROWSER_TOKEN"
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
Also there is an ability to retrie visitor identifier using our native library in combination with signals from the FingerprintJS Pro browser agent in the webview on the JavaScript level. For using FingerprintJS Pro Android inside of a WEB-based application check the [full API reference](docs/client_api.md).

## Additional Resources
[FingerprintJS Pro documentation](https://dev.fingerprintjs.com/docs)

## License
This library is MIT licensed.
