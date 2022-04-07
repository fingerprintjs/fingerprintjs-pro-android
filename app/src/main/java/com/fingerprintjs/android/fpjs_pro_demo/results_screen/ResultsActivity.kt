package com.fingerprintjs.android.fpjs_pro_demo.results_screen


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.fingerprintjs.android.fpjs_pro.FingerprintJSFactory
import com.fingerprintjs.android.fpjs_pro_demo.API_PUBLIC_KEY
import com.fingerprintjs.android.fpjs_pro_demo.ENDPOINT_URL_KEY
import com.fingerprintjs.android.fpjs_pro_demo.R
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseActivity
import com.fingerprintjs.android.fpjs_pro_demo.persistence.ApplicationPreferences
import com.fingerprintjs.android.fpjs_pro_demo.persistence.ApplicationPreferencesImpl


class ResultsActivity :
    BaseActivity<ResultState>(R.layout.activity_results, RESULT_SCREEN_STATE_KEY), ResultsRouter {

    private val preferences by lazy {
        ApplicationPreferencesImpl(applicationContext)
    }

    private var endpointUrl = ""
    private var apiKey = ""

    override fun init(intent: Intent, savedInstanceState: ResultState?) {
        apiKey = intent.getStringExtra(API_PUBLIC_KEY) ?: preferences.getEndpointUrl()
        endpointUrl = intent.getStringExtra(ENDPOINT_URL_KEY) ?: preferences.getPublicApiKey()
        initPresenter(
            endpointUrl,
            apiKey,
            FingerprintJSFactory(applicationContext),
            preferences,
            savedInstanceState
        )
    }

    override fun refresh() {
        presenter.detachRouter()
        presenter.detachView()
        initPresenter(
            endpointUrl,
            apiKey,
            FingerprintJSFactory(applicationContext),
            preferences,
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
        endpointUrl: String,
        apiKey: String,
        factory: FingerprintJSFactory,
        applicationPreferences: ApplicationPreferences,
        savedInstanceState: ResultState?
    ) {
        presenter = ResultsPresenter(
            endpointUrl,
            apiKey,
            factory,
            applicationPreferences,
            savedInstanceState
        )
        presenter.attachRouter(this)
        presenter.attachView(ResultsViewImpl(this))
    }
}

private const val RESULT_SCREEN_STATE_KEY = "resultsScreenState"