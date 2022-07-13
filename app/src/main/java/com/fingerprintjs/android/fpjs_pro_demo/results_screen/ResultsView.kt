package com.fingerprintjs.android.fpjs_pro_demo.results_screen


import android.preference.PreferenceManager
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fingerprintjs.android.fpjs_pro.Error
import com.fingerprintjs.android.fpjs_pro_demo.BuildConfig
import com.fingerprintjs.android.fpjs_pro_demo.R
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


interface ResultsView {
    fun setVisitorId(visitorId: String)
    fun setRequestId(requestId: String)
    fun setIpGeolocation(ipAddress: String, latitude: Double, longitude: Double)
    fun setOsInformation(osInfo: String)

    fun showProgressBar()
    fun hideProgressBar()

    fun setOnRequestSettingsClickedListener(
        onClickListener: () -> (Unit)
    )

    fun showTryAgainBtn()
    fun hideTryAgainBtn()

    fun showMessage(message: String)
    fun showError(error: Error)

    fun setTimestamps(firstSeen: String, lastSeen: String)

    fun setOnCopyVisitorIdClickedListener(listener: (String) -> Unit)
    fun setOnCopyRequestIdClickedListener(listener: (String) -> Unit)
    fun setOnGoBackClickedListener(listener: () -> Unit)

    fun setOnTryAgainClickedListener(listener: () -> (Unit))
}

class ResultsViewImpl(private val activity: ResultsActivity) : BaseView(activity), ResultsView {
    private val idContainer: View = activity.findViewById(R.id.id_response_container)
    private val swipeRefreshLayout: SwipeRefreshLayout = activity.findViewById(R.id.swipe_refresh_layout)
    private val progressBar: ProgressBar = activity.findViewById(R.id.progress_indicator)

    private val visitorIdTextView: TextView = activity.findViewById(R.id.id_text_view)
    private val ipTextView: TextView = activity.findViewById(R.id.ip_text_view)
    private val osTextView: TextView = activity.findViewById(R.id.os_text_view)
    private val tryAgainBtnContainer: View = activity.findViewById(R.id.try_again_btn_container)
    private val tryAgainBtn: View = activity.findViewById(R.id.try_again_btn)
    private val openRequestSettingsDialogBtn: View =
        activity.findViewById(R.id.request_settings_btn)

    private val firstSeenTv: TextView = activity.findViewById(R.id.first_seen_at_text_view)
    private val lastSeenTv: TextView = activity.findViewById(R.id.last_seen_at_text_view)

    private val errorContainer: View = activity.findViewById(R.id.error_response_container)
    private val errorDescriptionTextView: TextView = activity.findViewById(R.id.error_description)
    private val requestIdTextView: TextView = activity.findViewById(R.id.request_id_view)
    private val errorRequestIdTextView: TextView = activity.findViewById(R.id.error_request_id_view)
    private var requestId: String = ""

    private val goBackBtn: View = activity.findViewById(R.id.go_back_button)

    private val mapView: MapView = activity.findViewById(R.id.map_view)

    init {
        org.osmdroid.config.Configuration.getInstance().load(
            activity.applicationContext,
            PreferenceManager.getDefaultSharedPreferences(activity.applicationContext)
        )
        org.osmdroid.config.Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }

    override fun setVisitorId(visitorId: String) {
        activity.runOnUiThread {
            visitorIdTextView.text = visitorId
        }
    }

    override fun setRequestId(requestId: String) {
        activity.runOnUiThread {
            val requestIdString = "Request ID: $requestId"
            requestIdTextView.text = requestIdString
            this.requestId = requestIdString
        }
    }

    override fun setIpGeolocation(ipAddress: String, latitude: Double, longitude: Double) {
        activity.runOnUiThread {
            ipTextView.text = ipAddress

            val marker = Marker(mapView)
            val point = GeoPoint(
                latitude,
                longitude
            )

            marker.position = point
            marker.icon = ContextCompat.getDrawable(activity, R.drawable.ic_point_on_map)
            mapView.setTileSource(TileSourceFactory.MAPNIK)

            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            mapView.overlays.add(marker)
            mapView.setMultiTouchControls(true)
            mapView.controller.setZoom(INITIAL_ZOOM)
            mapView.controller.setCenter(point)


            mapView.invalidate()
        }
    }

    override fun setOsInformation(osInfo: String) {
        activity.runOnUiThread {
            osTextView.text = osInfo
        }
    }

    override fun showMessage(message: String) {
        activity.runOnUiThread {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showTryAgainBtn() {
        activity.runOnUiThread {
            tryAgainBtnContainer.visibility = View.VISIBLE
        }
    }

    override fun hideTryAgainBtn() {
        activity.runOnUiThread {
            tryAgainBtnContainer.visibility = View.GONE
        }
    }

    override fun setOnRequestSettingsClickedListener(
        onClickListener: () -> (Unit)
    ) {
        activity.runOnUiThread {
            openRequestSettingsDialogBtn.setOnClickListener {
                onClickListener.invoke()
            }
        }
    }

    override fun showError(error: Error) {
        activity.runOnUiThread {
            hideProgressBar()
            idContainer.visibility = View.GONE
            swipeRefreshLayout.visibility = View.GONE
            errorContainer.visibility = View.VISIBLE

            errorDescriptionTextView.text = error.description
            val requestIdString = "Request ID: ${error.requestId}"
            this.requestId = error.requestId

            errorRequestIdTextView.text = requestIdString
        }
    }

    override fun setTimestamps(firstSeen: String, lastSeen: String) {
        activity.runOnUiThread {
            firstSeenTv.text = firstSeen
            lastSeenTv.text = lastSeen
        }
    }

    override fun setOnCopyVisitorIdClickedListener(listener: (String) -> Unit) {
        activity.runOnUiThread {
            visitorIdTextView.setOnClickListener {
                listener.invoke(visitorIdTextView.text.toString())
            }
        }
    }


    override fun setOnCopyRequestIdClickedListener(listener: (String) -> Unit) {
        activity.runOnUiThread {
            requestIdTextView.setOnClickListener {
                listener.invoke(this.requestId)
            }
        }
    }

    override fun setOnGoBackClickedListener(listener: () -> Unit) {
        activity.runOnUiThread {
            goBackBtn.setOnClickListener {
                listener.invoke()
            }
        }
    }

    override fun showProgressBar() {
        activity.runOnUiThread {
            idContainer.visibility = View.INVISIBLE
            swipeRefreshLayout.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun hideProgressBar() {
        activity.runOnUiThread {
            progressBar.visibility = View.GONE
            idContainer.visibility = View.VISIBLE
            swipeRefreshLayout.visibility = View.VISIBLE
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun setOnTryAgainClickedListener(listener: () -> Unit) {
        activity.runOnUiThread {
            swipeRefreshLayout.setOnRefreshListener {
                listener.invoke()
            }
            tryAgainBtn.setOnClickListener {
                listener.invoke()
            }
        }
    }
}

private const val INITIAL_ZOOM = 10.0
