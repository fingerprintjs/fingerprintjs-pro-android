package com.fingerprintjs.android.fpjs_pro_demo.base


import android.os.Parcelable


abstract class BasePresenter<T : Parcelable> {
    abstract fun attachView(view: BaseView)
    abstract fun detachView()

    abstract fun attachRouter(router: BaseRouter)
    abstract fun detachRouter()

    abstract fun onSaveState(): T
}