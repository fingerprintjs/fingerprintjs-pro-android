package com.fingerprintjs.android.fpjs_pro.client


import com.fingerprintjs.android.fpjs_pro.ApiKeyNotFound
import com.fingerprintjs.android.fpjs_pro.api.fetch_visitor_id_request.FetchVisitorIdResponse
import com.fingerprintjs.android.fpjs_pro.logger.ConsoleLogger
import junit.framework.TestCase.fail
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class FingerprintJSProTests {
    @Test
    fun getVisitorIdFailed() {
        val interactor = object : FetchVisitorIdInteractor {
            override fun getVisitorId(tags: Map<String, Any>): FetchVisitorIdResponse {
                return FetchVisitorIdResponse(
                    null,
                    false
                )
            }
        }
        val fingerprintJS = FingerprintJSPro(interactor, ConsoleLogger())

        val countDownLatch = CountDownLatch(1)

        fingerprintJS.getVisitorId({
            fail()
        }, {
            countDownLatch.countDown()
        })

        countDownLatch.await()
    }

    @Test(timeout = 3000L)
    fun getVisitorId_failed_with_error_code() {
        val interactor = object : FetchVisitorIdInteractor {
            override fun getVisitorId(tags: Map<String, Any>): FetchVisitorIdResponse {
                return FetchVisitorIdResponse(
                    JSON_RESPONSE_WITH_ERRROR.encodeToByteArray(),
                    false
                )
            }
        }

        val countDownLatch = CountDownLatch(1)

        val fingerprintJS = FingerprintJSPro(interactor, ConsoleLogger())
        fingerprintJS.getVisitorId({
            fail()
        }, {
            when (it) {
                is ApiKeyNotFound -> {
                    countDownLatch.countDown()
                }
                else -> fail()
            }
        })

        countDownLatch.await(3000L, TimeUnit.MILLISECONDS)
    }

    @Test
    fun getVisitorId_success() {
        val interactor = object : FetchVisitorIdInteractor {
            override fun getVisitorId(tags: Map<String, Any>): FetchVisitorIdResponse {
                return FetchVisitorIdResponse(
                    JSON_EXTENDED_RESPONSE.encodeToByteArray(),
                   true
                )
            }
        }

        val countDownLatch = CountDownLatch(1)

        val fingerprintJS = FingerprintJSPro(interactor, ConsoleLogger())

        fingerprintJS.getVisitorId {
            countDownLatch.countDown()
        }

        countDownLatch.await(3000L, TimeUnit.MILLISECONDS)
    }
}

private const val JSON_RESPONSE_WITH_ERRROR =
    "{\"v\":\"2\",\"requestId\":\"1648734337656.GSgGnp\",\"error\":{\"code\":\"TokenNotFound\",\"message\":\"bad request: invalid token\"},\"products\":{}}"
private const val JSON_EXTENDED_RESPONSE =
    "{\"v\":\"2\",\"requestId\":\"1643358566486.T2dzPl\",\"products\":{\"identification\":{\"data\":{\"visitorToken\":\"0g6fy7PGeQKVHRPl2f6ngHAmO2D2WEi9lQlb5UKVPRZ+6oiof88FimmMroqdPcHdLgDSFAlku7zq5dNC7PSkrAhe24wJ8ZoGWQ==\",\"result\":{\"visitorId\":\"ZyCYWyZejBIO8SBPwJBo\",\"visitorFound\":true,\"meta\":{\"version\":\"v1.1.381+d596961f\"},\"confidence\":{\"score\":0.999},\"incognito\":false,\"browserName\":\"Other\",\"browserVersion\":\"\",\"device\":\"Generic Smartphone\",\"ip\":\"79.136.228.95\",\"ipLocation\":{\"accuracyRadius\":5,\"latitude\":56.4911,\"longitude\":84.9949,\"postalCode\":\"634024\",\"timezone\":\"Asia\\\\/Tomsk\",\"city\":{\"name\":\"Tomsk\"},\"country\":{\"code\":\"RU\",\"name\":\"Russia\"},\"continent\":{\"code\":\"EU\",\"name\":\"Europe\"},\"subdivisions\":[{\"isoCode\":\"TOM\",\"name\":\"Tomsk Oblast\"}]},\"os\":\"Android\",\"osVersion\":\"12\"}}}}}"
