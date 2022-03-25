package com.fingerprintjs.android.fpjs_pro_demo.results_screen


import android.preference.PreferenceManager
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.fingerprintjs.android.fpjs_pro_demo.BuildConfig
import com.fingerprintjs.android.fpjs_pro_demo.R
import com.fingerprintjs.android.fpjs_pro_demo.base.BaseView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


interface ResultsView {
    fun setVisitorId(visitorId: String)
    fun setIpGeolocation(ipAddress: String, latitude: Double, longitude: Double)
    fun setOsInformation(osInfo: String)

    fun showProgressBar()
    fun hideProgressBar()

    fun showMessage(message: String)
    fun showError(message: String)

    fun setOnTryAgainClickedListener(listener: () -> (Unit))
}

class ResultsViewImpl(private val activity: ResultsActivity) : BaseView(activity), ResultsView {
    private val idContainer: View = activity.findViewById(R.id.id_response_container)
    private val progressBar: ProgressBar = activity.findViewById(R.id.progress_indicator)

    private val idTextView: TextView = activity.findViewById(R.id.id_text_view)
    private val ipTextView: TextView = activity.findViewById(R.id.ip_text_view)
    private val osTextView: TextView = activity.findViewById(R.id.os_text_view)
    private val tryAgainBtn: View = activity.findViewById(R.id.try_again_btn)

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
            idTextView.text = visitorId
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

    override fun showError(message: String) {
        activity.runOnUiThread {
            hideProgressBar()
            idTextView.text = message
        }
    }

    override fun showProgressBar() {
        activity.runOnUiThread {
            idContainer.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun hideProgressBar() {
        activity.runOnUiThread {
            progressBar.visibility = View.GONE
            idContainer.visibility = View.VISIBLE
        }
    }

    override fun setOnTryAgainClickedListener(listener: () -> Unit) {
        tryAgainBtn.setOnClickListener {
            listener.invoke()
        }
    }
}

private const val INITIAL_ZOOM = 10.0