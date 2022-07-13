package com.fingerprintjs.android.fpjs_pro_demo.results_screen


import com.fingerprintjs.android.fpjs_pro_demo.base.BaseRouter
import com.fingerprintjs.android.fpjs_pro_demo.dialogs.IdentificationRequestParams


interface ResultsRouter : BaseRouter {
    fun refresh(identificationRequestParams: IdentificationRequestParams? = null)
    fun goBack()
    fun copyTextToBuffer(text: String)
}