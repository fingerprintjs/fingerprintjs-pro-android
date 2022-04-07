package com.fingerprintjs.android.fpjs_pro.client


import com.fingerprintjs.android.fpjs_pro.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.MediaDrmIdProvider
import com.fingerprintjs.android.fpjs_pro.api.fetch_visitor_id_request.FetchVisitorIdRequest
import com.fingerprintjs.android.fpjs_pro.api.fetch_visitor_id_request.FetchVisitorIdResponse
import com.fingerprintjs.android.fpjs_pro.transport.http_client.HttpClient


internal interface FetchVisitorIdInteractor {
    fun getVisitorId(tags: Map<String, Any>): FetchVisitorIdResponse
}

internal class FetchVisitorIdInteractorImpl(
    private val androidIdProvider: AndroidIdProvider,
    private val gsfIdProvider: GsfIdProvider,
    private val mediaDrmIdProvider: MediaDrmIdProvider,
    private val httpClient: HttpClient,
    private val endpointUrl: String,
    private val authToken: String,
    private val version: String,
    private val appname: String,
    private val extendedResponse: Boolean,
    private val integrationInfo: List<Pair<String, String>> = emptyList()
) : FetchVisitorIdInteractor {

    override fun getVisitorId(tags: Map<String, Any>): FetchVisitorIdResponse {
        val androidId = androidIdProvider.getAndroidId()
        val gsfId = gsfIdProvider.getGsfAndroidId()
        val mediaDrmId = mediaDrmIdProvider.getMediaDrmId()
        val s67 = gsfId ?: mediaDrmId ?: androidId

        val fetchTokenRequest = FetchVisitorIdRequest(
            endpointUrl,
            authToken,
            androidId,
            gsfId,
            mediaDrmId,
            s67,
            tags,
            version,
            appname,
            extendedResponse,
            integrationInfo
        )

        val rawRequestResult = httpClient.performRequest(
            fetchTokenRequest
        )

        return FetchVisitorIdResponse(
            rawRequestResult.rawResponse,
            extendedResponse
        )
    }
}
