package com.fingerprintjs.android.fpjs_pro.api.fetch_visitor_id_request


import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test


class FetchVisitorIDResponseTests {
    @Test
    fun parse_success() {
        val response = FetchVisitorIdResponse(
            JSON_PLAIN_RESPONSE.encodeToByteArray(),
            false
        )

        assertEquals(0.999, response.typedResult()?.confidenceScore?.score)
    }

    @Test
    fun parse_extended_result_success() {
        val response = FetchVisitorIdResponse(
            JSON_EXTENDED_RESPONSE.encodeToByteArray(),
            true
        )

        assertEquals(5, response.typedResult()?.ipLocation?.accuracyRadius)
    }

    @Test
    fun parse_failed() {
        val response = FetchVisitorIdResponse(
            FAILED_JSON_PLAIN_RESPONSE.encodeToByteArray(),
            false
        )

        assertEquals(response.typedResult(), null)
        assertNotNull(response.typedError())
    }

    @Test
    fun parseError() {
        val response = FetchVisitorIdResponse(
            JSON_RESPONSE_WITH_ERRROR.encodeToByteArray(),
            false
        )

        assertEquals(response.typedResult(), null)
        assertEquals(response.typedError()?.requestId, "1648734337656.GSgGnp")
    }

    @Test
    fun parseWrongFormat_noExceptions() {
        val extendedResponse = FetchVisitorIdResponse(
            JSON_EXTENDED_RESPONSE.encodeToByteArray(),
            false
        )

        extendedResponse.typedResult()

        val plainResponse = FetchVisitorIdResponse(
            JSON_PLAIN_RESPONSE.encodeToByteArray(),
            true
        )

        plainResponse.typedResult()
    }
}

private const val JSON_PLAIN_RESPONSE =
    "{\"v\":\"2\",\"requestId\":\"1643357697766.D521L4\",\"products\":{\"identification\":{\"data\":{\"visitorToken\":\"eyDnXk2WT\\\\/u3bgzA0kgOXuXB1JyyGREroSWFWNK66rIKQEHrnZ53SN1DIyQdXYJMV0PNjuQYYg9ZXYJRIv6tcR0GYd7aZr1X2A==\",\"result\":{\"visitorId\":\"ZyCYWyZejBIO8SBPwJBo\",\"visitorFound\":true,\"meta\":{\"version\":\"v1.1.381+d596961f\"},\"confidence\":{\"score\":0.999}}}}}}"
private const val FAILED_JSON_PLAIN_RESPONSE =
    "{{\"v\":\"2\",\"requestId\":\"1643357697766.D521L4\",\"products\":{\"identification\":{\"data\":{\"visitorToken\":\"eyDnXk2WT\\\\/u3bgzA0kgOXuXB1JyyGREroSWFWNK66rIKQEHrnZ53SN1DIyQdXYJMV0PNjuQYYg9ZXYJRIv6tcR0GYd7aZr1X2A==\",\"result\":{\"visitorId\":\"ZyCYWyZejBIO8SBPwJBo\",\"visitorFound\":true,\"meta\":{\"version\":\"v1.1.381+d596961f\"},\"confidence\":{\"score\":0.999}}}}}}"
private const val JSON_EXTENDED_RESPONSE =
    "{\"v\":\"2\",\"requestId\":\"1643358566486.T2dzPl\",\"products\":{\"identification\":{\"data\":{\"visitorToken\":\"0g6fy7PGeQKVHRPl2f6ngHAmO2D2WEi9lQlb5UKVPRZ+6oiof88FimmMroqdPcHdLgDSFAlku7zq5dNC7PSkrAhe24wJ8ZoGWQ==\",\"result\":{\"visitorId\":\"ZyCYWyZejBIO8SBPwJBo\",\"visitorFound\":true,\"meta\":{\"version\":\"v1.1.381+d596961f\"},\"confidence\":{\"score\":0.999},\"incognito\":false,\"browserName\":\"Other\",\"browserVersion\":\"\",\"device\":\"Generic Smartphone\",\"ip\":\"79.136.228.95\",\"ipLocation\":{\"accuracyRadius\":5,\"latitude\":56.4911,\"longitude\":84.9949,\"postalCode\":\"634024\",\"timezone\":\"Asia\\\\/Tomsk\",\"city\":{\"name\":\"Tomsk\"},\"country\":{\"code\":\"RU\",\"name\":\"Russia\"},\"continent\":{\"code\":\"EU\",\"name\":\"Europe\"},\"subdivisions\":[{\"isoCode\":\"TOM\",\"name\":\"Tomsk Oblast\"}]},\"os\":\"Android\",\"osVersion\":\"12\"}}}}}"
private const val JSON_RESPONSE_WITH_ERRROR =
    "{\"v\":\"2\",\"requestId\":\"1648734337656.GSgGnp\",\"error\":{\"code\":\"TokenNotFound\",\"message\":\"bad request: invalid token\"},\"products\":{}}"
