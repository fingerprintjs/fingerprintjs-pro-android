package com.fingerprintjs.android.fpjs_pro.transport.fetch_visitor_id_request


import android.os.Build
import android.util.Log
import com.fingerprintjs.android.fpjs_pro.transport.http_client.Request
import com.fingerprintjs.android.fpjs_pro.transport.http_client.RequestResultType
import com.fingerprintjs.android.fpjs_pro.transport.http_client.RequestType
import com.fingerprintjs.android.fpjs_pro.transport.http_client.TypedRequestResult
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap


//region: Request
class FetchVisitorIdRequest(
    endpointUrl: String,
    private val publicApiKey: String,
    private val androidId: String,
    private val gsfId: String?,
    private val mediaDrmId: String?,
    private val s67: String,
    private val tag: Map<String, Any>,
    version: String,
    private val packageName: String,
    private val extendedFormat: Boolean
) : Request {

    override val url = "$endpointUrl/?ci=android/$version"
    override val type = RequestType.POST
    override val headers = mapOf(
        "Content-Type" to "application/json"
    )

    override fun bodyAsMap(): Map<String, Any> {
        val resultMap = HashMap<String, Any>()

        val s67Map = mapOf(
            "v" to mapOf(
                DEVICE_ID_KEY to s67,
                TYPE_KEY to "android"
            ),
            "s" to 0
        )


        resultMap[CUSTOMER_KEY] = publicApiKey
        resultMap[URL_KEY] = packageName

        resultMap[S67_KEY] = s67Map
        resultMap[ANDROID_ID_KEY] = mapOf(
            "v" to androidId,
            "s" to 0
        )

        if (gsfId.isNullOrEmpty()) {
            resultMap[GSF_ID_KEY] = mapOf(
                "s" to -1
            )
        } else {
            resultMap[GSF_ID_KEY] = mapOf(
                "s" to 0,
                "v" to gsfId
            )
        }

        if (mediaDrmId.isNullOrEmpty()) {
            resultMap[MEDIA_DRM_KEY] = mapOf(
                "s" to -1
            )
        } else {
            resultMap[MEDIA_DRM_KEY] = mapOf(
                "s" to 0,
                "v" to mediaDrmId
            )
        }

        if (extendedFormat) {
            resultMap[EXTENDED_FORMAT_SUPPORT] = 1
        }

        if (tag.isNotEmpty()) {
            resultMap[TAGS_KEY] = tag
        }

        return resultMap
    }
}

//endregion


//region: Parse response

class FetchVisitorIdResult(
    type: RequestResultType,
    rawResponse: ByteArray?,
    private val extendedFormat: Boolean
) : TypedRequestResult<FetchVisitorIdResponse>(type, rawResponse) {
    override fun typedResult(): FetchVisitorIdResponse {
        val body = rawResponse?.toString(Charsets.UTF_8) ?: return emptyResponse(
            UNKNOWN_KEY,
            rawResponse?.toString(Charsets.UTF_8) ?: UNKNOWN_KEY
        )

        var requestId = ""
        return try {
            val jsonBody = JSONObject(body)
            requestId = jsonBody.getString(REQUEST_ID_KEY)

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
            FetchVisitorIdResponse(
                requestId,
                deviceId,
                confidenceScore,
                visitorFound,
                ipAddress,
                ipLocation,
                osName,
                osVersion
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.e(this.javaClass.canonicalName, exception.toString())
            emptyResponse(
                requestId,
                "RequestID: $requestId " + rawResponse.toString(Charsets.UTF_8)
            )
        }
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

        val cityJson = ipLocationJson.optJSONObject(CITY_KEY)
        val city = IpLocation.City(cityJson?.optString(CITY_NAME_KEY, UNKNOWN_KEY) ?: UNKNOWN_KEY)

        val countryJson = ipLocationJson?.getJSONObject(COUNTRY_KEY)
        val country = IpLocation.Country(
            countryJson?.optString(COUNTRY_CODE_KEY, UNKNOWN_KEY) ?: UNKNOWN_KEY,
            countryJson?.optString(COUNTRY_NAME_KEY, UNKNOWN_KEY) ?: UNKNOWN_KEY
        )

        val continentJson = ipLocationJson.optJSONObject(CONTINENT_KEY)
        val continent = IpLocation.Continent(
            continentJson?.optString(CONTINENT_CODE_KEY, UNKNOWN_KEY) ?: UNKNOWN_KEY,
            continentJson?.optString(CONTINENT_NAME_KEY, UNKNOWN_KEY) ?: UNKNOWN_KEY
        )

        val subdivisionsJson = ipLocationJson.optJSONArray(SUBDIVISIONS_KEY) ?: JSONArray(emptyArray<JSONObject>())
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

    private fun emptyResponse(requestId: String, errorMessage: String): FetchVisitorIdResponse {
        return FetchVisitorIdResponse(
            requestId,
            UNKNOWN_KEY,
            confidenceScore = ConfidenceScore(0.0),
            visitorFound = false,
            ipAddress = UNKNOWN_KEY,
            ipLocation = IpLocation(
                0,
                0.0,
                0.0,
                UNKNOWN_KEY,
                UNKNOWN_KEY,
                IpLocation.City(
                    UNKNOWN_KEY
                ),
                IpLocation.Country(
                    UNKNOWN_KEY,
                    UNKNOWN_KEY
                ),
                IpLocation.Continent(
                    UNKNOWN_KEY, UNKNOWN_KEY
                ),
                emptyList()

            ),
            UNKNOWN_KEY,
            UNKNOWN_KEY,
            errorMessage = errorMessage
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

private const val CUSTOMER_KEY = "c"
private const val TAGS_KEY = "t"
private const val URL_KEY = "url"

private const val ANDROID_ID_KEY = "a1"
private const val MEDIA_DRM_KEY = "a2"
private const val GSF_ID_KEY = "a3"

private const val S67_KEY = "s67"
private const val EXTENDED_FORMAT_SUPPORT = "cbd"
private const val DEVICE_ID_KEY = "deviceId"
private const val TYPE_KEY = "type"
private const val REQUEST_ID_KEY = "requestId"

private const val UNKNOWN_KEY = "n\\a"

//endregion
