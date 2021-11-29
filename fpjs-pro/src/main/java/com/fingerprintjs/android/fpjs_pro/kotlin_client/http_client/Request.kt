package com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client


enum class RequestType {
    GET,
    POST
}

interface Request {
    val url: String
    val type: RequestType
    val headers: Map<String, String>
    fun bodyAsMap(): Map<String, Any>
}