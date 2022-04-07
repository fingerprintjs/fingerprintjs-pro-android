package com.fingerprintjs.android.fpjs_pro.api.fetch_visitor_id_request


import com.fingerprintjs.android.fpjs_pro.*


internal interface ErrorFactory {
    fun getError(errorKey: String, errorDescription: String?, requestId: String): Error
}

internal class ErrorFactoryImpl : ErrorFactory {
    override fun getError(errorKey: String, errorDescription: String?, requestId: String): Error {
        return when (errorKey) {
            API_REQUIRED_KEY -> ApiKeyRequired(
                requestId,
                errorDescription ?: API_REQUIRED_DESCRIPTION
            )
            API_NOT_FOUND_KEY -> ApiKeyNotFound(
                requestId,
                errorDescription ?: API_NOT_FOUND_DESCRIPTION
            )
            API_EXPIRED_KEY -> ApiKeyExpired(requestId, API_EXPIRED_DESCRIPTION)
            REQUEST_CANNOT_BE_PARSED_KEY -> RequestCannotBeParsed(
                requestId,
                errorDescription ?: REQUEST_CANNOT_BE_PARSED_DESCRIPTION
            )
            FAILED_KEY -> Failed(requestId, FAILED_DESCRIPTION)
            REQUEST_TIMEOUT_KEY -> RequestTimeout(
                requestId,
                errorDescription ?: REQUEST_TIMEOUT_DESCRIPTION
            )
            TOO_MANY_REQUESTS_KEY -> TooManyRequest(
                requestId,
                errorDescription ?: TOO_MANY_REQUESTS_DESCRIPTION
            )
            ORIGIN_NOT_AVAILABLE_KEY -> OriginNotAvailable(
                requestId,
                errorDescription ?: ORIGIN_NOT_AVAILABLE_DESCRIPTION
            )
            HEADER_RESTRICTED_KEY -> HeaderRestricted(
                requestId,
                errorDescription ?: HEADER_RESTRICTED_DESCRIPTION
            )
            NOT_AVAILABLE_FOR_CRAWL_BOTS_KEY -> NotAvailableForCrawlBots(
                requestId,
                errorDescription ?: NOT_AVAILABLE_FOR_CRAWL_BOTS_DESCRIPTION
            )
            NOT_AVAILABLE_WITHOUT_UA_KEY -> NotAvailableWithoutUA(
                requestId,
                errorDescription ?: NOT_AVAILABLE_WITHOUT_UA_DESCRIPTION
            )
            WRONG_REGION_KEY -> WrongRegion(requestId, WRONG_REGION_DESCRIPTION)
            SUBSCRIBTION_NOT_ACTIVE_KEY -> SubscriptionNotActive(
                requestId,
                errorDescription ?: SUBSCRIBTION_NOT_ACTIVE_DESCRIPTION
            )
            UNSUPPORTED_VERISON_KEY -> UnsupportedVersion(
                requestId,
                errorDescription ?: UNSUPPORTED_VERISON_DESCRIPTION
            )
            INSTALLATION_METHOD_RESTRICTED_KEY -> InstallationMethodRestricted(
                requestId,
                errorDescription ?: INSTALLATION_METHOD_RESTRICTED_DESCRIPTION
            )
            else -> UnknownError(requestId, UNKNOWN_ERROR_DESCRIPTION)

        }
    }
}

private const val API_REQUIRED_KEY = "TokenRequired"
private const val API_REQUIRED_DESCRIPTION = "API key required"

private const val API_NOT_FOUND_KEY = "TokenNotFound"
private const val API_NOT_FOUND_DESCRIPTION = "API key not found"

private const val API_EXPIRED_KEY = "TokenExpired"
private const val API_EXPIRED_DESCRIPTION = "API key expired"

private const val REQUEST_CANNOT_BE_PARSED_KEY = "RequestCannotBeParsed"
internal const val REQUEST_CANNOT_BE_PARSED_DESCRIPTION = "Request cannot be parsed"

private const val FAILED_KEY = "Failed"
private const val FAILED_DESCRIPTION = "Request failed"

private const val REQUEST_TIMEOUT_KEY = "RequestTimeout"
private const val REQUEST_TIMEOUT_DESCRIPTION = "Server-side timeout"

private const val TOO_MANY_REQUESTS_KEY = "TooManyRequests"
private const val TOO_MANY_REQUESTS_DESCRIPTION = "Too many requests, rate limit exceeded"

private const val ORIGIN_NOT_AVAILABLE_KEY = "OriginNotAvailable"
private const val ORIGIN_NOT_AVAILABLE_DESCRIPTION = "Not available for this origin"

private const val HEADER_RESTRICTED_KEY = "HeaderRestricted"
private const val HEADER_RESTRICTED_DESCRIPTION = "Not available with restricted header"

private const val NOT_AVAILABLE_FOR_CRAWL_BOTS_KEY = "NotAvailableForCrawlBots"
private const val NOT_AVAILABLE_FOR_CRAWL_BOTS_DESCRIPTION = "Not available for crawl bots"

private const val NOT_AVAILABLE_WITHOUT_UA_KEY = "NotAvailableWithoutUA"
private const val NOT_AVAILABLE_WITHOUT_UA_DESCRIPTION =
    "Not available when User-Agent is unspecified"

private const val WRONG_REGION_KEY = "WrongRegion"
private const val WRONG_REGION_DESCRIPTION = "Wrong region"

private const val SUBSCRIBTION_NOT_ACTIVE_KEY = "SubscriptionNotActive"
private const val SUBSCRIBTION_NOT_ACTIVE_DESCRIPTION = "Subscription is not active"

private const val UNSUPPORTED_VERISON_KEY = "UnsupportedVersion"
private const val UNSUPPORTED_VERISON_DESCRIPTION = "Android agent version not supported"

private const val INSTALLATION_METHOD_RESTRICTED_KEY = "InstallationMethodRestricted"
private const val INSTALLATION_METHOD_RESTRICTED_DESCRIPTION =
    "The installation method of the agent is not allowed for the customer"

internal const val NETWORK_ERROR_DESCRIPTION = "Network error. Check your connection or the endpoint URL"

private const val UNKNOWN_ERROR_DESCRIPTION = "Unknown error."
