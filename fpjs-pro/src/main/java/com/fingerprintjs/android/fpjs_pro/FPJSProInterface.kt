package com.fingerprintjs.android.fpjs_pro


import android.webkit.JavascriptInterface
import com.fingerprintjs.android.fpjs_pro.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.MediaDrmIdProvider
import com.fingerprintjs.android.fpjs_pro.tools.executeSafe


class FPJSProInterface internal constructor(
    private val apiToken: String,
    private val endpointUrl: String,
    private val androidIdProvider: AndroidIdProvider,
    private val gsfIdProvider: GsfIdProvider,
    private val mediaDrmIdProvider: MediaDrmIdProvider
) {
    @JavascriptInterface
    fun getDeviceId(): String = nativeDeviceId()

    @JavascriptInterface
    fun getEndpoint(): String = endpointUrl

    @JavascriptInterface
    fun getApiToken(): String = apiToken

    private fun nativeDeviceId(): String {
        val mediaDrmId = mediaDrmIdProvider.getMediaDrmId()
        val gsfId = gsfIdProvider.getGsfAndroidId()
        val androidId = androidIdProvider.getAndroidId()

        return gsfId ?: mediaDrmId ?: androidId
    }
}