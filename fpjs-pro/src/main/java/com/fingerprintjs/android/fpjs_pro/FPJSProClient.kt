package com.fingerprintjs.android.fpjs_pro


import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.webkit.ConsoleMessage
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.fingerprintjs.android.fingerprint.Configuration
import com.fingerprintjs.android.fingerprint.FingerprinterFactory
import com.fingerprintjs.android.fingerprint.tools.executeSafe
import java.lang.ref.WeakReference


interface FPJSProClient {
    fun getVisitorId(listener: (String) -> Unit)
}

class FPJSProFactory(
    private val applicationContext: Context
) {
    fun create(
        endpointUrl: String,
        apiToken: String
    ): FPJSProClient {
        return FPJSProClientImpl(applicationContext, endpointUrl, apiToken)
    }
}


@SuppressLint("SetJavaScriptEnabled", "InflateParams")
class FPJSProClientImpl(
    private val applicationContext: Context,
    private val endpointUrl: String,
    private val apiToken: String
) : FPJSProClient {

    private lateinit var webViewWeakRef: WeakReference<WebView>

    override fun getVisitorId(listener: (String) -> (Unit)) {
        Handler(applicationContext.mainLooper).post {
            runFPJS(listener)
        }
    }

    private fun runFPJS(listener: (String) -> (Unit)) {
        executeSafe({
            val fingerprinter =
                FingerprinterFactory.getInstance(applicationContext, Configuration(3))
            val inflater = LayoutInflater.from(applicationContext)
            val webView = inflater.inflate(R.layout.empty_webview, null) as WebView
            webViewWeakRef = WeakReference(webView)
            webViewWeakRef.get()?.webChromeClient = object : WebChromeClient() {

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
                    listener.invoke(message ?: "error")
                    webViewWeakRef.get()?.apply {
                        removeJavascriptInterface(JS_INTERFACE_NAME)
                        webChromeClient = null
                        removeAllViews()
                    }

                    webViewWeakRef.clear()
                    return true
                }
            }

            webViewWeakRef.get()?.settings?.javaScriptEnabled = true
            webViewWeakRef.get()
                ?.addJavascriptInterface(
                    FPJSProInterface(fingerprinter, endpointUrl, apiToken),
                    JS_INTERFACE_NAME
                )

            webViewWeakRef.get()?.loadUrl(FPJS_ASSET_URL)
        }, null)
    }

}

private const val JS_INTERFACE_NAME = "fingerprint-android"
private const val FPJS_ASSET_URL = "file:///android_asset/index.html"
