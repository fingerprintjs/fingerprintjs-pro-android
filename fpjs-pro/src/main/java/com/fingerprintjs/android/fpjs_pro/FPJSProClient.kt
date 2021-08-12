package com.fingerprintjs.android.fpjs_pro


import android.content.Context
import com.fingerprintjs.android.fingerprint.FingerprinterFactory


interface FPJSProClient {
    fun getVisitorId(listener: (String) -> Unit)
}

class Configuration(
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
        val fingerprinter = FingerprinterFactory.getInstance(
            applicationContext,
            com.fingerprintjs.android.fingerprint.Configuration(3)
        )

        return FPJSProInterface(fingerprinter, configuration.apiToken, configuration.endpointUrl)
    }
}
