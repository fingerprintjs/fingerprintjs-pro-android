package com.fingerprintjs.android.fpjs_pro.tools


import java.lang.Exception


internal fun <T> executeSafe(code: () -> T, defaultValue: T): T {
    return try {
        code()
    } catch (exception: Exception) {
        defaultValue
    }
}