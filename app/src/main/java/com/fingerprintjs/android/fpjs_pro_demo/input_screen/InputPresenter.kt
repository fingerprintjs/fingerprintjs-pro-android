package com.fingerprintjs.android.fpjs_pro_demo.input_screen


import android.os.Parcelable
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro_demo.base.BasePresenter
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseRouter
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseView
import com.fingerprintjs.android.fpjs_pro_demo.dialogs.IdentificationRequestParams
import com.fingerprintjs.android.fpjs_pro_demo.persistence.ApplicationPreferences
import kotlinx.parcelize.Parcelize


@Parcelize
data class InputScreenState(
    val selectedRegion: Configuration.Region?,
    val endpointUrl: String?,
    val apiKey: String?,
    val isDefaultApiKeyUsed: Boolean,
    val isExtendedResult: Boolean,
    val identificationRequestParams: IdentificationRequestParams?
) : Parcelable

class InputPresenter(
    private val applicationPreferences: ApplicationPreferences,
    state: InputScreenState?
) : BasePresenter<InputScreenState>() {

    private var selectedRegion: Configuration.Region =
        state?.selectedRegion ?: applicationPreferences.getRegion()
    private var endpointUrl: String = state?.endpointUrl ?: applicationPreferences.getEndpointUrl()
    private var apiKey: String = state?.apiKey ?: applicationPreferences.getPublicApiKey()
    private var isDefaultApiKeyUsed: Boolean = state?.isDefaultApiKeyUsed ?: applicationPreferences.getIsDefaultApiKeyUsed()
    private var isExtendedResult: Boolean = state?.isExtendedResult ?: applicationPreferences.getExtendedResult()
    private var identificationRequestParams: IdentificationRequestParams? = state?.identificationRequestParams

    private var view: InputView? = null
    private var router: InputScreenRouter? = null

    override fun attachView(view: BaseView) {
        this.view = view as InputViewImpl
        subscribeToView()

        this.view?.apply {
            setRegionText(selectedRegion.name)
            setEndpointUrl(endpointUrl)
            setPublicApiKey(apiKey)
            setIsDefaultApiKeyUsed(isDefaultApiKeyUsed)
            setExtendedResult(isExtendedResult)
        }
    }

    override fun detachView() {
        view = null
    }

    override fun attachRouter(router: BaseRouter) {
        this.router = router as InputScreenRouter
    }

    override fun detachRouter() {
        this.router = null
    }

    override fun onSaveState(): InputScreenState {
        return InputScreenState(
            selectedRegion = selectedRegion,
            endpointUrl = view?.getEndpointUrl(),
            apiKey = view?.getPublicApiKey(),
            isDefaultApiKeyUsed = isDefaultApiKeyUsed,
            isExtendedResult = isExtendedResult,
            identificationRequestParams = identificationRequestParams,
        )
    }

    private fun subscribeToView() {
        this.view?.apply {
            setOnGetVisitorIdBtnClickedListener {
                view?.apply {
                    apiKey = getPublicApiKey()
                    endpointUrl = getEndpointUrl()
                    isDefaultApiKeyUsed = getIsDefaultApiKeyUsed()
                    isExtendedResult = getExtendedResult()
                }

                applicationPreferences.setPublicApiKey(apiKey)
                applicationPreferences.setEndpointUrl(endpointUrl)
                applicationPreferences.setRegion(selectedRegion)
                applicationPreferences.setIsDefaultApiKeyUsed(isDefaultApiKeyUsed)
                applicationPreferences.setExtendedResult(isExtendedResult)
                router?.openFingerprintResultScreen(identificationRequestParams)
            }

            setOnRequestSettingsClickedListener {
                router?.openRequestSettingsDialog(identificationRequestParams) {
                    handleRequestParams(it)
                }
            }
            setOnRegionSelectedListener {
                selectedRegion = it
                setEndpointUrl(it.endpointUrl)
                setRegionText(it.name)
            }
        }
    }

    private fun handleRequestParams(requestParams: IdentificationRequestParams) {
        identificationRequestParams = requestParams
    }
}
