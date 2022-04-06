package com.fingerprintjs.android.fpjs_pro_demo.results_screen


import android.os.Parcelable
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FingerprintJS
import com.fingerprintjs.android.fpjs_pro.FingerprintJSFactory
import com.fingerprintjs.android.fpjs_pro.FingerprintJSProResponse
import com.fingerprintjs.android.fpjs_pro_demo.base.BasePresenter
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseRouter
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseView
import com.fingerprintjs.android.fpjs_pro_demo.persistence.ApplicationPreferences
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
    private val fpjsProFactory: FingerprintJSFactory,
    private val applicationPreferences: ApplicationPreferences,
    state: ResultState?
) : BasePresenter<ResultState>() {
    private var fpjsClient: FingerprintJS? = null

    private var visitorId = state?.visitorId
    private var ipAddress = state?.ipAddress
    private var osInfo = state?.osInfo
    private var latitude = state?.latitude
    private var longitude = state?.longitude

    private var view: ResultsView? = null
    private var router: ResultsRouter? = null

    override fun attachView(view: BaseView) {
        this.view = view as ResultsView
        subscribeToView()
        initFPJSClient()

        if (visitorId != null) {
            updateView()
            return
        }

        this.view?.showProgressBar()
        fpjsClient?.getVisitorId({
            applicationPreferences.setEndpointUrl(endpointUrl)
            applicationPreferences.setPublicApiKey(apiToken)
            handleId(it)
            updateView()
        },
        errorListener = {
            this.view?.showError(it)
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
            endpointUrl = endpointUrl,
            extendedResponseFormat = true
        )
        fpjsClient = fpjsProFactory.createInstance(
            configuration
        )
    }

    private fun handleId(idResponse: FingerprintJSProResponse) {
        visitorId = idResponse.visitorId
        ipAddress = idResponse.ipAddress
        latitude = idResponse.ipLocation?.latitude ?: 0.0
        longitude = idResponse.ipLocation?.longitude ?: 0.0

        osInfo = "${idResponse.osName} ${idResponse.osVersion}"
    }

    private fun updateView() {
        this.view?.apply {
            hideProgressBar()
            setVisitorId(visitorId ?: "")
            setIpGeolocation(
                ipAddress ?: "",
                latitude ?: 0.0,
                longitude ?: 0.0
            )
            setOsInformation(osInfo ?: "")
        }
    }
}