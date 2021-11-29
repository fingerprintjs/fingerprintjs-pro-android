package com.fingerprintjs.android.fpjs_pro.kotlin_client


import com.fingerprintjs.android.fpjs_pro.FPJSProClient
import com.fingerprintjs.android.fpjs_pro.kotlin_client.http_client.RequestResultType
import java.util.concurrent.Executors


class FPJSProKotlinClient(
    private val interactor: FetchVisitorIdInteractor
) : FPJSProClient {

    private val executor = Executors.newSingleThreadExecutor()

    override fun getVisitorId(listener: (String) -> (Unit)) {
        getVisitorId(emptyMap(), listener, {})
    }

    override fun getVisitorId(listener: (String) -> Unit, errorListener: (String) -> Unit) {
        getVisitorId(emptyMap(), listener, errorListener)
    }

    override fun getVisitorId(
        tags: Map<String, Any>,
        listener: (String) -> Unit,
        errorListener: (String) -> Unit
    ) {
        executor.execute {
            val result = interactor.getVisitorId(tags)
            when(result.type) {
                RequestResultType.SUCCESS -> {
                    listener.invoke(result.typedResult().visitorId)
                }
                RequestResultType.ERROR -> {
                    errorListener.invoke(result.typedResult().errorMessage ?: "")
                }
            }
        }
    }
}
