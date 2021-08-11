package com.fingerprintjs.android.fpjs_pro_demo


import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val idTextView = findViewById<TextView>(R.id.id_text_view)
        val progressBar = findViewById<ProgressBar>(R.id.progress_indicator)

        val endpointUrlInput = findViewById<EditText>(R.id.endpoint_url_input)
        val apiTokenInput = findViewById<EditText>(R.id.api_token_input)

        val getVisitorIdButton = findViewById<FrameLayout>(R.id.get_visitor_id_button)

        getVisitorIdButton.setOnClickListener {
            if (!validateInputs(endpointUrlInput, apiTokenInput)) return@setOnClickListener

            val aspClient = FPJSProFactory(
                applicationContext
            ).getInstance(
                endpointUrlInput.text.toString(),
                apiTokenInput.text.toString()
            )

            progressBar.visibility = View.VISIBLE

            aspClient.getVisitorId {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    idTextView.visibility = View.VISIBLE
                    idTextView.text = it
                }
            }
        }
    }

    private fun validateInputs(endpointUrlInput: EditText, apiTokenInput: EditText): Boolean {
        return true
    }
}