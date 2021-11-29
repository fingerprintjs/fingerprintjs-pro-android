package com.fingerprintjs.android.fpjs_pro


import android.content.Context
import com.fingerprintjs.android.fpjs_pro.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.MediaDrmIdProvider
import com.fingerprintjs.android.fpjs_pro.kotlin_client.FPJSProKotlinClient
import com.fingerprintjs.android.fpjs_pro.kotlin_client.FetchVisitorIdInteractor
import com.fingerprintjs.android.fpjs_pro.kotlin_client.FetchVisitorIdInteractorImpl
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.HttpClient
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.NativeHttpClient
import com.fingerprintjs.android.fpjs_pro.web_view_client.FPJSProClientImpl
import com.fingerprintjs.android.fpjs_pro.web_view_client.FPJSProInterface


interface FPJSProClient {
    fun getVisitorId(listener: (String) -> Unit)
    fun getVisitorId(listener: (String) -> Unit, errorListener: (String) -> (Unit))
    fun getVisitorId(
        tags: Map<String, Any>,
        listener: (String) -> Unit,
        errorListener: (String) -> (Unit)
    )
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

    @JvmOverloads
    fun createInstance(
        configuration: Configuration,
        webViewClient: Boolean = false
    ): FPJSProClient {
        return if (webViewClient) {
            FPJSProClientImpl(
                applicationContext,
                configuration.apiToken,
                configuration.endpointUrl
            )
        } else {
            FPJSProKotlinClient(
                createApiInteractor(
                    configuration.apiToken,
                    configuration.endpointUrl
                )
            )
        }
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

    private fun createApiInteractor(
        endpointUrl: String,
        authToken: String
    ): FetchVisitorIdInteractor {
        val contentResolver = applicationContext.contentResolver!!
        val androidIdProvider = AndroidIdProvider(contentResolver)
        val gsfIdProvider = GsfIdProvider(contentResolver)
        val mediaDrmIdProvider = MediaDrmIdProvider()

        return FetchVisitorIdInteractorImpl(
            androidIdProvider,
            gsfIdProvider,
            mediaDrmIdProvider,
            createHttpClient(),
            endpointUrl,
            authToken,
            BuildConfig.VERSION_NAME,
            getAppName()
        )
    }

    private fun createHttpClient(): HttpClient {
        return NativeHttpClient()
    }

    private fun getAppName(): String {
        val applicationInfo = applicationContext.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else applicationContext.getString(
            stringId
        )
    }
}
