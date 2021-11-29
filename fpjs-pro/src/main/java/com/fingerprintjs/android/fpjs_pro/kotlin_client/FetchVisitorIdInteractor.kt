package com.fingerprintjs.android.fpjs_pro.kotlin_client


import com.fingerprintjs.android.fpjs_pro.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fpjs_pro.device_id_providers.MediaDrmIdProvider
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.HttpClient


interface FetchVisitorIdInteractor {
    fun getVisitorId(tags: Map<String, Any>, listener: (String) -> (Unit), errorListener: (String) -> (Unit)): FetchVisitorIdResponse
}

class FetchVisitorIdInteractorImpl(
    private val androidIdProvider: AndroidIdProvider,
    private val gsfIdProvider: GsfIdProvider,
    private val mediaDrmIdProvider: MediaDrmIdProvider,
    private val httpClient: HttpClient,
    private val endpointUrl: String,
    private val authToken: String,
    private val version: String,
    private val appname: String
) : FetchVisitorIdInteractor {


    override fun getVisitorId(tags: Map<String, Any>, listener: (String) -> Unit, errorListener: (String) -> Unit): FetchVisitorIdResponse {
        val androidId = androidIdProvider.getAndroidId()
        val gsfId = gsfIdProvider.getGsfAndroidId()
        val mediaDrmId = mediaDrmIdProvider.getMediaDrmId()
        val s67 = gsfId ?: mediaDrmId
        ?: androidId

        val fetchTokenRequest = FetchVisitorIdRequest(
            endpointUrl, authToken, androidId, gsfId, mediaDrmId, s67, tags, version, appname
        )

        val rawRequestResult = httpClient.performRequest(
            fetchTokenRequest
        )

        val response = FetchVisitorIdResult(rawRequestResult.type, rawRequestResult.rawResponse)
        rawRequestResult.rawResponse?.let {
            println("Response: ${String(it, Charsets.UTF_8)}")
        }
        return response.typedResult()
    }
}
