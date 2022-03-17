package com.fingerprintjs.android.fpjs_pro_demo.input_screen


import android.os.Parcelable
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro_demo.base.BasePresenter
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseRouter
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseView
import kotlinx.parcelize.Parcelize


@Parcelize
data class InputScreenState(
    val selectedRegion: Configuration.Region?,
    val endpointUrl: String?,
    val apiKey: String?
) : Parcelable


class InputPresenter(
    state: InputScreenState?
) : BasePresenter<InputScreenState>() {

    private var selectedRegion: Configuration.Region =
        state?.selectedRegion ?: Configuration.Region.US
    private val endpointUrl: String? = state?.endpointUrl
    private val apiKey: String? = state?.apiKey

    private var view: InputView? = null
    private var router: InputScreenRouter? = null

    override fun attachView(view: BaseView) {
        this.view = view as InputViewImpl
        subscribeToView()

        this.view?.apply {
            setRegionText(selectedRegion.name)
            endpointUrl?.let {
                setEndpointUrl(it)
            }
            apiKey?.let {
                setPublicApiKey(it)
            }
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
            selectedRegion,
            view?.getEndpointUrl(),
            view?.getPublicApiKey()
        )
    }

    private fun subscribeToView() {
        this.view?.apply {
            setOnGetVisitorIdBtnClickedListener {
                router?.openFingerprintResultScreen(
                    Configuration(
                        view?.getPublicApiKey() ?: "",
                        selectedRegion,
                        view?.getEndpointUrl() ?: selectedRegion.endpointUrl
                    )
                )
            }
            setOnRegionSelectedListener {
                selectedRegion = it
                setEndpointUrl(it.endpointUrl)
                setRegionText(it.name)
            }
        }
    }
}