package com.fingerprintjs.android.fpjs_pro


import com.fingerprintjs.android.fpjs_pro.transport.fetch_visitor_id_request.FetchVisitorIdResponse


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
    val extendedResponseFormat: Boolean = false,
    val integrationType: List<Pair<String, String>> = emptyList()
) {
    enum class Region(val endpointUrl: String) {
        US("https://api.fpjs.io"),
        EU("https://eu.api.fpjs.io"),
        AP("https://ap.api.fpjs.io")
    }
}
