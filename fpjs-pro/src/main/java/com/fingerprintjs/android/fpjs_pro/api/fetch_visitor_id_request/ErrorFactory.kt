package com.fingerprintjs.android.fpjs_pro.api.fetch_visitor_id_request

import com.fingerprintjs.android.fpjs_pro.Error


internal interface ErrorFactory {
    fun getError(errorKey: String, requestId: String): Error
}

internal class ErrorFactoryImpl : ErrorFactory {
    override fun getError(errorKey: String, requestId: String): Error {
        return when (errorKey) {
            API_REQUIRED_KEY -> createError(Error.API_REQUIRED, requestId, API_REQUIRED_DESCRIPTION)
            API_NOT_FOUND_KEY -> createError(Error.API_NOT_FOUND, requestId, API_NOT_FOUND_DESCRIPTION)
            API_EXPIRED_KEY -> createError(Error.API_EXPIRED, requestId, API_EXPIRED_DESCRIPTION)
            REQUEST_CANNOT_BE_PARSED_KEY -> createError(Error.REQUEST_CANNOT_BE_PARSED, requestId, REQUEST_CANNOT_BE_PARSED_DESCRIPTION)
            FAILED_KEY -> createError(Error.FAILED, requestId, FAILED_DESCRIPTION)
            REQUEST_TIMEOUT_KEY -> createError(Error.REQUEST_TIMEOUT, requestId, REQUEST_TIMEOUT_DESCRIPTION)
            TOO_MANY_REQUESTS_KEY -> createError(Error.TOO_MANY_REQUESTS, requestId, TOO_MANY_REQUESTS_DESCRIPTION)
            ORIGIN_NOT_AVAILABLE_KEY -> createError(Error.ORIGIN_NOT_AVAILABLE, requestId, ORIGIN_NOT_AVAILABLE_DESCRIPTION)
            HEADER_RESTRICTED_KEY -> createError(Error.HEADER_RESTRICTED, requestId, HEADER_RESTRICTED_DESCRIPTION)
            NOT_AVAILABLE_FOR_CRAWL_BOTS_KEY -> createError(Error.NOT_AVAILABLE_FOR_CRAWL_BOTS, requestId, NOT_AVAILABLE_FOR_CRAWL_BOTS_DESCRIPTION)
            NOT_AVAILABLE_WITHOUT_UA_KEY -> createError(Error.NOT_AVAILABLE_WITHOUT_UA, requestId, NOT_AVAILABLE_WITHOUT_UA_DESCRIPTION)
            WRONG_REGION_KEY -> createError(Error.WRONG_REGION, requestId, WRONG_REGION_DESCRIPTION)
            SUBSCRIBTION_NOT_ACTIVE_KEY -> createError(Error.SUBSCRIPTION_NOT_ACTIVE, requestId, SUBSCRIBTION_NOT_ACTIVE_DESCRIPTION)
            UNSUPPORTED_VERISON_KEY -> createError(Error.UNSUPPORTED_VERSION, requestId, UNSUPPORTED_VERISON_DESCRIPTION)
            INSTALLATION_METHOD_RESTRICTED_KEY -> createError(Error.INSTALLATION_METHOD_RESTRICTED, requestId, INSTALLATION_METHOD_RESTRICTED_DESCRIPTION)
            else -> createError(Error.UNKNOWN_ERROR, requestId, UNKNOWN_ERROR_DESCRIPTION)

        }
    }

    private fun createError(error: Error, requestId: String, description: String): Error {
        error.description = description
        error.requestId = requestId
        return error
    }
}

private const val API_REQUIRED_KEY = "TokenRequired"
private const val API_REQUIRED_DESCRIPTION = "API key required"

private const val API_NOT_FOUND_KEY = "TokenNotFound"
private const val API_NOT_FOUND_DESCRIPTION = "API key not found"

private const val API_EXPIRED_KEY = "TokenExpired"
private const val API_EXPIRED_DESCRIPTION = "API key expired"

private const val REQUEST_CANNOT_BE_PARSED_KEY = "RequestCannotBeParsed"
private const val REQUEST_CANNOT_BE_PARSED_DESCRIPTION = "Request cannot be parsed"

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
private const val NOT_AVAILABLE_WITHOUT_UA_DESCRIPTION = "Not available when User-Agent is unspecified"

private const val WRONG_REGION_KEY = "WrongRegion"
private const val WRONG_REGION_DESCRIPTION = "Wrong region"

private const val SUBSCRIBTION_NOT_ACTIVE_KEY = "SubscriptionNotActive"
private const val SUBSCRIBTION_NOT_ACTIVE_DESCRIPTION = "Subscription is not active"

private const val UNSUPPORTED_VERISON_KEY = "UnsupportedVersion"
private const val UNSUPPORTED_VERISON_DESCRIPTION = "Android agent version not supported"

private const val INSTALLATION_METHOD_RESTRICTED_KEY = "InstallationMethodRestricted"
private const val INSTALLATION_METHOD_RESTRICTED_DESCRIPTION = "The installation method of the agent is not allowed for the customer"

private const val UNKNOWN_ERROR_DESCRIPTION = "Unknown error."
