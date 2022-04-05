package com.fingerprintjs.android.fpjs_pro


import android.content.Context
import com.fingerprintjs.android.fpjs_pro.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.MediaDrmIdProvider
import com.fingerprintjs.android.fpjs_pro.logger.ConsoleLogger
import com.fingerprintjs.android.fpjs_pro.client.FingerprintJSPro
import com.fingerprintjs.android.fpjs_pro.client.FetchVisitorIdInteractor
import com.fingerprintjs.android.fpjs_pro.client.FetchVisitorIdInteractorImpl
import com.fingerprintjs.android.fpjs_pro.transport.http_client.HttpClient
import com.fingerprintjs.android.fpjs_pro.transport.http_client.NativeHttpClient
import com.fingerprintjs.android.fpjs_pro.client.FPJSProInterface


class FingerprintJSFactory(
    private val applicationContext: Context
) {

    private val logger = ConsoleLogger()

    fun createInstance(
        configuration: Configuration
    ): FingerprintJS {
        return FingerprintJSPro(
            createApiInteractor(
                configuration.endpointUrl,
                configuration.apiToken,
                configuration.extendedResponseFormat
            )
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
