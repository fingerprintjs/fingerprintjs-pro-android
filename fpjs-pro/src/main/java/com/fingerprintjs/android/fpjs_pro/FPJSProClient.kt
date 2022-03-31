package com.fingerprintjs.android.fpjs_pro


import com.fingerprintjs.android.fpjs_pro.transport.fetch_visitor_id_request.FetchVisitorIdResponse


interface FPJSProClient {
    fun getVisitorId(listener: (FetchVisitorIdResponse) -> Unit)
    fun getVisitorId(listener: (FetchVisitorIdResponse) -> Unit, errorListener: (Error) -> (Unit))
    fun getVisitorId(
        tags: Map<String, Any>,
        listener: (FetchVisitorIdResponse) -> Unit,
        errorListener: (Error) -> (Unit)
    )

    enum class Error(
        val description: String,
        var requestId: String = "Unknown"
    ) {
        INTERNAL("Failed while collecting data."),
        NETWORK("Failed while making request to the FingerprintJS API. Check your internet connection and the endpoint URL."),
        UNRECOGNIZED_RESPONSE("Failed while parsing response. Check if your library version is up to dated."),
        API_KEY("Your Public API key is not registered. Visit https://dashboard.fingerprintjs.com/ for getting the actual one.");
    }
}

class Configuration @JvmOverloads constructor(
    val apiToken: String,
    val region: Region = Region.US,
    val endpointUrl: String = region.endpointUrl,
    val extendedResponseFormat: Boolean = false,
    val integrationType: List<Pair<String, String>> = emptyList()
) {
    enum class Region(val endpointUrl: String) {
        US("https://api.fpjs.io"),
        EU("https://eu.api.fpjs.io"),
        AP("https://ap.api.fpjs.io")
    }
}
