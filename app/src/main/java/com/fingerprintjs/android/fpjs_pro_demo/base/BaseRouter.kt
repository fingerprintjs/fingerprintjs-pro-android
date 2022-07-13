package com.fingerprintjs.android.fpjs_pro_demo.base


import android.net.Uri
import com.fingerprintjs.android.fpjs_pro_demo.dialogs.IdentificationRequestParams


interface BaseRouter {
    fun openLink(webpage: Uri)
    fun openRequestSettingsDialog(identificationRequestParams: IdentificationRequestParams?, onSettingsAppliedListener: (IdentificationRequestParams) -> (Unit))
}