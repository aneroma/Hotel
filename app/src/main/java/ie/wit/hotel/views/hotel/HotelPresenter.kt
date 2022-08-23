package ie.wit.hotel.views.hotel

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.hotel.databinding.ActivityHotelBinding
import ie.wit.hotel.helpers.checkLocationPermissions
import ie.wit.hotel.helpers.showImagePicker
import ie.wit.hotel.main.MainApp
import ie.wit.hotel.models.HotelModel
import ie.wit.hotel.models.Location
import ie.wit.hotel.views.location.EditLocationView
import timber.log.Timber


class HotelPresenter(private val view: HotelView) {
    var map: GoogleMap? = null
    var hotel = HotelModel()
    var app: MainApp = view.application as MainApp
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false;
    private val location = Location(52.245696, -7.139102, 15f)

    init {

        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()


        if (view.intent.hasExtra("hotel_edit")) {
            edit = true
            hotel = view.intent.extras?.getParcelable("hotel_edit")!!
            view.showHotel(hotel)
        }
        else {

            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            hotel.lat = location.lat
            hotel.lng = location.lng
        }

    }

    fun doAddOrSave(title: String, description: String) {
        hotel.title = title
        hotel.description = description
        if (edit) {
            app.hotels.update(hotel)
        } else {
            app.hotels.create(hotel)
        }

        view.finish()

    }

    fun doCancel() {
        view.finish()

    }

    fun doDelete() {
        app.hotels.delete(hotel)
        view.finish()

    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {

        if (hotel.zoom != 0f) {
            location.lat = hotel.lat
            location.lng = hotel.lng
            location.zoom = hotel.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        Timber.i("setting location from doSetLocation")
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    fun cacheHotel(title: String, description: String) {
        hotel.title = title;
        hotel.description = description
    }
    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(hotel.lat, hotel.lng)
    }
    fun locationUpdate(lat: Double, lng: Double) {
        hotel.lat = lat
        hotel.lng = lng
        hotel.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hotel.title).position(LatLng(hotel.lat, hotel.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hotel.lat, hotel.lng), hotel.zoom))
        view.showHotel(hotel)
    }

    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            hotel.image = result.data!!.data!!
                            view.updateImage(hotel.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }

            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            hotel.lat = location.lat
                            hotel.lng = location.lng
                            hotel.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> {} else -> {}
                }

            }
    }
    private fun doPermissionLauncher() {
        Timber.i("permission check called")
        requestPermissionLauncher =
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }
}
