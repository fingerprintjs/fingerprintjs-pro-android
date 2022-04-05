package com.fingerprintjs.android.fpjs_pro.api.fetch_visitor_id_request


import android.os.Build
import com.fingerprintjs.android.fpjs_pro.Error
import com.fingerprintjs.android.fpjs_pro.transport.http_client.TypedRequestResult
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

//region: Parse response

internal class FetchVisitorIdResult(
    rawResponse: ByteArray?,
    private val extendedFormat: Boolean,
    private val errorFactory: ErrorFactory = ErrorFactoryImpl()
) : TypedRequestResult<FingerprintJSProResponse>(rawResponse) {

    private var typedResponse: FingerprintJSProResponse? = null
    private var typedError: Error? = null

    init {
        val body = rawResponse?.toString(Charsets.UTF_8) ?: "{}"

        val jsonBody = JSONObject(body)
        val requestId = jsonBody.optString(REQUEST_ID_KEY)

        try {
            if (jsonBody.has(ERROR_KEY)) {
                typedError = parseError(requestId, jsonBody)
            } else {
                typedResponse = parseResponse(requestId, jsonBody)
            }
        } catch (exception: Exception) {
            typedResponse = null
            typedError = Error.RESPONSE_CANNOT_BE_PARSED
            typedError?.requestId = requestId
        }
    }

    override fun typedError(): Error? = typedError

    override fun typedResult(): FingerprintJSProResponse? = typedResponse

    private fun parseResponse(requestId: String, jsonBody: JSONObject): FingerprintJSProResponse {
        val result = jsonBody
            .getJSONObject(PRODUCTS_KEY)
            .getJSONObject(IDENTIFICATION_KEY)
            .getJSONObject(DATA_KEY)
            .getJSONObject(RESULT_KEY)

        val deviceId =
            result.getString(VISITOR_ID_KEY)

        val confidenceScore = parseConfidenceScore(result)

        var visitorFound = false
        var ipAddress = UNKNOWN_KEY
        var ipLocation: IpLocation? = null
        var osName = UNKNOWN_KEY
        var osVersion = UNKNOWN_KEY

        if (extendedFormat) {
            visitorFound = result.optBoolean(VISITOR_FOUND_KEY, false)
            ipAddress = result.optString(IP_KEY, UNKNOWN_KEY)
            ipLocation = parseIpLocation(result)
            osName = result.optString(OS_KEY, "Android")
            osVersion = result.optString(OS_VERSION_KEY, Build.VERSION.CODENAME)
        }
        return FingerprintJSProResponse(
            requestId,
            deviceId,
            confidenceScore,
            visitorFound,
            ipAddress,
            ipLocation,
            osName,
            osVersion
        )
    }

    private fun parseError(requestId: String, jsonBody: JSONObject): Error {
        val errorCode = jsonBody
            .getJSONObject(ERROR_KEY)
            .optString(ERROR_CODE_KEY)

        return errorFactory.getError(errorCode, requestId)
    }

    private fun parseConfidenceScore(resultJson: JSONObject): ConfidenceScore {
        return ConfidenceScore(
            resultJson.optJSONObject(CONFIDENCE_KEY)?.optDouble(CONFIDENCE_SCORE_KEY, 0.0) ?: 0.0
        )
    }

    private fun parseIpLocation(resultJson: JSONObject): IpLocation {
        val ipLocationJson = resultJson.optJSONObject(IP_LOCATION_KEY)
        val accuracyRadius = ipLocationJson?.optInt(ACCURACY_RADIUS_KEY, 0) ?: 0
        val latitude = ipLocationJson?.optDouble(LATITUDE_KEY, 0.0) ?: 0.0
        val longitude = ipLocationJson?.optDouble(LONGITUDE_KEY, 0.0) ?: 0.0
        val postalCode = ipLocationJson?.optString(POSTAL_CODE_KEY, UNKNOWN_KEY) ?: UNKNOWN_KEY
        val timezone = ipLocationJson?.optString(TIMEZONE_KEY, UNKNOWN_KEY) ?: UNKNOWN_KEY

        val cityJson = ipLocationJson?.optJSONObject(CITY_KEY)
        val city = IpLocation.City(cityJson?.optString(CITY_NAME_KEY, UNKNOWN_KEY) ?: UNKNOWN_KEY)

        val countryJson = ipLocationJson?.getJSONObject(COUNTRY_KEY)
        val country = IpLocation.Country(
            countryJson?.optString(COUNTRY_CODE_KEY, UNKNOWN_KEY) ?: UNKNOWN_KEY,
            countryJson?.optString(COUNTRY_NAME_KEY, UNKNOWN_KEY) ?: UNKNOWN_KEY
        )

        val continentJson = ipLocationJson?.optJSONObject(CONTINENT_KEY)
        val continent = IpLocation.Continent(
            continentJson?.optString(CONTINENT_CODE_KEY, UNKNOWN_KEY) ?: UNKNOWN_KEY,
            continentJson?.optString(CONTINENT_NAME_KEY, UNKNOWN_KEY) ?: UNKNOWN_KEY
        )

        val subdivisionsJson =
            ipLocationJson?.optJSONArray(SUBDIVISIONS_KEY) ?: JSONArray(emptyArray<JSONObject>())
        val subdivisions = LinkedList<IpLocation.Subdivisions>()

        for (i in 0 until subdivisionsJson.length()) {
            subdivisions.add(
                IpLocation.Subdivisions(
                    subdivisionsJson.optJSONObject(i).optString(ISO_CODE_KEY, UNKNOWN_KEY),
                    subdivisionsJson.optJSONObject(i).optString(SUBDIVISION_NAME_KEY, UNKNOWN_KEY)
                )
            )
        }

        return IpLocation(
            accuracyRadius,
            latitude,
            longitude,
            postalCode,
            timezone,
            city,
            country,
            continent,
            subdivisions
        )
    }
}

//endregion

//region: Const


private const val PRODUCTS_KEY = "products"
private const val IDENTIFICATION_KEY = "identification"
private const val DATA_KEY = "data"
private const val RESULT_KEY = "result"
private const val VISITOR_ID_KEY = "visitorId"
private const val VISITOR_FOUND_KEY = "visitorFound"
private const val CONFIDENCE_KEY = "confidence"
private const val CONFIDENCE_SCORE_KEY = "score"
private const val IP_KEY = "ip"


private const val IP_LOCATION_KEY = "ipLocation"
private const val ACCURACY_RADIUS_KEY = "accuracyRadius"
private const val LATITUDE_KEY = "latitude"
private const val LONGITUDE_KEY = "longitude"
private const val POSTAL_CODE_KEY = "postalCode"
private const val TIMEZONE_KEY = "timezone"

private const val CITY_KEY = "city"
private const val CITY_NAME_KEY = "name"

private const val COUNTRY_KEY = "country"
private const val COUNTRY_CODE_KEY = "code"
private const val COUNTRY_NAME_KEY = "name"

private const val CONTINENT_KEY = "continent"
private const val CONTINENT_CODE_KEY = "code"
private const val CONTINENT_NAME_KEY = "name"

private const val SUBDIVISIONS_KEY = "subdivisions"
private const val ISO_CODE_KEY = "isoCode"
private const val SUBDIVISION_NAME_KEY = "name"


private const val OS_KEY = "os"
private const val OS_VERSION_KEY = "osVersion"

private const val UNKNOWN_KEY = "n\\a"

//endregion
