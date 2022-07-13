package com.fingerprintjs.android.fpjs_pro_demo.results_screen


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.fingerprintjs.android.fpjs_pro.FingerprintJSFactory
import com.fingerprintjs.android.fpjs_pro_demo.R
import com.fingerprintjs.android.fpjs_pro_demo.REQUEST_PARAMS_KEY
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseActivity
import com.fingerprintjs.android.fpjs_pro_demo.dialogs.IdentificationRequestParams
import com.fingerprintjs.android.fpjs_pro_demo.persistence.ApplicationPreferences


class ResultsActivity :
    BaseActivity<ResultState>(R.layout.activity_results, RESULT_SCREEN_STATE_KEY), ResultsRouter {

    override fun init(intent: Intent, savedInstanceState: ResultState?) {
        val identificationRequestParams = intent.getParcelableExtra<IdentificationRequestParams>(REQUEST_PARAMS_KEY)

        initPresenter(
            FingerprintJSFactory(applicationContext),
            preferences,
            identificationRequestParams,
            savedInstanceState
        )
    }

    override fun refresh(identificationRequestParams: IdentificationRequestParams?) {
        presenter.detachRouter()
        presenter.detachView()
        initPresenter(
            FingerprintJSFactory(applicationContext),
            preferences,
            identificationRequestParams,
            null
        )
    }

    override fun goBack() {
        super.onBackPressed()
    }

    override fun copyTextToBuffer(text: String) {
        val clipboard: ClipboardManager? =
            ContextCompat.getSystemService(this, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(this, "Copied!", Toast.LENGTH_LONG).show()
    }

    private fun initPresenter(
        factory: FingerprintJSFactory,
        applicationPreferences: ApplicationPreferences,
        identificationRequestParams: IdentificationRequestParams?,
        savedInstanceState: ResultState?
    ) {
        presenter = ResultsPresenter(
            factory,
            applicationPreferences,
            identificationRequestParams,
            savedInstanceState
        )
        presenter.attachRouter(this)
        presenter.attachView(ResultsViewImpl(this))
    }
}

private const val RESULT_SCREEN_STATE_KEY = "resultsScreenState"