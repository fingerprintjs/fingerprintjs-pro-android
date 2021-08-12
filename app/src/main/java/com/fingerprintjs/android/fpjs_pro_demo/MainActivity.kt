package com.fingerprintjs.android.fpjs_pro_demo


import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.fpjs_pro.FPJSProClient
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory


class MainActivity : AppCompatActivity() {

    private lateinit var fpjsClient: FPJSProClient

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
            if (!validateInputs(endpointUrlInput, apiTokenInput)) {
                Toast.makeText(this, "Inputs are invalid", Toast.LENGTH_SHORT).show()
            } else {

                idTextView.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE

                fpjsClient = FPJSProFactory(
                    applicationContext
                ).create(
                    endpointUrlInput.text.toString(),
                    apiTokenInput.text.toString()
                )

                fpjsClient.getVisitorId {
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        idTextView.visibility = View.VISIBLE
                        idTextView.text = it
                    }
                }
            }
        }
    }

    private fun validateInputs(endpointUrlInput: EditText, apiTokenInput: EditText): Boolean {
        val urlIsValid = URLUtil.isValidUrl(endpointUrlInput.text.toString())
        return urlIsValid
    }
}