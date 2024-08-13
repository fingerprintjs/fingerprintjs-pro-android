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
  <a href="https://github.com/fingerprintjs/fingerprint-device-intelligence-android-demo">
    <img src="https://img.shields.io/badge/Fingerprint%20Pro%20Demo%20App-red" alt="Fingerprint Pro Demo App">
  </a>
</p>


# Fingerprint Identification SDK for Android

[Fingerprint’s Device Intelligence platform for Android](https://dev.fingerprint.com/docs/android-sdk) helps you to accurately identify the devices on which your mobile app is being used. The platform also provides high-quality [Smart Signals](https://dev.fingerprint.com/docs/smart-signals-overview#smart-signals-for-mobile-devices) that will help you identify risky transactions before they happen.


> [!TIP]
> Check out the [Fingerprint Pro Demo App](https://github.com/fingerprintjs/fingerprint-device-intelligence-android-demo) to better understand and experience the capabilities of our device intelligence platform.

For local Android fingerprinting and identifying without making requests to API take a look at the [fingerprintjs-android](https://github.com/fingerprintjs/fingerprintjs-android) repository.

If you are interested in the iOS platform, you can also check our [Fingerprint Pro iOS](https://github.com/fingerprintjs/fingerprintjs-pro-ios).


## Quick start

### Requirements

The Android SDK requires Android 5.0 (API level 21+) or higher.

### Installation steps
#### 1. Add the repository to the gradle

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
#### 2. Add the dependency to your `build.gradle` file

```gradle
dependencies {
    ...
  implementation "com.fingerprint.android:pro:2.4.0"
}
```

Note: Fingerprint PRO Android uses [FingerprintJS Android](https://github.com/fingerprintjs/fingerprintjs-android), [kotlin-stdlib](https://kotlinlang.org/api/latest/jvm/stdlib/) and [androidx.core:core](https://developer.android.com/jetpack/androidx/releases/core) as dependencies. Additionally, [com.google.android.gms:play-services-location](https://developers.google.com/android/reference/com/google/android/gms/location/package-summary) is required when [Geolocation Spoofing Detection Smart Signal](https://dev.fingerprint.com/docs/smart-signals-overview#geolocation-spoofing-detection) functionality is used.

#### 3. Sync gradle settings


#### 4. Get the visitor identifier

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
## Region and Domain configuration

It is possible to manually select an endpoint from a predefined set of regions. The library uses the `Region.US` region by default. The list of existing regions can be found in our [developer documentation](https://dev.fingerprint.com/docs/regions).

Besides selecting a region from the predefined set, it's possible to point the library to a custom endpoint that has the correct API interface with the `endpointUrl` field value. If the endpoint isn't a valid URL, the library throws a specific error during API calls.

Note: API keys are region-specific so make sure you have selected the correct region during initialization. 

### Selecting a Region

```kotlin
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FingerprintJSFactory
...

// Initialize and configure the SDK
val factory = FingerprintJSFactory(applicationContext)
val configuration = Configuration(
  apiKey = "YOUR_PUBLIC_API_KEY",
  // The 'region' must match the region associated with your API key, in this example it's EU
  region = Configuration.Region.EU
)
 
val fpjsClient = factory.createInstance(
  configuration
)

// Get a 'visitorId'
fpjsClient.getVisitorId { visitorIdResponse ->
  val visitorId = visitorIdResponse.visitorId
  // Use the visitorId
}
```
### Using Custom Endpoint Domain

```kotlin
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FingerprintJSFactory
...

// Initialize and configure the SDK
val factory = FingerprintJSFactory(applicationContext)
val configuration = Configuration(
  apiKey = "YOUR_PUBLIC_API_KEY",
  // For custom subdomain or proxy integration, use this option to specify a custom endpoint. If a region parameter is set – it will be ignored.
  endpointUrl = "custom-endpoint-URL",
)
 
val fpjsClient = factory.createInstance(
  configuration
)

// Get a 'visitorId'
fpjsClient.getVisitorId { visitorIdResponse ->
  val visitorId = visitorIdResponse.visitorId
  // Use the visitorId
}
```

## Default and Extended Response Formats

The backend can return either a default or an extended response. Extended response contains more metadata that further explain the fingerprinting process. Both default and extended responses are captured in the `FingerprintJSProResponse` object. To set up the format use the `extendedResponseFormat` parameter of the `Configuration` class.

```kotlin
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FingerprintJSFactory
...

// Initialize and configure the SDK
val factory = FingerprintJSFactory(applicationContext)
val configuration = Configuration(
  apiKey = "YOUR_PUBLIC_API_KEY",
  endpointUrl = "custom-endpoint-URL",
  // Use this to get the extended response, explained below
  extendedResponseFormat = false
)
 
val fpjsClient = factory.createInstance(
  configuration
)

// Get a 'visitorId'
fpjsClient.getVisitorId { visitorIdResponse ->
  val visitorId = visitorIdResponse.visitorId
  // Use the visitorId
}
```

### Default Response

```kotlin
data class FingerprintJSProResponse(
    val requestId: String,
    val visitorId: String,
    val confidenceScore: ConfidenceScore,
    val errorMessage: String? = null
)
```

### Extended Result

```kotlin
data class FingerprintJSProResponse(
    val requestId: String,
    val visitorId: String,
    val confidenceScore: ConfidenceScore,
    val visitorFound: Boolean, // Available with extendedResponseFormat == true
    val ipAddress: String, // Available with extendedResponseFormat == true
    val ipLocation: IpLocation?, // Available with extendedResponseFormat == true
    val osName: String, // Available with extendedResponseFormat == true
    val osVersion: String, // Available with extendedResponseFormat == true
    val firstSeenAt: Timestamp,// Available with extendedResponseFormat == true
    val lastSeenAt: Timestamp, // Available with extendedResponseFormat == true
    val errorMessage: String? = null
)
```

## Errors

The library parses backend errors and introduces its own error sealed class called `Error`.

```kotlin
sealed class Error(
  // The request ID of the identification request
  val requestId: String
  // A self-explanatory description of the error
  val description: String?
)
```

It's a sealed class, which can be one of:

- ApiKeyRequired
- ApiKeyNotFound
- ApiKeyExpired
- RequestCannotBeParsed
- Failed
- RequestTimeout
- TooManyRequest
- OriginNotAvailable
- HeaderRestricted
- PackageNotAuthorized
- WrongRegion
- SubscriptionNotActive
- UnsupportedVersion
- ResponseCannotBeParsed
- NetworkError

## Privacy notes

When publishing to the Play Market make sure you've noted the following information about collected data:
| Data Types | Collected | Shared | Processed ephemerally | Required or Optional | Purposes |
| --- | --- | --- | --- | --- | --- |
| User IDs | Yes | No | No | Required | Fraud Prevention |
| Device or other IDs | Yes | No | No | Required | Fraud Prevention |


## Additional Resources
- [Full API reference](docs/client_api.md)
- [Server-to-Server API](https://dev.fingerprint.com/docs/server-api)
- [Fingerprint Pro documentation](https://dev.fingerprint.com/docs)
