package com.fingerprintjs.android.fpjs_pro_demo.base


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.fpjs_pro_demo.BuildConfig
import com.fingerprintjs.android.fpjs_pro_demo.R
import com.fingerprintjs.android.fpjs_pro_demo.dialogs.IdentificationRequestParams
import com.fingerprintjs.android.fpjs_pro_demo.dialogs.IdentificationRequestSettingsDialog
import com.fingerprintjs.android.fpjs_pro_demo.persistence.ApplicationPreferences


abstract class BaseActivity<T : Parcelable>(
    private val layoutId: Int,
    private val parcelableStateKey: String
) : AppCompatActivity(), BaseRouter {

    protected lateinit var presenter: BasePresenter<T>

    protected val preferences by lazy {
        ApplicationPreferences.Factory(applicationContext).getInstance()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        supportActionBar?.apply {
            title = "${this.title} ${BuildConfig.VERSION_NAME}"
        }
        init(intent, restoreState(savedInstanceState) as? T)
    }

    override fun onDestroy() {
        presenter.detachView()
        presenter.detachRouter()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveState(outState)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_repository -> openLink(Uri.parse(GITHUB_REPO_URL))
        }
        return super.onOptionsItemSelected(item)
    }


    override fun openLink(webpage: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun openRequestSettingsDialog(
        identificationRequestParams: IdentificationRequestParams?,
        onSettingsAppliedListener: (IdentificationRequestParams) -> Unit
    ) {
        IdentificationRequestSettingsDialog(this, identificationRequestParams).show(onSettingsAppliedListener)
    }

    private fun saveState(outState: Bundle) {
        outState.putParcelable(parcelableStateKey, presenter.onSaveState())
    }

    private fun restoreState(savedInstanceState: Bundle?): Parcelable? {
        return savedInstanceState?.getParcelable(parcelableStateKey)
    }

    abstract fun init(intent: Intent, savedInstanceState: T?)
}

private const val GITHUB_REPO_URL = "https://github.com/fingerprintjs/fingerprintjs-pro-android"
