package ie.wit.hotel.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.hotel.models.HotelModel
import ie.wit.hotel.views.BasePresenter
import ie.wit.hotel.views.BaseView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class HotelMapPresenter(view: BaseView) : BasePresenter(view) {

    fun doPopulateMap(map: GoogleMap, hotels: List<HotelModel>) {
        map.uiSettings.setZoomControlsEnabled(true)
        hotels.forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }

    fun doMarkerSelected(marker: Marker) {
        doAsync {
            val hotel = marker.tag as HotelModel
            uiThread {
                if (hotel != null) view?.showHotel(hotel)
            }
        }
    }

    fun loadHotels() {
        doAsync {
            val hotels = app.hotels.findAll()
            uiThread {
                view?.showHotels(hotels)
            }
        }
    }

}

