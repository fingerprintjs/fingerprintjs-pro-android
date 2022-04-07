package com.fingerprintjs.android.fpjs_pro.api.fetch_visitor_id_request


import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Test


class FetchVisitorIDRequestTests {
    @Test
    fun request_formation_correct() {
        val request = FetchVisitorIdRequest(
            endpointUrl = ENDPOINT_URL,
            publicApiKey = PUBLIC_API_KEY,
            androidId = ANDROID_ID,
            gsfId = GSF_ID,
            mediaDrmId = MEDIA_DRM_ID,
            s67 = S67,
            tag = emptyMap(),
            version = VERSION,
            packageName = PACKAGE_NAME,
            false
        )

        val requestMap = request.bodyAsMap()

        assertEquals("$ENDPOINT_URL/?ci=android/$VERSION", request.url)
        assertEquals(PUBLIC_API_KEY, requestMap[CUSTOMER_KEY])
        assertEquals(ANDROID_ID, (requestMap[ANDROID_ID_KEY] as Map<*, *>)[VALUE_KEY])
        assertEquals(GSF_ID, (requestMap[GSF_ID_KEY] as Map<*, *>)[VALUE_KEY])
        assertEquals(MEDIA_DRM_ID, (requestMap[MEDIA_DRM_KEY] as Map<*, *>)[VALUE_KEY])
        assertEquals(
            S67,
            ((requestMap[S67_KEY] as Map<*, *>)[VALUE_KEY] as Map<*, *>)[DEVICE_ID_KEY]
        )
        assertEquals(PACKAGE_NAME, requestMap[URL_KEY])
        assertFalse(requestMap.containsKey(EXTENDED_FORMAT_SUPPORT))
        assertFalse(requestMap.containsKey(TAGS_KEY))
    }

    @Test
    fun extended_result_parameter_correct() {
        val request = FetchVisitorIdRequest(
            endpointUrl = ENDPOINT_URL,
            publicApiKey = PUBLIC_API_KEY,
            androidId = ANDROID_ID,
            gsfId = GSF_ID,
            mediaDrmId = MEDIA_DRM_ID,
            s67 = S67,
            tag = emptyMap(),
            version = VERSION,
            packageName = PACKAGE_NAME,
            true
        )

        assertEquals(1, request.bodyAsMap()[EXTENDED_FORMAT_SUPPORT])
    }

    @Test
    fun tags_parameter_correct() {
        val tags = mapOf("parameter" to "value")
        val request = FetchVisitorIdRequest(
            endpointUrl = ENDPOINT_URL,
            publicApiKey = PUBLIC_API_KEY,
            androidId = ANDROID_ID,
            gsfId = GSF_ID,
            mediaDrmId = MEDIA_DRM_ID,
            s67 = S67,
            tag = tags,
            version = VERSION,
            packageName = PACKAGE_NAME,
            true
        )

        assertEquals(request.bodyAsMap()[TAGS_KEY], tags)
    }

    @Test
    fun integration_type_two_parameter() {
        val request = FetchVisitorIdRequest(
            endpointUrl = ENDPOINT_URL,
            publicApiKey = PUBLIC_API_KEY,
            androidId = ANDROID_ID,
            gsfId = GSF_ID,
            mediaDrmId = MEDIA_DRM_ID,
            s67 = S67,
            tag = emptyMap(),
            version = VERSION,
            packageName = PACKAGE_NAME,
            false,
            integrationInfo = listOf(
                Pair("fingerprintjs-pro-flutter", "1.0.1"),
                Pair("fingerprintjs-pro-react-native", "1.2.0")
            )
        )

        assertEquals(
            "$ENDPOINT_URL/?ci=android/$VERSION&ii=fingerprintjs-pro-flutter/1.0.1&ii=fingerprintjs-pro-react-native/1.2.0",
            request.url
        )
    }

    @Test
    fun integration_type_one_parameter() {
        val request = FetchVisitorIdRequest(
            endpointUrl = ENDPOINT_URL,
            publicApiKey = PUBLIC_API_KEY,
            androidId = ANDROID_ID,
            gsfId = GSF_ID,
            mediaDrmId = MEDIA_DRM_ID,
            s67 = S67,
            tag = emptyMap(),
            version = VERSION,
            packageName = PACKAGE_NAME,
            false,
            integrationInfo = listOf(
                Pair("fingerprintjs-pro-flutter", "1.0.1"),
            )
        )

        assertEquals(
            "$ENDPOINT_URL/?ci=android/$VERSION&ii=fingerprintjs-pro-flutter/1.0.1",
            request.url
        )
    }
}

private const val VALUE_KEY = "v"
private const val ENDPOINT_URL = "https://api.fpjs.io"
private const val PUBLIC_API_KEY = "sadsdwqdsadwqgsdlgjshdfg"
private const val ANDROID_ID = "androidId"
private const val GSF_ID = "gsfId"
private const val MEDIA_DRM_ID = "mediaDrmId"
private const val PACKAGE_NAME = "com.fingerprintjs.android"
private const val VERSION = "2.0.0"
private const val S67 = "s67"
