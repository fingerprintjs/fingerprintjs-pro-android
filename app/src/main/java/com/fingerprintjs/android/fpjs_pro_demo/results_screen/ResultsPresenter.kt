package com.fingerprintjs.android.fpjs_pro_demo.results_screen


import android.os.Parcelable
import com.fingerprintjs.android.fpjs_pro.*
import com.fingerprintjs.android.fpjs_pro_demo.BuildConfig
import com.fingerprintjs.android.fpjs_pro_demo.base.BasePresenter
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseRouter
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseView
import com.fingerprintjs.android.fpjs_pro_demo.dialogs.IdentificationRequestParams
import com.fingerprintjs.android.fpjs_pro_demo.persistence.ApplicationPreferences
import com.fingerprintjs.android.fpjs_pro_demo.toMap
import kotlinx.parcelize.Parcelize
import org.json.JSONException
import org.json.JSONObject


@Parcelize
data class ResultState(
    val visitorId: String?,
    val requestId: String?,
    val ipAddress: String?,
    val osInfo: String?,
    val latitude: Double?,
    val longitude: Double?,
    val firstSeen: String?,
    val lastSeen: String?,
    val identificationRequestParams: IdentificationRequestParams?
) : Parcelable


class ResultsPresenter(
    private val fpjsProFactory: FingerprintJSFactory,
    private val applicationPreferences: ApplicationPreferences,
    identificationRequestParams: IdentificationRequestParams?,
    state: ResultState?
) : BasePresenter<ResultState>() {
    private var fpjsClient: FingerprintJS? = null

    private var visitorId = state?.visitorId
    private var requestId = state?.requestId
    private var ipAddress = state?.ipAddress
    private var osInfo = state?.osInfo
    private var latitude = state?.latitude
    private var longitude = state?.longitude
    private var identificationRequestParams =
        identificationRequestParams ?: state?.identificationRequestParams

    private var firstSeen: String? = state?.firstSeen
    private var lastSeen: String? = state?.lastSeen

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

        var requestParamsMap = emptyMap<String, Any>()

        identificationRequestParams?.tags?.let {
            try {
                requestParamsMap = JSONObject(it).toMap()
            } catch (e: JSONException) {
                // Do nothing
            }
        }

        fpjsClient?.getVisitorId(
            tags = requestParamsMap,
            linkedId = identificationRequestParams?.linkedId ?: "",
            listener = { it ->
                handleId(it)
                updateView()
            },
            errorListener = { it -> this.view?.showError(it) }
        )
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
            requestId,
            ipAddress,
            osInfo,
            latitude,
            longitude,
            firstSeen,
            lastSeen,
            identificationRequestParams
        )
    }

    private fun subscribeToView() {
        view?.apply {
            setOnTryAgainClickedListener {
                hideTryAgainBtn()
                router?.refresh(identificationRequestParams)
            }
            setOnRequestSettingsClickedListener {
                router?.openRequestSettingsDialog(
                    identificationRequestParams = identificationRequestParams
                ) {
                    identificationRequestParams = it
                }
            }
            setOnCopyRequestIdClickedListener {
                router?.copyTextToBuffer(it)
            }
            setOnCopyVisitorIdClickedListener {
                router?.copyTextToBuffer(it)
            }
            setOnGoBackClickedListener {
                router?.goBack()
            }
        }
    }

    private fun initFPJSClient() {
        val apiKey = when(applicationPreferences.getIsDefaultApiKeyUsed()) {
            true -> BuildConfig.DEFAULT_API_KEY.orEmpty()
            false -> applicationPreferences.getPublicApiKey()
        }
        val endpointUrl = when(applicationPreferences.getIsDefaultApiKeyUsed()) {
            true -> BuildConfig.DEFAULT_ENDPOINT_URL.orEmpty()
            false -> applicationPreferences.getEndpointUrl()
        }
        val configuration = Configuration(
            apiKey = apiKey,
            endpointUrl = endpointUrl,
            extendedResponseFormat = applicationPreferences.getExtendedResult()
        )
        fpjsClient = fpjsProFactory.createInstance(
            configuration
        )
    }

    private fun handleId(idResponse: FingerprintJSProResponse) {
        visitorId = idResponse.visitorId
        requestId = idResponse.requestId
        ipAddress = idResponse.ipAddress
        latitude = idResponse.ipLocation?.latitude ?: 0.0
        longitude = idResponse.ipLocation?.longitude ?: 0.0

        osInfo = "${idResponse.osName} ${idResponse.osVersion}"
        firstSeen =
            "Global:\n${idResponse.firstSeenAt.global}\n\nSubscription:\n${idResponse.firstSeenAt.subscription}"
        lastSeen =
            "Global:\n${idResponse.lastSeenAt.global}\n\nSubscription:\n${idResponse.lastSeenAt.subscription}"
    }

    private fun updateView() {
        this.view?.apply {
            hideProgressBar()
            showTryAgainBtn()
            setVisitorId(visitorId ?: UNKNOWN)
            setRequestId(requestId ?: UNKNOWN)
            setIpGeolocation(
                ipAddress ?: UNKNOWN,
                latitude ?: 0.0,
                longitude ?: 0.0
            )
            setOsInformation(osInfo ?: UNKNOWN)
            setTimestamps(firstSeen ?: UNKNOWN, lastSeen ?: UNKNOWN)
        }
    }
}

private const val UNKNOWN = "N\\A"