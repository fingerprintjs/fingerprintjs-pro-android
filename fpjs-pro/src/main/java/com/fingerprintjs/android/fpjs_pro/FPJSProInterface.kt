package com.fingerprintjs.android.fpjs_pro


import android.webkit.JavascriptInterface
import com.fingerprintjs.android.fingerprint.Fingerprinter
import java.util.concurrent.CountDownLatch


class FPJSProInterface internal constructor(
    private val fingerprinter: Fingerprinter,
    private val apiToken: String,
    private val endpointUrl: String,
) {
    @JavascriptInterface
    fun getDeviceId(): String {
        val countDownLatch = CountDownLatch(1)
        var result = ""
        fingerprinter.getDeviceId {
            result = it.deviceId
            countDownLatch.countDown()
        }
        countDownLatch.await()
        return result
    }

    @JavascriptInterface
    fun getEndpoint(): String = endpointUrl

    @JavascriptInterface
    fun getApiToken(): String = apiToken
}