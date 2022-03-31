package com.fingerprintjs.android.fpjs_pro.transport.http_client

import com.fingerprintjs.android.fpjs_pro.FPJSProClient


open class RawRequestResult(
    val error: FPJSProClient.Error? = null,
    val rawResponse: ByteArray?
)

abstract class TypedRequestResult<T>(
    type: FPJSProClient.Error? = null,
    rawResponse: ByteArray?
) : RawRequestResult(type, rawResponse) {
    abstract fun typedResult(): T?
}