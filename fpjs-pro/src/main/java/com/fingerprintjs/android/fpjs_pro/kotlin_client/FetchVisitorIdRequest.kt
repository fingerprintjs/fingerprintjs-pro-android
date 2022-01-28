package com.fingerprintjs.android.fpjs_pro.kotlin_client


import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.Request
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.RequestResultType
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.RequestType
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.TypedRequestResult
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap


//region: Response Types

data class IpLocation(
    val accuracyRadius: Int,
    val latitude: Double,
    val longitude: Double,
    val postalCode: String,
    val timezone: String,
    val city: City,
    val country: Country,
    val subdivisions: List<Subdivisions>

) {

    data class City(
        val name: String
    )

    data class Country(
        val code: String,
        val name: String
    )

    data class Continent(
        val code: String,
        val name: String
    )

    data class Subdivisions(
        val isoCode: String,
        val name: String
    )
}

data class ConfidenceScore(
    val score: Double
)

data class FetchVisitorIdResponse(
    val requestId: String,
    val visitorId: String,
    val visitorFound: Boolean,
    val ipAddress: String,
    val ipLocation: IpLocation,
    val osName: String,
    val osVersion: String,
    val errorMessage: String? = ""
)

//endregion

//region: Parse response

class FetchVisitorIdResult(
    type: RequestResultType,
    rawResponse: ByteArray?
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

            val visitorFound = result.getBoolean(VISITOR_FOUND_KEY)
            val ipAddress = result.getString(IP_KEY)
            val ipLocation = parseIpLocation(result)
            val osName = result.getString(OS_KEY)
            val osVersion = result.getString(OS_VERSION_KEY)
            FetchVisitorIdResponse(
                requestId,
                deviceId,
                visitorFound,
                ipAddress,
                ipLocation,
                osName,
                osVersion
            )
        } catch (exception: Exception) {
            emptyResponse(requestId, "RequestID: $requestId " + rawResponse.toString(Charsets.UTF_8))
        }
    }

    private fun parseConfidenceScore(resultJson: JSONObject): ConfidenceScore {
        return ConfidenceScore(
            resultJson.getJSONObject(CONFIDENCE_KEY).getDouble(CONFIDENCE_SCORE_KEY)
        )
    }

    private fun parseIpLocation(resultJson: JSONObject): IpLocation {
        val ipLocationJson = resultJson.getJSONObject(IP_LOCATION_KEY)
        val accuracyRadius = resultJson.getInt(ACCURACY_RADIUS_KEY)
        val latitude = resultJson.getDouble(LATITUDE_KEY)
        val longitude = resultJson.getDouble(LONGITUDE_KEY)
        val postalCode = resultJson.getString(POSTAL_CODE_KEY)
        val timezone = resultJson.getString(TIMEZONE_KEY)

        val cityJson = ipLocationJson.getJSONObject(CITY_KEY)
        val city = IpLocation.City(cityJson.getString(CITY_NAME_KEY))

        val countryJson = ipLocationJson.getJSONObject(COUNTRY_KEY)
        val country = IpLocation.Country(
            countryJson.getString(COUNTRY_CODE_KEY),
            countryJson.getString(COUNTRY_NAME_KEY)
        )

        val continentJson = ipLocationJson.getJSONObject(CONTINENT_KEY)
        val continent = IpLocation.Continent(
            continentJson.getString(CONTINENT_CODE_KEY),
            continentJson.getString(CONTINENT_NAME_KEY)
        )

        val subdivisionsJson = ipLocationJson.getJSONArray(SUBDIVISIONS_KEY)
        val subdivisions = LinkedList<IpLocation.Subdivisions>()

        for (i in 0..subdivisionsJson.length()) {
            subdivisions.add(
                IpLocation.Subdivisions(
                    subdivisionsJson.getJSONObject(i).getString(ISO_CODE_KEY),
                    subdivisionsJson.getJSONObject(i).getString(SUBDIVISION_NAME_KEY)
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
            subdivisions
        )
    }

    private fun emptyResponse(requestId: String, errorMessage: String): FetchVisitorIdResponse {
        return FetchVisitorIdResponse(
            requestId,
            UNKNOWN_KEY,
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
                emptyList()
            ),
            UNKNOWN_KEY,
            UNKNOWN_KEY,
            errorMessage = errorMessage
        )
    }
}

//endregion

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
    private val packageName: String
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

        if (tag.isNotEmpty()) {
            resultMap[TAGS_KEY] = tag
        }

        return resultMap
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
private const val CITY_NAME_KEY = "city"

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
private const val DEVICE_ID_KEY = "deviceId"
private const val TYPE_KEY = "type"
private const val REQUEST_ID_KEY = "requestId"

private const val UNKNOWN_KEY = "n\\a"

//endregion
