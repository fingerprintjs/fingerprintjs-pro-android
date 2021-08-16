package com.fingerprintjs.android.fpjs_pro_demo


import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FPJSProClient
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory


class MainActivity : AppCompatActivity() {

    private var fpjsClient: FPJSProClient? = null

    private lateinit var idTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var endpointUrlInput: EditText
    private lateinit var apiTokenInput: EditText
    private lateinit var getVisitorIdButton: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        subscribeToView()
    }

    private fun init() {
        idTextView = findViewById(R.id.id_text_view)
        progressBar = findViewById(R.id.progress_indicator)

        endpointUrlInput = findViewById(R.id.endpoint_url_input)
        apiTokenInput = findViewById(R.id.api_token_input)

        getVisitorIdButton = findViewById(R.id.get_visitor_id_button)
    }

    private fun subscribeToView() {
        getVisitorIdButton.setOnClickListener {
            getVisitorId()
        }
    }

    private fun getVisitorId() {
        if (!validateInputs(endpointUrlInput)) {
            showMessage("Inputs are invalid")
        } else {
            initFPJSClient()
            idTextView.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE

            fpjsClient?.getVisitorId {
                handleId(it)
            }
        }
    }

    private fun initFPJSClient() {
        val configuration = Configuration(
            apiTokenInput.text.toString(),
            endpointUrl = endpointUrlInput.text.toString()
        )
        fpjsClient = FPJSProFactory(
            applicationContext
        ).createInstance(
            configuration
        )
    }

    private fun clearFPJSClient() {
        fpjsClient = null
    }

    private fun handleId(id: String) {
        runOnUiThread {
            progressBar.visibility = View.GONE
            idTextView.visibility = View.VISIBLE
            idTextView.text = id
            clearFPJSClient()
        }
    }

    private fun validateInputs(endpointUrlInput: EditText) =
        (URLUtil.isValidUrl(endpointUrlInput.text.toString()) or endpointUrlInput.text.toString()
            .isEmpty()) && (apiTokenInput.text.toString().isNotEmpty())

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
