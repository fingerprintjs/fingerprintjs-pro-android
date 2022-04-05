package com.fingerprintjs.android.fpjs_pro.device_id_providers


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.Settings
import com.fingerprintjs.android.fpjs_pro.tools.executeSafe


internal class AndroidIdProvider(
    private val contentResolver: ContentResolver
) {
    @SuppressLint("HardwareIds")
    fun getAndroidId(): String {
        return executeSafe({
            Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }, "")
    }
}