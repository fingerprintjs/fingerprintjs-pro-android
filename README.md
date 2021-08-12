# fingerprintjs-pro-android-webview

## Quick start

### Add repository

Add these lines to your `build.gradle`.


```gradle
allprojects {	
  repositories {
  ...
  maven { url 'https://jitpack.io' }	
}}
```

### Add dependency

Add these lines to `build.gradle` of a module.

This library depends on [kotlin-stdlib](https://kotlinlang.org/api/latest/jvm/stdlib/).

If your application is written in Java, add `kotlin-stdlib` dependency first (it's lightweight and has excellent backward and forward compatibility).

```gradle
dependencies {
  // Add this line only if you use this library with Java
  implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"

  implementation "com.github.fingerprintjs:fingerprintjs-pro-android-webview-1.0"
}


```


## Usage

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

Note: the call is performed on UI-thread, consider use it while screen is static.