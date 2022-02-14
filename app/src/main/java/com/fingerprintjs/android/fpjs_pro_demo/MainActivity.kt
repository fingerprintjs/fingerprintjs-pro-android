package com.fingerprintjs.android.fpjs_pro_demo


import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.fpjs_pro.Configuration
import com.fingerprintjs.android.fpjs_pro.FPJSProClient
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory
import com.fingerprintjs.android.fpjs_pro.kotlin_client.FetchVisitorIdResponse
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.preference.PreferenceManager
import org.osmdroid.tileprovider.tilesource.TileSourceFactory


class MainActivity : AppCompatActivity() {

    private var fpjsClient: FPJSProClient? = null

    private lateinit var idContainer: View
    private lateinit var idTextView: TextView
    private lateinit var ipTextView: TextView
    private lateinit var osTextView: TextView

    private lateinit var mapView: MapView

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
        idContainer = findViewById(R.id.id_response_container)

        idTextView = findViewById(R.id.id_text_view)

        ipTextView = findViewById(R.id.ip_text_view)
        osTextView = findViewById(R.id.os_text_view)

        mapView = findViewById(R.id.map_view)
        val ctx: Context = applicationContext
        org.osmdroid.config.Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        org.osmdroid.config.Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        mapView.setTileSource(TileSourceFactory.MAPNIK)


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
            idContainer.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE

            fpjsClient?.getVisitorId(
                listener = { visitorId ->
                    handleId(visitorId)
                },
                errorListener = { error ->
                    showError(error)
                })
        }
    }

    private fun initFPJSClient() {
        val configuration = Configuration(
            apiToken = "YOUR_BROWSER_TOKEN",
            endpointUrl = "https://ap.api.fpjs.io/"
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

    private fun showError(error: String) {
        runOnUiThread {
            showMessage(error)
            progressBar.visibility = View.GONE
            idContainer.visibility = View.VISIBLE
            clearFPJSClient()
        }
    }

    private fun handleId(idResponse: FetchVisitorIdResponse) {
        runOnUiThread {
            progressBar.visibility = View.GONE
            idContainer.visibility = View.VISIBLE
            idTextView.text = idResponse.visitorId
            ipTextView.text = idResponse.ipAddress
            osTextView.text = "${idResponse.osName} ${idResponse.osVersion}"

            val marker = Marker(mapView)
            val point = GeoPoint(idResponse.ipLocation?.latitude ?: 0.0,
                idResponse.ipLocation?.latitude ?: 0.0)
            marker.position = point
            mapView.controller.setCenter(point)

            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            mapView.overlays.add(marker)

            mapView.invalidate()

            clearFPJSClient()
        }
    }

    private fun validateInputs(endpointUrlInput: EditText) =
        (URLUtil.isValidUrl(endpointUrlInput.text.toString()) or endpointUrlInput.text.toString()
            .isEmpty()) && (apiTokenInput.text.toString().isNotEmpty())

    private fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
