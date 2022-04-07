package com.fingerprintjs.android.fpjs_pro_demo.results_screen


interface ResultsRouter {
    fun refresh()
    fun goBack()
    fun copyTextToBuffer(text: String)
}