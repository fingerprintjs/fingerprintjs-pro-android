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


@SuppressLint("SetJavaScriptEnabled", "InflateParams")
class FPJSProClientImpl(
    private val applicationContext: Context,
    private val apiToken: String,
    private val endpointUrl: String
) : FPJSProClient {

    private lateinit var webViewWeakRef: WeakReference<WebView>

    override fun getVisitorId(listener: (String) -> (Unit)) {
        runOnUiThread { drawWebView() }
        runOnUiThread(DELAY) { init(listener) }
        runOnUiThread(2 * DELAY) { executeFPJS() }
    }

    private fun init(listener: (String) -> (Unit)) {
        val fingerprinter =
            FingerprinterFactory.getInstance(applicationContext, Configuration(3))

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
                listener.invoke(message ?: "")
                clearWebView()
                return true
            }
        }

        webViewWeakRef.get()?.settings?.javaScriptEnabled = true
        webViewWeakRef.get()
            ?.addJavascriptInterface(
                FPJSProInterface(fingerprinter, apiToken, endpointUrl),
                JS_INTERFACE_NAME
            )
    }

    private fun drawWebView() {
        val inflater = LayoutInflater.from(applicationContext)
        val webView = inflater.inflate(R.layout.empty_webview, null) as WebView
        webViewWeakRef = WeakReference(webView)
    }

    private fun executeFPJS() {
        executeSafe({
            webViewWeakRef.get()?.apply {
                loadUrl(FPJS_ASSET_URL)
            }
        }, null)
    }

    private fun runOnUiThread(delayInMillis: Long = 0L, action: () -> (Unit)) {
        Handler(applicationContext.mainLooper).postDelayed({
            action.invoke()
        }, delayInMillis)
    }

    private fun clearWebView() {
        webViewWeakRef.get()?.apply {
            removeJavascriptInterface(JS_INTERFACE_NAME)
            webChromeClient = null
            destroy()
        }

        webViewWeakRef.clear()
    }
}

private const val JS_INTERFACE_NAME = "fpjs-pro-android"
private const val FPJS_ASSET_URL = "file:///android_asset/index.html"
private const val DELAY = 50L
