package com.fingerprintjs.android.fpjs_pro.api.fetch_visitor_id_request


import com.fingerprintjs.android.fpjs_pro.transport.http_client.Request
import com.fingerprintjs.android.fpjs_pro.transport.http_client.RequestType


//region: Request
internal class FetchVisitorIdRequest(
    private val endpointUrl: String,
    private val publicApiKey: String,
    private val androidId: String,
    private val gsfId: String?,
    private val mediaDrmId: String?,
    private val s67: String,
    private val tag: Map<String, Any>,
    private val version: String,
    private val packageName: String,
    private val extendedFormat: Boolean,
    private val integrationInfo: List<Pair<String, String>> = emptyList()
) : Request {

    override val url
        get() = createUrl()

    override val type = RequestType.POST
    override val headers = mapOf(
        "Content-Type" to "application/json"
    )

    private fun createUrl(): String {
        var ur = "$endpointUrl/?ci=android/$version"
        integrationInfo.forEach {
            ur = "$ur&ii=${it.first}/${it.second}"
        }
        return ur
    }

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


//region: Const

internal const val CUSTOMER_KEY = "c"
internal const val TAGS_KEY = "t"
internal const val URL_KEY = "url"

internal const val ANDROID_ID_KEY = "a1"
internal const val MEDIA_DRM_KEY = "a2"
internal const val GSF_ID_KEY = "a3"

internal const val S67_KEY = "s67"
internal const val EXTENDED_FORMAT_SUPPORT = "cbd"

internal const val DEVICE_ID_KEY = "deviceId"

private const val TYPE_KEY = "type"

internal const val REQUEST_ID_KEY = "requestId"
internal const val ERROR_KEY = "error"
internal const val ERROR_CODE_KEY = "code"
internal const val ERROR_MESSAGE_KEY = "message"

//endregion
