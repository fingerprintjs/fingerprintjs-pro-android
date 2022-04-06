package com.fingerprintjs.android.fpjs_pro


interface FingerprintJS {
    fun getVisitorId(listener: (FingerprintJSProResponse) -> Unit)
    fun getVisitorId(listener: (FingerprintJSProResponse) -> Unit, errorListener: (Error) -> (Unit))
    fun getVisitorId(
        tags: Map<String, Any>,
        listener: (FingerprintJSProResponse) -> Unit,
        errorListener: (Error) -> (Unit)
    )
}

class Configuration @JvmOverloads constructor(
    val apiToken: String,
    val region: Region = Region.US,
    val endpointUrl: String = region.endpointUrl,
    val extendedResponseFormat: Boolean = false,
    val integrationInfo: List<Pair<String, String>> = emptyList()
) {
    enum class Region(val endpointUrl: String) {
        US("https://api.fpjs.io"),
        EU("https://eu.api.fpjs.io"),
        AP("https://ap.api.fpjs.io")
    }
}
