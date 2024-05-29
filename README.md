<p align="center">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="res/logo_light.svg" />
      <source media="(prefers-color-scheme: light)" srcset="res/logo_dark.svg" />
      <img src="res/logo_dark.svg" alt="Fingerprint logo" width="312px" />
    </picture>
</p>
<p align="center">
  <a href="https://discord.gg/39EpE2neBg">
    <img src="https://img.shields.io/discord/852099967190433792?style=logo&label=Discord&logo=Discord&logoColor=white" alt="Discord server">
  </a>
    <a href="https://android-arsenal.com/api?level=21">
    <img src="https://img.shields.io/badge/API-21%2B-brightgreen.svg" alt="Android minAPI status">
  </a>
</p>

# Fingerprint Pro Android
### Official Android library for 100% accurate device identification, created for the Fingerprint Pro API.

```kotlin
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FingerprintJSFactory
...

// Trust your user's identifiers with the Fingerprint Pro

val fpjsClient = FingerprintJSFactory(applicationContext).createInstance(
     Configuration(
        apiKey = "your-public-api-key"
  )
)

fpjsClient.getVisitorId { result ->
    val visitorId = result.visitorId
    // Use the visitorId
}
```


## Introduction

Fingerprint Pro is a professional visitor identification service that processes all information server-side and transmits it securely to your servers using server-to-server APIs.

This identification library generates an accurate, sticky, and stable [Fingerprint Pro](https://fingerprint.com/) visitor identifier in Android apps. The identifier is linked to a device, i.e. it is the same in all the apps on a device, even if you reinstall or clone them. This library requires a [free API key](https://dashboard.fingerprintjs.com/signup) to connect to the Fingerprint Pro API.

> [!TIP]
> Check out the [Fingerprint Pro Demo App](https://github.com/fingerprintjs/fingerprint-device-intelligence-android-demo) to better understand and experience the capabilities of our device intelligence platform.

For local Android fingerprinting and identifying without making requests to API take a look at the [fingerprintjs-android](https://github.com/fingerprintjs/fingerprintjs-android) repository.

If you are interested in the iOS platform, you can also check our [Fingerprint Pro iOS](https://github.com/fingerprintjs/fingerprintjs-pro-ios).


## Smart Signals

- Fingerprint PRO now supports a Smart Signals functionality, including Root Detection and Emulator detection. The results are available in the response of the `GET /events` [Server API method](https://dev.fingerprint.com/docs/native-android-integration#smart-signals).
- Access to the Smart Signals functionality is currently only available upon request for Enterprise customers. Contact Support ([support@fingerprint.com](mailto:support@fingerprint.com)) to enable it for your subscription.


## Quick start

#### Requirements

The Android SDK requires Android 5.0 (API level 21+) or higher.

#### Add the repository to the gradle.

If your version of Gradle is earlier than 7, add these lines to your `build.gradle`.


```gradle
allprojects {	
  repositories {
  ...
  maven { url 'https://maven.fpregistry.io/releases' }	
  maven { url 'https://jitpack.io' }	

}}
```

If your version of Gradle is 7 or newer, add these lines to your `settings.gradle`.
```gradle
repositories {
  ...
  maven { url 'https://maven.fpregistry.io/releases' }	
  maven { url 'https://jitpack.io' }	

}
```
#### Add the dependency to your `build.gradle` file.

```gradle
dependencies {
    ...
  implementation "com.fingerprint.android:pro:2.4.0"
}
```

Note: Fingerprint PRO Android uses [FingerprintJS Android](https://github.com/fingerprintjs/fingerprintjs-android), [kotlin-stdlib](https://kotlinlang.org/api/latest/jvm/stdlib/) and [androidx.core:core](https://developer.android.com/jetpack/androidx/releases/core) as dependencies. Additionally, [com.google.android.gms:play-services-location](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary) is required when [Geolocation Spoofing Detection Smart Signal](https://dev.fingerprint.com/docs/smart-signals-overview#geolocation-spoofing-detection) functionality is used.

#### Sync gradle settings.


#### Get the visitor identifier

Retrieve the visitor identifier using Public API key. You can find your [Public API key](https://dev.fingerprint.com/docs) in your [dashboard](https://dashboard.fingerprint.com/subscriptions/).

##### Kotlin example
```kotlin
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FingerprintJSFactory
...

// Initialization
val factory = FingerprintJSFactory(applicationContext)
val configuration = Configuration(
    apiKey = "your-public-api-key"
  )
 
val fpjsClient = factory.createInstance(
    configuration
)

// Usage
fpjsClient.getVisitorId { visitorIdResponse ->
    val visitorId = visitorIdResponse.visitorId
    // Use the ID
}

```
##### Java example

```java
import com.fingerprintjs.android.fpjs_pro.Configuration;
import com.fingerprintjs.android.fpjs_pro.FingerprintJS;
import com.fingerprintjs.android.fpjs_pro.FingerprintJSFactory;
...

FingerprintJSFactory factory = new FingerprintJSFactory(this.getApplicationContext());
Configuration configuration = new Configuration(
    "your-public-api-key"
    );

FingerprintJS fpjsClient = factory.createInstance(
    configuration
);

fpjsClient.getVisitorId(visitorIdResponse -> {
    // Use the ID
    String visitorId = visitorIdResponse.getVisitorId();
    return null;
});
```

## Privacy notes

When publishing to the Play Market make sure you've noted the following information about collected data:
| Data Types | Collected | Shared | Processed ephemerally | Required or Optional | Purposes |
| --- | --- | --- | --- | --- | --- |
| User IDs | Yes | No | No | Required | Fraud Prevention |
| Device or other IDs | Yes | No | No | Required | Fraud Prevention |


## Additional Resources
- [Full API reference](docs/client_api.md).
- [Server-to-Server API](https://dev.fingerprint.com/docs/server-api)
- [Fingerprint Pro documentation](https://dev.fingerprint.com/docs)
