package ie.wit.hotel.views.hotel

import android.annotation.SuppressLint
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.hotel.helpers.checkLocationPermissions
import ie.wit.hotel.helpers.createDefaultLocationRequest
import ie.wit.hotel.helpers.isPermissionGranted
import ie.wit.hotel.helpers.showImagePicker
import ie.wit.hotel.models.HotelModel
import ie.wit.hotel.models.Location
import ie.wit.hotel.views.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class HotelPresenter (view: BaseView) : BasePresenter(view) {

    var map: GoogleMap? = null

    var hotel = HotelModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false;

    var locationService: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(view)

    val locationRequest = createDefaultLocationRequest()

    val REQUEST_IMAGE_CAPTURE = 1

    init {
        if (view.intent.hasExtra("Hotel_edit")) {
            edit = true
            hotel= view.intent.extras?.getParcelable<HotelModel>("Hotel_edit")!!
            view.showHotel(hotel)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun doRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation)
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(hotel.location)
    }

    fun locationUpdate(location: Location) {
        hotel.location = location
        hotel.location.zoom = 15f
        map?.clear()
        val options = MarkerOptions().title(hotel.name).position(LatLng(hotel.location.lat, hotel.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hotel.location.lat, hotel.location.lng), hotel.location.zoom))
        view?.showLocation(hotel.location)
    }


    fun doAddOrSave(name: String, description: String, visitedDateL: String, visitedL: Boolean, favourite: Boolean, rating: Float) {
        hotel.name = name
        hotel.description = description
        hotel.visitedDate = visitedDateL
        hotel.visited = visitedL
        hotel.favourite = favourite
        hotel.rating = rating
        doAsync {
            if (edit) {
                app.hotels.update(hotel)
            } else {
                app.hotels.create(hotel)
            }
            uiThread {
                view?.finish()
            }
        }
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete() {
        app.hotels.delete(hotel)
        view?.finish()
    }

    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doSetLocation() {
        view?.navigateTo(
            VIEW.LOCATION,
            LOCATION_REQUEST,
            "location",
            Location(hotel.location.lat, hotel.location.lng, hotel.location.zoom)
        )

    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                hotel.image = data.data.toString()
                view?.showHotel(hotel)
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                hotel.location = location
                locationUpdate(location)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun shareHotel(){
        val details = "Checkout this Hotel" +
                "\nName: ${hotel.name}" +
                "\nDescription: ${hotel.description}" +
                "\nRating: ${hotel.rating}" +
                "\nLocation: ${hotel.location}"

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, details);
        view!!.startActivity(Intent.createChooser(shareIntent,"Share via"))
    }







}

