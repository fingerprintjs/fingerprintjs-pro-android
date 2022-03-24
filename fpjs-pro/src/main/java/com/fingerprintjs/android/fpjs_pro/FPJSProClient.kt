package com.fingerprintjs.android.fpjs_pro


import android.content.Context
import com.fingerprintjs.android.fpjs_pro.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.MediaDrmIdProvider
import com.fingerprintjs.android.fpjs_pro.transport.FPJSProKotlinImpl
import com.fingerprintjs.android.fpjs_pro.transport.FetchVisitorIdInteractor
import com.fingerprintjs.android.fpjs_pro.transport.FetchVisitorIdInteractorImpl
import com.fingerprintjs.android.fpjs_pro.transport.fetch_visitor_id_request.FetchVisitorIdResponse
import com.fingerprintjs.android.fpjs_pro.transport.http_client.HttpClient
import com.fingerprintjs.android.fpjs_pro.transport.http_client.NativeHttpClient
import com.fingerprintjs.android.fpjs_pro.logger.ConsoleLogger
import com.fingerprintjs.android.fpjs_pro.web_view_client.FPJSProInterface


interface FPJSProClient {
    fun getVisitorId(listener: (FetchVisitorIdResponse) -> Unit)
    fun getVisitorId(listener: (FetchVisitorIdResponse) -> Unit, errorListener: (String) -> (Unit))
    fun getVisitorId(
        tags: Map<String, Any>,
        listener: (FetchVisitorIdResponse) -> Unit,
        errorListener: (String) -> (Unit)
    )
}

class Configuration @JvmOverloads constructor(
    val apiToken: String,
    val region: Region = Region.US,
    val endpointUrl: String = region.endpointUrl,
    val extendedResponseFormat: Boolean = false
) {
    enum class Region(val endpointUrl: String) {
        US("https://api.fpjs.io"),
        EU("https://eu.api.fpjs.io"),
        AP("https://ap.api.fpjs.io")
    }
}

class FPJSProFactory(
    private val applicationContext: Context
) {

    private val logger = ConsoleLogger()

    fun createInstance(
        configuration: Configuration
    ): FPJSProClient {
        return FPJSProKotlinImpl(
            createApiInteractor(
                configuration.endpointUrl,
                configuration.apiToken,
                configuration.extendedResponseFormat
            ),
            logger
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

    private fun createApiInteractor(
        endpointUrl: String,
        authToken: String,
        extendedResponseFormat: Boolean
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
            getAppId(),
            extendedResponseFormat
        )
    }

    private fun createHttpClient(): HttpClient {
        return NativeHttpClient(logger)
    }

    private fun getAppId(): String {
        val applicationInfo = applicationContext.applicationInfo
        return applicationInfo.packageName
    }
}
