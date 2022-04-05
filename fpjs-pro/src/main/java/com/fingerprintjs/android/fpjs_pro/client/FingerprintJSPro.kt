package com.fingerprintjs.android.fpjs_pro.client


import com.fingerprintjs.android.fpjs_pro.FingerprintJS
import com.fingerprintjs.android.fpjs_pro.api.fetch_visitor_id_request.FingerprintJSProResponse
import com.fingerprintjs.android.fpjs_pro.Error
import java.util.concurrent.Executors


internal class FingerprintJSPro(
    private val interactor: FetchVisitorIdInteractor
) : FingerprintJS {

    private val executor = Executors.newSingleThreadExecutor()

    override fun getVisitorId(listener: (FingerprintJSProResponse) -> (Unit)) {
        getVisitorId(emptyMap(), listener) {}
    }

    override fun getVisitorId(
        listener: (FingerprintJSProResponse) -> Unit,
        errorListener: (Error) -> Unit
    ) {
        getVisitorId(emptyMap(), listener, errorListener)
    }

    override fun getVisitorId(
        tags: Map<String, Any>,
        listener: (FingerprintJSProResponse) -> Unit,
        errorListener: (Error) -> Unit
    ) {
        executor.execute {
            val result = interactor.getVisitorId(tags)

            val error = result.typedError()
            val typedResult = result.typedResult()

            if (error == null && typedResult != null) {
                listener.invoke(typedResult)
                return@execute
            } else {
                errorListener.invoke(result.typedError() ?: Error.UNKNOWN_ERROR)
            }
        }
    }
}
