package com.fingerprintjs.android.fpjs_pro


enum class Error(
    var errorCode: String = UNKNOWN,
    var description: String = UNKNOWN,
    var requestId: String = UNKNOWN
) {
    API_REQUIRED,
    API_NOT_FOUND,
    API_EXPIRED,
    REQUEST_CANNOT_BE_PARSED,
    FAILED,
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    ORIGIN_NOT_AVAILABLE,
    HEADER_RESTRICTED,
    NOT_AVAILABLE_FOR_CRAWL_BOTS,
    NOT_AVAILABLE_WITHOUT_UA,
    WRONG_REGION,
    SUBSCRIPTION_NOT_ACTIVE,
    UNSUPPORTED_VERSION,
    INSTALLATION_METHOD_RESTRICTED,
    RESPONSE_CANNOT_BE_PARSED,
    UNKNOWN_ERROR
}

private const val UNKNOWN = "Unknown"