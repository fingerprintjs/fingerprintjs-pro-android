package com.fingerprintjs.android.fpjs_pro.kotlin_client


import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.Request
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.RequestResultType
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.RequestType
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.TypedRequestResult
import org.json.JSONObject


data class FetchVisitorIdResponse(
    val requestId: String,
    val visitorId: String,
    val errorMessage: String? = ""
)

class FetchVisitorIdResult(
    type: RequestResultType,
    rawResponse: ByteArray?
) : TypedRequestResult<FetchVisitorIdResponse>(type, rawResponse) {
    override fun typedResult(): FetchVisitorIdResponse {
        val body = rawResponse?.toString(Charsets.UTF_8) ?: return FetchVisitorIdResponse(
            "",
            "",
            rawResponse?.toString(Charsets.UTF_8)
        )
        var requestId = ""
        return try {
            val jsonBody = JSONObject(body)
            requestId = jsonBody.getString(REQUEST_ID_KEY)
            val deviceId = jsonBody
                .getJSONObject(PRODUCTS_KEY)
                .getJSONObject(IDENTIFICATION_KEY)
                .getJSONObject(DATA_KEY)
                .getJSONObject(RESULT_KEY)
                .getString(VISITOR_ID_KEY)
            FetchVisitorIdResponse(requestId, deviceId)
        } catch (exception: Exception) {
            FetchVisitorIdResponse(requestId, "", "RequestID: $requestId " + rawResponse.toString(Charsets.UTF_8))
        }
    }
}


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

private const val PRODUCTS_KEY = "products"
private const val IDENTIFICATION_KEY = "identification"
private const val DATA_KEY = "data"
private const val RESULT_KEY = "result"
private const val VISITOR_ID_KEY = "visitorId"

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