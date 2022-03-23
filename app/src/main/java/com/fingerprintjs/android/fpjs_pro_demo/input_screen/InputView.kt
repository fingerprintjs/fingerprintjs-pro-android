package com.fingerprintjs.android.fpjs_pro_demo.input_screen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro_demo.R
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseActivity
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


interface InputView {
    fun setOnGetVisitorIdBtnClickedListener(listener: () -> (Unit))

    fun setOnRegionSelectedListener(selectedRegionListener: (Configuration.Region) -> (Unit))
    fun setRegionText(text: String)

    fun setEndpointUrl(url: String)
    fun getEndpointUrl(): String

    fun setPublicApiKey(key: String)
    fun getPublicApiKey(): String
}


class InputViewImpl(activity: BaseActivity<*>) : BaseView(activity), InputView {
    private val regionSelectButton: TextView = activity.findViewById(R.id.btn_region_select)

    private val endpointUrlInput: EditText = activity.findViewById(R.id.endpoint_url_input)
    private val apiKeyInput: EditText = activity.findViewById(R.id.api_key_input)
    private val getVisitorIdButton: View = activity.findViewById(R.id.get_visitor_id_button)
    private val fragmentManager = activity.supportFragmentManager

    override fun setOnGetVisitorIdBtnClickedListener(listener: () -> Unit) {
        getVisitorIdButton.setOnClickListener { listener.invoke() }
    }

    override fun setEndpointUrl(url: String) {
        endpointUrlInput.setText(url)
    }

    override fun getEndpointUrl() = endpointUrlInput.text.toString()

    override fun setPublicApiKey(key: String) {
        apiKeyInput.setText(key)
    }

    override fun getPublicApiKey() = apiKeyInput.text.toString()


    override fun setOnRegionSelectedListener(selectedRegionListener: (Configuration.Region) -> Unit) {
        regionSelectButton.setOnClickListener {
            RegionBottomSheet(selectedRegionListener).show(fragmentManager, RegionBottomSheet.TAG)
        }
    }

    override fun setRegionText(text: String) {
        regionSelectButton.text = text
    }
}


class RegionBottomSheet(
    private val selectedRegionListener: (Configuration.Region) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var usRegionButton: View
    private lateinit var euRegionButton: View
    private lateinit var apRegionButton: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_region_content, container, false)

        usRegionButton = view.findViewById(R.id.us_endpoint_btn)
        usRegionButton.setOnClickListener {
            selectedRegionListener.invoke(Configuration.Region.US)
            dismiss()
        }

        euRegionButton = view.findViewById(R.id.eu_endpoint_btn)
        euRegionButton.setOnClickListener {
            selectedRegionListener.invoke(Configuration.Region.EU)
            dismiss()
        }

        apRegionButton = view.findViewById(R.id.ap_endpoint_btn)
        apRegionButton.setOnClickListener {
            selectedRegionListener.invoke(Configuration.Region.AP)
            dismiss()
        }

        return view
    }


    companion object {
        const val TAG = "RegionBottomSheet"
    }
}