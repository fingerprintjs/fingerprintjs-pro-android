package com.fingerprintjs.android.fpjs_pro


import com.fingerprintjs.android.fpjs_pro.api.fetch_visitor_id_request.NETWORK_ERROR_DESCRIPTION


sealed class Error(
    val requestId: String = UNKNOWN,
    val description: String? = UNKNOWN
)

class ApiKeyRequired(requestId: String, errorDescription: String?) : Error(requestId, errorDescription)

class ApiKeyNotFound(requestId: String, errorDescription: String?) : Error(requestId, errorDescription)

class ApiKeyExpired(requestId: String, errorDescription: String?) : Error(requestId, errorDescription)

class RequestCannotBeParsed(requestId: String, errorDescription: String?) :
    Error(requestId, errorDescription)

class Failed(requestId: String, errorDescription: String?) : Error(requestId, errorDescription)

class RequestTimeout(requestId: String, errorDescription: String?) :
    Error(requestId, errorDescription)

class TooManyRequest(requestId: String, errorDescription: String?) :
    Error(requestId, errorDescription)

class OriginNotAvailable(requestId: String, errorDescription: String?) :
    Error(requestId, errorDescription)

class HeaderRestricted(requestId: String, errorDescription: String?) :
    Error(requestId, errorDescription)

class NotAvailableForCrawlBots(requestId: String, errorDescription: String?) :
    Error(requestId, errorDescription)

class NotAvailableWithoutUA(requestId: String, errorDescription: String?) :
    Error(requestId, errorDescription)

class WrongRegion(requestId: String, errorDescription: String?) : Error(requestId, errorDescription)

class SubscriptionNotActive(requestId: String, errorDescription: String?) :
    Error(requestId, errorDescription)

class UnsupportedVersion(requestId: String, errorDescription: String?) :
    Error(requestId, errorDescription)

class InstallationMethodRestricted(requestId: String, errorDescription: String?) :
    Error(requestId, errorDescription)

class ResponseCannotBeParsed(requestId: String, errorDescription: String?) :
    Error(requestId, errorDescription)

class NetworkError(description: String = NETWORK_ERROR_DESCRIPTION) :
    Error(description = description)

class UnknownError(requestId: String = UNKNOWN, errorDescription: String? = UNKNOWN) :
    Error(requestId, errorDescription)

private const val UNKNOWN = "Unknown"
