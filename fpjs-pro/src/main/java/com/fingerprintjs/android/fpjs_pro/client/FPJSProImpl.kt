package com.fingerprintjs.android.fpjs_pro.client


import com.fingerprintjs.android.fpjs_pro.FPJSProClient
import com.fingerprintjs.android.fpjs_pro.transport.http_client.RequestResultType
import com.fingerprintjs.android.fpjs_pro.logger.Logger
import com.fingerprintjs.android.fpjs_pro.transport.fetch_visitor_id_request.FetchVisitorIdResponse
import java.util.concurrent.Executors


internal class FPJSProKotlinImpl(
    private val interactor: FetchVisitorIdInteractor,
    private val logger: Logger
) : FPJSProClient {

    private val executor = Executors.newSingleThreadExecutor()

    override fun getVisitorId(listener: (FetchVisitorIdResponse) -> (Unit)) {
        getVisitorId(emptyMap(), listener) {}
    }

    override fun getVisitorId(listener: (FetchVisitorIdResponse) -> Unit, errorListener: (String) -> Unit) {
        getVisitorId(emptyMap(), listener, errorListener)
    }

    override fun getVisitorId(
        tags: Map<String, Any>,
        listener: (FetchVisitorIdResponse) -> Unit,
        errorListener: (String) -> Unit
    ) {
        executor.execute {
            val result = interactor.getVisitorId(tags)
            when(result.type) {
                RequestResultType.SUCCESS -> {
                    listener.invoke(result.typedResult())
                }
                RequestResultType.ERROR -> {
                    result.typedResult().let {
                        logger.error(this, "Error: ${it.errorMessage}, RequestId: ${it.requestId}")
                    }
                    errorListener.invoke(result.typedResult().errorMessage ?: "")
                }
            }
        }
    }
}
