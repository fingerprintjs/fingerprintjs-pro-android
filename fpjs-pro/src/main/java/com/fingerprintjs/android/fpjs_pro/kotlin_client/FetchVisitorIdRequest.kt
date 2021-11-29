package com.fingerprintjs.android.fpjs_pro.kotlin_client


import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.Request
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.RequestResultType
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.RequestType
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.TypedRequestResult
import org.json.JSONObject


data class FetchVisitorIdResponse(
    val visitorId: String,
    val errorMessage: String? = ""
)

class FetchVisitorIdResult(
    type: RequestResultType,
    rawResponse: ByteArray?
) : TypedRequestResult<FetchVisitorIdResponse>(type, rawResponse) {
    override fun typedResult(): FetchVisitorIdResponse {
        val errorResponse = FetchVisitorIdResponse("", rawResponse?.toString(Charsets.UTF_8))
        val body = rawResponse?.toString(Charsets.UTF_8) ?: return errorResponse
        return try {
            val jsonBody = JSONObject(body)
            val deviceId = jsonBody
                .getJSONObject("products")
                .getJSONObject("identification")
                .getJSONObject("data")
                .getJSONObject("result")
                .getString("visitorId")
            FetchVisitorIdResponse(deviceId)
        } catch (exception: Exception) {
            errorResponse
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
    private val version: String,
    private val packageName: String
) : Request {

    override val url = "$endpointUrl/"
    override val type = RequestType.POST
    override val headers = mapOf(
        "Content-Type" to "application/json"
    )

    override fun bodyAsMap(): Map<String, Any> {
        val resultMap = HashMap<String, Any>()

        val s67Map = mapOf(
            "deviceId" to s67,
            "type" to "android",
            "version" to version
        )

        resultMap["c"] = publicApiKey
        resultMap["url"] = packageName

        resultMap["s67"] = s67Map
        resultMap["a1"] = androidId
        gsfId?.let {
            resultMap["a2"] = it
        }

        mediaDrmId?.let {
            resultMap["a3"] = it
            return resultMap
        }

        if (tag.isNotEmpty()) {
            resultMap["t"] = tag
        }

        return resultMap
    }
}