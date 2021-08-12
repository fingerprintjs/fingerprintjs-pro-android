# [FingerprintJS Pro](https://fingerprintjs.com/) Android Webview Integration

An example usage of FingerprintJS Pro inside a webview. The repo illustrates how to retrieve a FPJS visitor identifier in a mobile app.

## Using as an external library

Consider using this option if your mobile application is written in the native code.

The library depends on [kotlin-stdlib](https://kotlinlang.org/api/latest/jvm/stdlib/).

If your application is written in Java, add `kotlin-stdlib` dependency first (it's lightweight and has excellent backward and forward compatibility).

#### 1. Add the jitpack repository to your `build.gradle` file.

```gradle
allprojects {	
  repositories {
  ...
  maven { url 'https://jitpack.io' }	
}}
```

#### 2. Add a dependency to your `build.gradle` file.

```gradle
dependencies {
  // Add this line only if you use this library with Java
  implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"

  implementation "com.github.fingerprintjs:fingerprintjs-pro-android-webview-1.0"
}
```

#### 3. Retrieve the visitor identifier

Kotlin

```kotlin
// Initialization
val factory = FPJSProFactory(applicationContext)
 
val fpjsClient = factory.create(
    endpointUrl,
    apiToken
)

// Usage
fpjsClient.getVisitorId { visitorId ->
    // Handle visitorId
}
```

Note: the identification call is performed on the UI-thread, consider using it while the screen is static.


## Using inside a webview (JavaScript)

Consider using this option if your mobile application has a webview and you need a visitor identifier on the JavaScript level

#### 1. Create a JavaScript binding

TBD


#### 2. Setup the JavaScript FPJS SDK

```js
function initFingerprintJS() {
    // Initialize an agent at application startup.
    const fpPromise = FingerprintJS.load({
      token: 'your-browser-token',
      endpoint: 'your-endpoint', // optional
      region: 'your-region', // optional
    });

    // Get the visitor identifier when you need it.
    fpPromise
      .then(fp => fp.get({
       tag: {
        deviceId: '',
        deviceType: 'android',
       }
      }))
      .then(result => console.log(result.visitorId));
  }
```

[Read more.](https://dev.fingerprintjs.com/docs)
