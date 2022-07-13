package com.fingerprintjs.android.fpjs_pro_demo.dialogs


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.fingerprintjs.android.fpjs_pro_demo.R
import com.fingerprintjs.android.fpjs_pro_demo.persistence.ApplicationPreferences


class ClientSettingsDialog(
    private val activity: Activity,
    private val preferences: ApplicationPreferences
) {

    private var dialog: AlertDialog? = null

    private val accentColor = ContextCompat.getColor(activity, R.color.orange)

    @SuppressLint("InflateParams")
    fun showSettings() {
        val builder = activity.let {
            AlertDialog.Builder(it)
        }

        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_settings, null)

        val endpointUrlEditText = view.findViewById<EditText>(R.id.edit_text_endpoint_url)
        val publicApiKeyEditText = view.findViewById<EditText>(R.id.edit_text_public_api_key)
        val extendedResultCheckBox = view.findViewById<CheckBox>(R.id.checkbox_extended_result)

        endpointUrlEditText.text = SpannableStringBuilder(preferences.getEndpointUrl())
        publicApiKeyEditText.text = SpannableStringBuilder(preferences.getPublicApiKey())
        extendedResultCheckBox.isChecked = preferences.getExtendedResult()

        dialog = builder
            .setTitle("Client Settings")
            .setPositiveButton(
                "Apply"
            ) { _, _ ->
                applyPreferences(
                    endpointUrlEditText.text.toString(),
                    publicApiKeyEditText.text.toString(),
                    extendedResultCheckBox.isChecked
                )
                dismiss()
            }
            .setNegativeButton(
                "Cancel"
            ) { _, _ ->
                dismiss()
            }
            .setView(view)

            .create()

        dialog?.window?.decorView?.setBackgroundResource(R.drawable.bg_main)
        dialog?.show()

        val negativeButton = dialog?.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton?.setTextColor(accentColor)

        val positiveButton = dialog?.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton?.setTextColor(accentColor)
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    private fun applyPreferences(
        endpointUrl: String,
        apiToken: String,
        extendedResult: Boolean
    ) {
        preferences.setEndpointUrl(endpointUrl)
        preferences.setPublicApiKey(apiToken)
        preferences.setExtendedResult(extendedResult)
    }
}