package com.fingerprintjs.android.fpjs_pro.tools


import java.lang.Exception


fun <T> executeSafe(code: () -> T, defaultValue: T): T {
    return try {
        code()
    } catch (exception: Exception) {
        defaultValue
    }
}