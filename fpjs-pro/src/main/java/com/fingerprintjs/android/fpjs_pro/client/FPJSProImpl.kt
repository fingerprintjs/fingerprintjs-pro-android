package com.fingerprintjs.android.fpjs_pro.client


import com.fingerprintjs.android.fpjs_pro.FPJSProClient
import com.fingerprintjs.android.fpjs_pro.tools.executeSafe
import com.fingerprintjs.android.fpjs_pro.transport.fetch_visitor_id_request.FetchVisitorIdResponse
import java.util.concurrent.Executors


internal class FPJSProKotlinImpl(
    private val interactor: FetchVisitorIdInteractor
) : FPJSProClient {

    private val executor = Executors.newSingleThreadExecutor()

    override fun getVisitorId(listener: (FetchVisitorIdResponse) -> (Unit)) {
        getVisitorId(emptyMap(), listener) {}
    }

    override fun getVisitorId(
        listener: (FetchVisitorIdResponse) -> Unit,
        errorListener: (FPJSProClient.Error) -> Unit
    ) {
        getVisitorId(emptyMap(), listener, errorListener)
    }

    override fun getVisitorId(
        tags: Map<String, Any>,
        listener: (FetchVisitorIdResponse) -> Unit,
        errorListener: (FPJSProClient.Error) -> Unit
    ) {
        executor.execute {
            val result = interactor.getVisitorId(tags)

            if (result.error == null) {
                val typedResult = executeSafe(
                    {
                        result.typedResult()
                    }, null
                )

                if (typedResult == null) {
                    val error = FPJSProClient.Error.UNRECOGNIZED_RESPONSE
                    error.requestId = result.typedResult().requestId
                    errorListener.invoke(error)
                }
                listener.invoke(result.typedResult())
                return@execute
            }


            val error = result.error
            error.requestId = result.typedResult().requestId
            errorListener.invoke(result.error)
        }
    }
}
