package com.fingerprintjs.android.fpjs_pro_demo.input_screen


import android.content.Intent
import com.fingerprintjs.android.fpjs_pro_demo.R
import com.fingerprintjs.android.fpjs_pro_demo.REQUEST_PARAMS_KEY
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseActivity
import com.fingerprintjs.android.fpjs_pro_demo.dialogs.IdentificationRequestParams
import com.fingerprintjs.android.fpjs_pro_demo.results_screen.ResultsActivity


class InputScreenActivity :
    BaseActivity<InputScreenState>(R.layout.activity_input, INPUT_SCREEN_STATE_KEY),
    InputScreenRouter {

    override fun init(intent: Intent, savedInstanceState: InputScreenState?) {
        presenter = InputPresenter(preferences, savedInstanceState)
        presenter.attachView(InputViewImpl(this))
        presenter.attachRouter(this)
    }

    override fun openFingerprintResultScreen(identificationRequestParams: IdentificationRequestParams?) {
        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra(REQUEST_PARAMS_KEY, identificationRequestParams)
        startActivity(intent)
    }
}

private const val INPUT_SCREEN_STATE_KEY = "inputScreenState"