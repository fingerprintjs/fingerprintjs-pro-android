package com.fingerprintjs.android.fpjs_pro.web_view_client


import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.webkit.ConsoleMessage
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.fingerprintjs.android.fpjs_pro.FPJSProClient
import com.fingerprintjs.android.fpjs_pro.R
import com.fingerprintjs.android.fpjs_pro.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.MediaDrmIdProvider
import com.fingerprintjs.android.fpjs_pro.tools.executeSafe
import java.lang.ref.WeakReference
import java.util.concurrent.Executors


@SuppressLint("SetJavaScriptEnabled", "InflateParams")
class FPJSProClientImpl(
    private val applicationContext: Context,
    private val apiToken: String,
    private val endpointUrl: String
) : FPJSProClient {

    private lateinit var webViewWeakRef: WeakReference<WebView>
    private val executor = Executors.newSingleThreadExecutor()

    override fun getVisitorId(listener: (String) -> (Unit)) {
        getVisitorId(emptyMap(), listener, {})
    }

    override fun getVisitorId(listener: (String) -> Unit, errorListener: (String) -> Unit) {
        getVisitorId(emptyMap(), listener, errorListener)
    }

    override fun getVisitorId(
        tags: Map<String, Any>,
        listener: (String) -> Unit,
        errorListener: (String) -> Unit
    ) {
        runOnUiThread {
            drawWebView()
            init(tags, listener, errorListener)
            executeFPJS()
        }
    }

    private fun init(
        tags: Map<String, Any>,
        listener: (String) -> Unit,
        errorListener: (String) -> Unit
    ) {
        val contentResolver = applicationContext.contentResolver!!
        val androidIdProvider = AndroidIdProvider(contentResolver)
        val gsfIdProvider = GsfIdProvider(contentResolver)
        val mediaDrmIdProvider = MediaDrmIdProvider()

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
                executor.execute {
                    if (message.isNullOrEmpty()) {
                        errorListener.invoke("Unknown error")
                        return@execute
                    }

                    if (message.contains(ERROR_SIGNATURE)) {
                        val errorMsg = message.replace(ERROR_SIGNATURE, "")
                        errorListener.invoke(errorMsg)
                        return@execute
                    }
                    listener.invoke(message)
                }
                runOnUiThread {
                    clearWebView()
                }
                return true
            }
        }

        webViewWeakRef.get()?.settings?.javaScriptEnabled = true
        webViewWeakRef.get()?.settings?.defaultTextEncodingName = "utf-8"
        webViewWeakRef.get()
            ?.addJavascriptInterface(
                FPJSProInterface(
                    apiToken, endpointUrl,
                    androidIdProvider,
                    gsfIdProvider,
                    mediaDrmIdProvider,
                    tags
                ),
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
private const val ERROR_SIGNATURE = "FPJS-Pro-error:"