<p align="center">
  <a href="https://fingerprintjs.com">
    <img src="https://user-images.githubusercontent.com/10922372/129346814-a4e95dbf-cd27-49aa-ae7c-f23dae63b792.png" alt="FingerprintJS" width="312px" />
  </a>
</p>
<p align="center">
  <a href="https://discord.gg/39EpE2neBg">
    <img src="https://img.shields.io/discord/852099967190433792?style=logo&label=Discord&logo=Discord&logoColor=white" alt="Discord server">
  </a>
</p>

# FingerprintJS Pro Android Integrations


An example app and packages demonstrating [FingerprintJS Pro](https://fingerprintjs.com/) capabilities on the Android platform. The repository illustrates how to retrieve a FingerprintJS Pro visitor identifier in a native mobile app. These integrations communicate with the FingerprintJS Pro API and require [browser token](https://dev.fingerprintjs.com/docs). For local client-side Android fingerprinting take a look at [fingerprint-android](https://github.com/fingerprintjs/fingerprint-android) repository instead. If you are interested in the iOS platform, you can also check our [FingerprintJS Pro iOS integrations](https://github.com/fingerprintjs/fingerprintjs-pro-ios-webview).

There are two typical use cases:
- Using our native library to retrieve a FingerprintJS Pro visitor identifier in the native code OR
- Retrieving visitor identifier using our native library in combination with signals from the FingerprintJS Pro browser agent in the webview on the JavaScript level.

## Using the external library to retrieve a FingerprintJS Pro visitor identifier
This integration approach uses the external library [fingerprint-android](https://github.com/fingerprintjs/fingerprint-android). It collects various signals from the Android system, sends them to the FingerprintJS Pro API for processing, and retrieves an accurate visitor identifier.

*Note: The library depends on [kotlin-stdlib](https://kotlinlang.org/api/latest/jvm/stdlib/). If your application is written in Java, add `kotlin-stdlib` dependency first (it's lightweight and has excellent backward and forward compatibility).*


### 1. Add repository

Add these lines to your `build.gradle`.


```gradle
allprojects {	
  repositories {
  ...
  maven { url 'https://jitpack.io' }	
}}
```


### 2. Add a dependency to your `build.gradle` file

```gradle
dependencies {
  implementation 'com.github.fingerprintjs:fingerprintjs-pro-android-integrations:1.0.0'

  // If you use Java for you project, add also this line
  implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"}
```

When using Kotlin, also make sure you have specified Kotlin version in your `build.gradle` file:
```gradle
buildscript {
    ext.kotlin_version= 'your-kotlin-version'
    ...
```
*Note: You can find your Kotlin version in Android Studio > File > Settings > Languages & Frameworks > Kotlin.*

### 3. Get the visitor identifier

Retrieve the visitor identifier using browser token. You can find your [browser token](https://dev.fingerprintjs.com/docs) in your [dashboard](https://dashboard.fingerprintjs.com/subscriptions/).


#### 3.1 Kotlin example

```kotlin
// Initialization
val factory = FPJSProFactory(applicationContext)
val configuration(
    apiToken = "BROWSER_TOKEN",
    region = Region.US, // optional
    endpointUrl = "https://endpoint.url" // optional
  )
 
val fpjsClient = factory.create(
    configuration
)

// Usage
fpjsClient.getVisitorId { visitorId ->
    // Use visitorId
}
```


#### 3.2 Java example

```java

FPJSProFactory factory = new FPJSProFactory(this.getApplicationContext());
Configuration configuration = new Configuration("BROWSER_TOKEN", Configuration.Region.US, Configuration.Region.US.getEndpointUrl()); 
// Or you can choose Region.EU

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

*❗ Important: Due to WebView limitations the initialization of the client is performed on the UI-thread, consider call `getVisitorId()` while the screen is static.*


## Using inside a webview with JavaScript

This approach uses signals from [FingerprintJS Pro browser agent](https://dev.fingerprintjs.com/docs/quick-start-guide#js-agent) together with signals provided by [fingerprint-android](https://github.com/fingerprintjs/fingerprint-android). The identifier collected by [fingerprint-android](https://github.com/fingerprintjs/fingerprint-android) is added to the [`tag` field](https://dev.fingerprintjs.com/docs#tagging-your-requests) in the given format. FingerprintJS Pro browser agent adds an additional set of signals and sents them to the FingerprintJS Pro API. Eventually, the API returns accurate visitor identifier.

### 1. Add a JavaScript interface to your webview

```kotlin

// Init interface
val factory = FPJSProFactory(webview.context.applicationContext)
val interface = FPJSProFactory.createInterface()

// Add it to the webview
webview.addJavascriptInterface(
                interface,
                "fpjs-pro-android"
            )
```

### 2. Setup the JavaScript FingerprintJS PRO integration in your webview

```js
function initFingerprintJS() {
    // Initialize an agent at application startup
    const fpPromise = FingerprintJS.load({
      token: 'your-browser-token',
      endpoint: 'your-endpoint', // optional
      region: 'your-region' // optional
    });
    
    var androidDeviceId = window['fpjs-pro-android'].getDeviceId();


    // Get the visitor identifier when you need it
    fpPromise
      .then(fp => fp.get({
       environment: {
        deviceId: androidDeviceId,
        type: 'android',
       }
      }))
      .then(result => console.log(result.visitorId));
  }
```
You can find your [browser token](https://dev.fingerprintjs.com/docs) in your [dashboard](https://dashboard.fingerprintjs.com/subscriptions/).

## Additional Resources
[FingerprintJS Pro documentation](https://dev.fingerprintjs.com/docs)

## License
This library is MIT licensed.
