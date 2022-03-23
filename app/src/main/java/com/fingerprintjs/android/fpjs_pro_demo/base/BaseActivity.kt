package com.fingerprintjs.android.fpjs_pro_demo.base


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity<T : Parcelable>(
    private val layoutId: Int,
    private val parcelableStateKey: String
) : AppCompatActivity(), BaseRouter {

    protected lateinit var presenter: BasePresenter<T>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
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

    override fun openLink(webpage: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun saveState(outState: Bundle) {
        outState.putParcelable(parcelableStateKey, presenter.onSaveState())
    }

    private fun restoreState(savedInstanceState: Bundle?): Parcelable? {
        return savedInstanceState?.getParcelable(parcelableStateKey)
    }

    abstract fun init(intent: Intent, savedInstanceState: T?)
}