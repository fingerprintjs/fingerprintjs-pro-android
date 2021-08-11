package com.fingerprintjs.android.fpjs_pro


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.webkit.*
import com.fingerprintjs.android.fingerprint.Configuration
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.fingerprint.tools.executeSafe
import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean


interface FPJSProClient {
    fun getVisitorId(listener: (String) -> Unit)
}

class FPJSProFactory(
    private val applicationContext: Context
) {

    private var instance: FPJSProClient? = null

    fun getInstance(
        endpointUrl: String,
        apiToken: String
    ): FPJSProClient {
        if (instance == null) {
            synchronized(FPJSProFactory::class.java) {
                if (instance == null) {
                    instance = FPJSProClientImpl(applicationContext, endpointUrl, apiToken)
                }
            }
        }

        return instance!!
    }
}


@SuppressLint("SetJavaScriptEnabled", "InflateParams")
class FPJSProClientImpl(
    applicationContext: Context,
    endpointUrl: String,
    apiToken: String
) : FPJSProClient {

    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var webViewWeakRef: WeakReference<WebView>

    private val visitorIdIsReady = AtomicBoolean(false)
    private var visitorId: String = ""

    private var client = object : WebChromeClient() {

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            Log.d(this.javaClass.simpleName, consoleMessage.toString())
            return super.onConsoleMessage(consoleMessage)
        }

        override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            visitorId = message ?: "error"
            visitorIdIsReady.set(true)
            return true
        }
    }

    init {
        executeSafe({
            val fingerprinter =
                FingerprinterFactory.getInstance(applicationContext, Configuration(3))
            val inflater = LayoutInflater.from(applicationContext)
            val webView = inflater.inflate(R.layout.empty_webview, null) as WebView
            webViewWeakRef = WeakReference(webView)
            webViewWeakRef.get()?.webChromeClient = client
            webViewWeakRef.get()?.settings?.javaScriptEnabled = true
            webViewWeakRef.get()
                ?.addJavascriptInterface(
                    FPJSProInterface(fingerprinter, endpointUrl, apiToken),
                    JS_NAMESPACE
                )
            webViewWeakRef.get()?.loadUrl(FPJS_ASSET_URL)
        }, null)

    }

    override fun getVisitorId(listener: (String) -> (Unit)) {
        executor.execute {
            waitForVisitorId(listener)
        }
    }

    private fun waitForVisitorId(listener: (String) -> (Unit)) {
        executeSafe({
            if (visitorIdIsReady.get()) {
                listener.invoke(visitorId)
            } else {
                Thread.sleep(200)
                getVisitorId(listener)
            }
        }, null)
    }
}

private const val JS_NAMESPACE = "fingerprint-android"
private const val FPJS_ASSET_URL = "file:///android_asset/index.html"
