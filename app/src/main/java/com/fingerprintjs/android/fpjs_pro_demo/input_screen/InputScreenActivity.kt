package com.fingerprintjs.android.fpjs_pro_demo.input_screen


import android.content.Intent
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro_demo.API_PUBLIC_KEY
import com.fingerprintjs.android.fpjs_pro_demo.ENDPOINT_URL_KEY
import com.fingerprintjs.android.fpjs_pro_demo.results_screen.ResultsActivity
import com.fingerprintjs.android.fpjs_pro_demo.R
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseActivity


class InputScreenActivity : BaseActivity<InputScreenState>(R.layout.activity_input, INPUT_SCREEN_STATE_KEY), InputScreenRouter {
    override fun init(intent: Intent, savedInstanceState: InputScreenState?) {
        presenter = InputPresenter(savedInstanceState)
        presenter.attachView(InputViewImpl(this))
        presenter.attachRouter(this)
    }

    override fun openFingerprintResultScreen(configuration: Configuration) {
        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra(ENDPOINT_URL_KEY, configuration.endpointUrl)
        intent.putExtra(API_PUBLIC_KEY, configuration.apiToken)
        startActivity(intent)
    }
}

private const val INPUT_SCREEN_STATE_KEY = "inputScreenState"