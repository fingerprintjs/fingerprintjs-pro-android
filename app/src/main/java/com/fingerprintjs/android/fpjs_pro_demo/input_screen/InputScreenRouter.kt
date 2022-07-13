package com.fingerprintjs.android.fpjs_pro_demo.input_screen


import com.fingerprintjs.android.fpjs_pro_demo.base.BaseRouter
import com.fingerprintjs.android.fpjs_pro_demo.dialogs.IdentificationRequestParams


interface InputScreenRouter: BaseRouter {
    fun openFingerprintResultScreen(identificationRequestParams: IdentificationRequestParams? = null)
}