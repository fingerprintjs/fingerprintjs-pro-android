package com.fingerprintjs.android.fpjs_pro_demo.results_screen


import android.os.Parcelable
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FPJSProClient
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory
import com.fingerprintjs.android.fpjs_pro.transport.fetch_visitor_id_request.FetchVisitorIdResponse
import com.fingerprintjs.android.fpjs_pro_demo.base.BasePresenter
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseRouter
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseView
import kotlinx.parcelize.Parcelize


@Parcelize
data class ResultState(
    val visitorId: String?,
    val ipAddress: String?,
    val osInfo: String?,
    val latitude: Double?,
    val longitude: Double?
) : Parcelable


class ResultsPresenter(
    private val endpointUrl: String,
    private val apiToken: String,
    private val fpjsProFactory: FPJSProFactory,
    state: ResultState?
) : BasePresenter<ResultState>() {
    private var fpjsClient: FPJSProClient? = null

    private var visitorId = state?.visitorId
    private var ipAddress = state?.ipAddress
    private var osInfo = state?.osInfo
    private val latitude = state?.latitude
    private val longitude = state?.longitude

    private var view: ResultsView? = null
    private var router: ResultsRouter? = null

    override fun attachView(view: BaseView) {
        this.view = view as ResultsView
        subscribeToView()
        initFPJSClient()
        this.view?.showProgressBar()
        fpjsClient?.getVisitorId({
            handleId(it)
        },
            errorListener = {
                //TODO Handle error
            })
    }

    override fun detachView() {
        this.view = null
    }

    override fun attachRouter(router: BaseRouter) {
        this.router = router as ResultsRouter
    }

    override fun detachRouter() {
        this.router = null
    }

    override fun onSaveState(): ResultState {
        return ResultState(
            visitorId,
            ipAddress,
            osInfo,
            latitude,
            longitude
        )
    }

    private fun subscribeToView() {
        view?.apply {
            setOnTryAgainClickedListener {
                router?.refresh()
            }
        }
    }

    private fun initFPJSClient() {
        val configuration = Configuration(
            apiToken = apiToken,
            endpointUrl = endpointUrl
        )
        fpjsClient = fpjsProFactory.createInstance(
            configuration
        )
    }

    private fun handleId(idResponse: FetchVisitorIdResponse) {
        this.view?.apply {
            hideProgressBar()
            setVisitorId(idResponse.visitorId)
            setIpGeolocation(
                idResponse.ipAddress,
                idResponse.ipLocation?.latitude ?: 0.0,
                idResponse.ipLocation?.longitude ?: 0.0
            )
            setOsInformation("${idResponse.osName} ${idResponse.osVersion}")
        }
    }
}