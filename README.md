# [FingerprintJS Pro](https://fingerprintjs.com/) Android Webview Integration

An example usage of FingerprintJS Pro inside a webview. The repo illustrates how to retrieve a FPJS visitor identifier in a mobile app.

There are two common use cases:
1. Using an external library to retrieve a FPJS visitor identifier in the native code;
2. Retriving a FPJS visitor identifier in the webivew on the JavaScript level.

## Using as an external library

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
val configuration(apiToken = "YOUR_API_TOKEN")
 
val fpjsClient = factory.create(
    configuration
)

// Usage
fpjsClient.getVisitorId { visitorId ->
    // Handle visitorId
}
```

Note: the identification call is performed on the UI-thread, consider using it while the screen is static.


## Using inside a webview (JavaScript)

This option requires the [fingerprint-android](https://github.com/fingerprintjs/fingerprint-android) library to achive the better indetification accuracy. 

The android device identifier should be passed to the JS SDK as a `deviceId` tag.

#### 1. Add a JavaScript interface to your webview

```kotlin

// Init interface
val factory = FPJSProFactory(webview.context.applicationContext)
val configuration(apiToken = "YOUR_API_TOKEN")
val interface = FPJSProFactory.createInterface(configuration)

// Add it to your webview

webview.addJavascriptInterface(
                interface,
                "fpjs-pro-android"
            )
```



#### 2. Setup the JavaScript FPJS SDK in your webview

```js
function initFingerprintJS() {
    // Initialize an agent at application startup.
    const fpPromise = FingerprintJS.load({
      token: 'your-browser-token',
      endpoint: 'your-endpoint', // optional
      region: 'your-region' // optional
    });
    
    var androidDeviceId = window['fpjs-pro-android'].getDeviceId();


    // Get the visitor identifier when you need it.
    fpPromise
      .then(fp => fp.get({
       tag: {
        deviceId: androidDeviceId,
        deviceType: 'android',
       }
      }))
      .then(result => console.log(result.visitorId));
  }
```


[Read more.](https://dev.fingerprintjs.com/docs)
