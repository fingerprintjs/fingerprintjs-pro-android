package com.fingerprintjs.android.fpjs_pro_demo.persistence


import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro_demo.R


interface ApplicationPreferences {
    fun getEndpointUrl(): String
    fun getPublicApiKey(): String
    fun getExtendedResult(): Boolean
    fun getRegion(): Configuration.Region

    fun setEndpointUrl(endpointUrl: String): Boolean
    fun setPublicApiKey(apiToken: String): Boolean
    fun setExtendedResult(extendedResult: Boolean): Boolean
    fun setRegion(region: Configuration.Region)

    class Factory(private val context: Context) {

        @Volatile
        private var instance: ApplicationPreferences? = null

        fun getInstance(): ApplicationPreferences {
            if (instance == null) {
                synchronized(ApplicationPreferences::class) {
                    if (instance == null) {
                        instance = ApplicationPreferencesImpl(context)
                    }
                }
            }
            return instance!!
        }
    }
}

class ApplicationPreferencesImpl(context: Context) : ApplicationPreferences {

    private val preferences = createPreferences(context)

    private val defaultEndpointUrl = context.getString(R.string.default_endpoint_url)
    private val defaultPublicApiToken = ""
    private val defaultExtendedResultSupport = false
    private val defaultRegion = Configuration.Region.US

    private val API_KEY = context.getString(R.string.prefs_public_api_key)
    private val ENDPOINT_URL_KEY = context.getString(R.string.prefs_endpoint_url_key)
    private val EXTENDED_RESULT_SUPPORT_KEY = context.getString(R.string.prefs_extended_result_key)
    private val REGION_KEY = context.getString(R.string.prefs_region_key)

    override fun getEndpointUrl() =
        preferences.getString(ENDPOINT_URL_KEY, null) ?: defaultEndpointUrl

    override fun getPublicApiKey() = preferences.getString(API_KEY, null) ?: defaultPublicApiToken

    override fun getExtendedResult() =
        preferences.getBoolean(EXTENDED_RESULT_SUPPORT_KEY, defaultExtendedResultSupport)

    override fun getRegion(): Configuration.Region {
        return when (preferences.getString(REGION_KEY, defaultRegion.name)) {
            Configuration.Region.US.name -> Configuration.Region.US
            Configuration.Region.EU.name -> Configuration.Region.EU
            Configuration.Region.AP.name -> Configuration.Region.AP
            else -> defaultRegion
        }
    }

    override fun setEndpointUrl(endpointUrl: String) =
        preferences.edit().putString(ENDPOINT_URL_KEY, endpointUrl).commit()

    override fun setPublicApiKey(apiToken: String) =
        preferences.edit().putString(API_KEY, apiToken).commit()

    override fun setExtendedResult(extendedResult: Boolean) =
        preferences.edit().putBoolean(EXTENDED_RESULT_SUPPORT_KEY, extendedResult).commit()

    override fun setRegion(region: Configuration.Region) {
        preferences.edit().putString(REGION_KEY, region.name).apply()
    }

    private fun createPreferences(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            EncryptedSharedPreferences.create(
                context,
                PREFERENCES_FILENAME,
                MasterKey(context),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else {
            PreferenceManager.getDefaultSharedPreferences(context)
        }
}

private const val PREFERENCES_FILENAME = "fpjs_prefs"