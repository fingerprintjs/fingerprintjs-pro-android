package com.fingerprintjs.android.fpjs_pro.transport.http_client


import com.fingerprintjs.android.fpjs_pro.Error


internal open class RawRequestResult(
    val rawResponse: ByteArray?
)

internal abstract class TypedRequestResult<T>(
    rawResponse: ByteArray?
) : RawRequestResult(rawResponse) {
    abstract fun typedError(): Error?
    abstract fun typedResult(): T?
}
