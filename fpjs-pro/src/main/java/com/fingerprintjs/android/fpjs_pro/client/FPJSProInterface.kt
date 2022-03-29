package com.fingerprintjs.android.fpjs_pro.client


import android.webkit.JavascriptInterface
import com.fingerprintjs.android.fpjs_pro.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.MediaDrmIdProvider
import org.json.JSONException
import org.json.JSONObject


class FPJSProInterface internal constructor(
    private val apiToken: String,
    private val endpointUrl: String,
    private val androidIdProvider: AndroidIdProvider,
    private val gsfIdProvider: GsfIdProvider,
    private val mediaDrmIdProvider: MediaDrmIdProvider,
    private val tags: Map<String, Any> = emptyMap()
) {
    @JavascriptInterface
    fun getDeviceId(): String = nativeDeviceId()

    @JavascriptInterface
    fun getEndpoint(): String = endpointUrl

    @JavascriptInterface
    fun getApiToken(): String = apiToken

    @JavascriptInterface
    fun getTags(): String {
        if (tags.isEmpty()) return "{}"
        return try {
            JSONObject(tags).toString()
        } catch (e: JSONException) {
            "{}"
        }
    }

    private fun nativeDeviceId() =
        gsfIdProvider.getGsfAndroidId() ?: mediaDrmIdProvider.getMediaDrmId()
        ?: androidIdProvider.getAndroidId()
}