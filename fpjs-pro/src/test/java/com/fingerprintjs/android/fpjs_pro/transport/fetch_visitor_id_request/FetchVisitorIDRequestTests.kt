package com.fingerprintjs.android.fpjs_pro.transport.fetch_visitor_id_request


import junit.framework.Assert.*
import org.junit.Test
import java.lang.Exception


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
            integrationType = listOf(
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
            integrationType = listOf(
                Pair("fingerprintjs-pro-flutter", "1.0.1"),
            )
        )

        assertEquals(
            "$ENDPOINT_URL/?ci=android/$VERSION&ii=fingerprintjs-pro-flutter/1.0.1",
            request.url
        )
    }

    @Test
    fun parse_success() {
        val response = FetchVisitorIdResult(
            null,
            JSON_PLAIN_RESPONSE.encodeToByteArray(),
            false
        )

        assertEquals(0.999, response.typedResult().confidenceScore.score)
    }

    @Test
    fun parse_extended_result_success() {
        val response = FetchVisitorIdResult(
            null,
            JSON_EXTENDED_RESPONSE.encodeToByteArray(),
            true
        )

        assertEquals(5, response.typedResult().ipLocation?.accuracyRadius)
    }

    @Test
    fun parse_failed() {
        val response = FetchVisitorIdResult(
            null,
            FAILED_JSON_PLAIN_RESPONSE.encodeToByteArray(),
            false
        )
        try {
            response.typedResult()
        } catch (exception: Exception) {
            return
        }
        fail()
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

private const val JSON_PLAIN_RESPONSE =
    "{\"v\":\"2\",\"requestId\":\"1643357697766.D521L4\",\"products\":{\"identification\":{\"data\":{\"visitorToken\":\"eyDnXk2WT\\\\/u3bgzA0kgOXuXB1JyyGREroSWFWNK66rIKQEHrnZ53SN1DIyQdXYJMV0PNjuQYYg9ZXYJRIv6tcR0GYd7aZr1X2A==\",\"result\":{\"visitorId\":\"ZyCYWyZejBIO8SBPwJBo\",\"visitorFound\":true,\"meta\":{\"version\":\"v1.1.381+d596961f\"},\"confidence\":{\"score\":0.999}}}}}}"
private const val FAILED_JSON_PLAIN_RESPONSE =
    "{{\"v\":\"2\",\"requestId\":\"1643357697766.D521L4\",\"products\":{\"identification\":{\"data\":{\"visitorToken\":\"eyDnXk2WT\\\\/u3bgzA0kgOXuXB1JyyGREroSWFWNK66rIKQEHrnZ53SN1DIyQdXYJMV0PNjuQYYg9ZXYJRIv6tcR0GYd7aZr1X2A==\",\"result\":{\"visitorId\":\"ZyCYWyZejBIO8SBPwJBo\",\"visitorFound\":true,\"meta\":{\"version\":\"v1.1.381+d596961f\"},\"confidence\":{\"score\":0.999}}}}}}"
private const val JSON_EXTENDED_RESPONSE =
    "{\"v\":\"2\",\"requestId\":\"1643358566486.T2dzPl\",\"products\":{\"identification\":{\"data\":{\"visitorToken\":\"0g6fy7PGeQKVHRPl2f6ngHAmO2D2WEi9lQlb5UKVPRZ+6oiof88FimmMroqdPcHdLgDSFAlku7zq5dNC7PSkrAhe24wJ8ZoGWQ==\",\"result\":{\"visitorId\":\"ZyCYWyZejBIO8SBPwJBo\",\"visitorFound\":true,\"meta\":{\"version\":\"v1.1.381+d596961f\"},\"confidence\":{\"score\":0.999},\"incognito\":false,\"browserName\":\"Other\",\"browserVersion\":\"\",\"device\":\"Generic Smartphone\",\"ip\":\"79.136.228.95\",\"ipLocation\":{\"accuracyRadius\":5,\"latitude\":56.4911,\"longitude\":84.9949,\"postalCode\":\"634024\",\"timezone\":\"Asia\\\\/Tomsk\",\"city\":{\"name\":\"Tomsk\"},\"country\":{\"code\":\"RU\",\"name\":\"Russia\"},\"continent\":{\"code\":\"EU\",\"name\":\"Europe\"},\"subdivisions\":[{\"isoCode\":\"TOM\",\"name\":\"Tomsk Oblast\"}]},\"os\":\"Android\",\"osVersion\":\"12\"}}}}}"