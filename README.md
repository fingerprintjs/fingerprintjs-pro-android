# [FingerprintJS Pro](https://fingerprintjs.com/) Android Webview Integration

An example usage of FingerprintJS Pro inside a webview. The repo illustrates how to retrieve a FPJS visitor identifier in a mobile app.

## Usage example

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


## Installing as an external library

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

## Re-using an existing webview

TBD

## JavaScript without native code

TBD
