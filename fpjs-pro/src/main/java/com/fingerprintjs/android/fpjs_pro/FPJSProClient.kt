package com.fingerprintjs.android.fpjs_pro


import android.content.Context
import com.fingerprintjs.android.fpjs_pro.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.MediaDrmIdProvider


interface FPJSProClient {
    fun getVisitorId(listener: (String) -> Unit)
    fun getVisitorId(listener: (String) -> Unit, errorListener: (String) -> (Unit))
    fun getVisitorId(tags: Map<String, Any>, listener: (String) -> Unit, errorListener: (String) -> (Unit))
}

class Configuration @JvmOverloads constructor(
    val apiToken: String,
    val region: Region = Region.US,
    val endpointUrl: String = region.endpointUrl
) {
    enum class Region(val endpointUrl: String) {
        US("https://api.fpjs.io"),
        EU("https://eu.api.fpjs.io")
    }
}

class FPJSProFactory(
    private val applicationContext: Context
) {
    fun createInstance(
        configuration: Configuration
    ): FPJSProClient {
        return FPJSProClientImpl(
            applicationContext,
            configuration.apiToken,
            configuration.endpointUrl
        )
    }

    fun createInterface(configuration: Configuration = Configuration("")): FPJSProInterface {
        val contentResolver = applicationContext.contentResolver!!
        val androidIdProvider = AndroidIdProvider(contentResolver)
        val gsfIdProvider = GsfIdProvider(contentResolver)
        val mediaDrmIdProvider = MediaDrmIdProvider()
        return FPJSProInterface(
            configuration.apiToken,
            configuration.endpointUrl,
            androidIdProvider,
            gsfIdProvider,
            mediaDrmIdProvider
        )
    }
}
