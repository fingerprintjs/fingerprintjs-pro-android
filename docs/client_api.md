# FingerprintJS Pro Android Clien API


## Full public API

```kotlin

interface FPJSProClient {
    fun getVisitorId(listener: (String) -> Unit)
    fun getVisitorId(listener: (String) -> Unit, errorListener: (String) -> (Unit))
    fun getVisitorId(tags: Map<String, Any>, listener: (String) -> Unit, errorListener: (String) -> (Unit))
}

```

### Error handling

```kotlin
fpjsClient.getVisitorId(
          listener = { visitorId ->
            // Handle ID
          },
          errorListener = { error ->
            // Handle error
          })
```

### [Tags](https://dev.fingerprintjs.com/v2/docs/js-agent#tag) support

```kotlin
 fpjsClient.getVisitorId(
      tags = mapOf("sessionId" to sessionId),
      listener = { visitorId ->
          // Handle ID
      },
      errorListener = { error ->
          // Handle error
      })
```



## Using inside a webview with JavaScript

This approach uses signals from [FingerprintJS Pro browser agent](https://dev.fingerprintjs.com/docs/quick-start-guide#js-agent) together with signals provided by [fingerprint-android](https://github.com/fingerprintjs/fingerprint-android). The identifier collected by [fingerprint-android](https://github.com/fingerprintjs/fingerprint-android) is added to the [`tag` field](https://dev.fingerprintjs.com/docs#tagging-your-requests) in the given format. FingerprintJS Pro browser agent adds an additional set of signals and sends them to the FingerprintJS Pro API. Eventually, the API returns an accurate visitor identifier.

#### Add a JavaScript interface to your webview

##### Kotlin example

```kotlin
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory
...

val myWebView: WebView = findViewById(R.id.webview)

// Init interface
val factory = FPJSProFactory(myWebView.context.applicationContext)
val configuration = Configuration(
    apiToken = "BROWSER_TOKEN",
    region = Configuration.Region.US, // optional
    endpointUrl = "https://endpoint.url" // optional
)
val fpjsInterface = factory.createInterface(configuration)

// Add interface to the webview
myWebView.addJavascriptInterface(
    fpjsInterface,
    "fpjs-pro-android"
)

// Use embedded webview in the app instead of the default new app
myWebView.setWebViewClient(WebViewClient())

// Enable javascript inside the webview
val webSettings: WebSettings = myWebView.getSettings()
webSettings.javaScriptEnabled = true

// Load url with the injected and configured FingerprintJS Pro agent
myWebView.loadUrl("https://site-with-injected-agent.com")
```

##### Java example
```java
import com.fingerprintjs.android.fpjs_pro.Configuration;
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory;
import com.fingerprintjs.android.fpjs_pro.FPJSProInterface;
...

WebView myWebView = findViewById(R.id.webview);

// Init interface
FPJSProFactory factory = new FPJSProFactory(this.getApplicationContext());
Configuration configuration = new Configuration(
  "BROWSER_TOKEN"
  );

FPJSProInterface fpjsInterface = factory.createInterface(configuration);

// Add interface to the webview
myWebView.addJavascriptInterface(
    fpjsInterface,
    "fpjs-pro-android"
    );

// Use embedded webview in the app instead of the default new app
myWebView.setWebViewClient(new WebViewClient());

// Enable javascript inside the webview
WebSettings webSettings = myWebView.getSettings();
webSettings.setJavaScriptEnabled(true);

// Load url with the injected and configured FingerprintJS Pro agent
myWebView.loadUrl("https://site-with-injected-agent.com");
```

### Setup the JavaScript FingerprintJS Pro integration in your webview

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
