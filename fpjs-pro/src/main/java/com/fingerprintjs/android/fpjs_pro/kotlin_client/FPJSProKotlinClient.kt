package com.fingerprintjs.android.fpjs_pro.kotlin_client


import com.fingerprintjs.android.fpjs_pro.FPJSProClient
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
            interactor.getVisitorId(tags,
                {
                    listener.invoke(it)
                },
                {
                    errorListener.invoke(it)
                })
        }
    }
}
