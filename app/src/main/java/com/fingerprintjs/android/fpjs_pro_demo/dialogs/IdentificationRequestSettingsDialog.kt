package com.fingerprintjs.android.fpjs_pro_demo.dialogs


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.fingerprintjs.android.fpjs_pro_demo.R
import com.fingerprintjs.android.fpjs_pro_demo.toMap
import kotlinx.parcelize.Parcelize
import org.json.JSONException
import org.json.JSONObject


@Parcelize
data class IdentificationRequestParams(
    val linkedId: String,
    val tags: String? = null
) : Parcelable

class IdentificationRequestSettingsDialog(
    private val activity: Activity,
    private val identificationRequestParams: IdentificationRequestParams?
) {
    private var dialog: AlertDialog? = null

    private val accentColor = ContextCompat.getColor(activity, R.color.orange)

    @SuppressLint("InflateParams")
    fun show(listener: (IdentificationRequestParams) -> (Unit)) {
        val builder = activity.let {
            AlertDialog.Builder(it)
        }

        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_request_params, null)

        val linkedIdEditText = view.findViewById<EditText>(R.id.edit_text_linked_id)
        val materialCheckBox = view.findViewById<CheckBox>(R.id.enable_tags_checkbox)
        val jsonEditText = view.findViewById<EditText>(R.id.tags_json_input)
        val jsonValidatorHint = view.findViewById<TextView>(R.id.json_validator_hint)
        val tagsDivider = view.findViewById<View>(R.id.tags_divider)

        materialCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val visibility = if (isChecked) View.VISIBLE else View.GONE
            jsonEditText.visibility = visibility
            jsonValidatorHint.visibility = visibility
            tagsDivider.visibility = visibility
        }

        identificationRequestParams?.apply {
            if (linkedId.isNotEmpty()) {
                linkedIdEditText.setText(linkedId)
            }
            if (tags != null && tags.isNotEmpty()) {
                materialCheckBox.isChecked = true
                jsonEditText.setText(tags)
            }
        }


        notifyTagsJsonState(jsonValidatorHint, validateJson(jsonEditText.text.toString()))

        jsonEditText.doOnTextChanged { text, start, before, count ->
            notifyTagsJsonState(jsonValidatorHint, validateJson(text.toString()))
        }

        dialog = builder
            .setTitle("Request settings")
            .setPositiveButton(
                "Apply"
            ) { _, _ ->
                linkedIdEditText.text.toString().let {
                    var tags: String? = null

                    val linkedId: String = linkedIdEditText.text.toString()

                    if (materialCheckBox.isChecked) {
                        if (validateJson(jsonEditText.text.toString())) {
                            tags = jsonEditText.text.toString()
                        } else {
                            notifyTagsJsonState(jsonValidatorHint, false)
                            return@setPositiveButton
                        }
                    }
                    listener.invoke(IdentificationRequestParams(linkedId, tags))
                }
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

    private fun dismiss() {
        dialog?.dismiss()
    }

    private fun validateJson(jsonString: String): Boolean {
        try {
            JSONObject(jsonString).toMap()
            return true
        } catch (exception: JSONException) {
            return false
        }
    }

    private fun notifyTagsJsonState(view: TextView, valid: Boolean) {
        val drawable = if (valid) {
            R.drawable.baseline_check_circle_24
        } else {
            R.drawable.baseline_highlight_off_24
        }
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, 0, 0, 0)
    }
}