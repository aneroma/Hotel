package ie.wit.hotel.views.hotellist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import ie.wit.hotel.main.MainApp
import ie.wit.hotel.models.HotelModel
import ie.wit.hotel.views.hotel.HotelView
import ie.wit.hotel.views.map.HotelMapView

class HotelListPresenter(val view: HotelListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var editIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerEditCallback()
        registerRefreshCallback()
    }

    fun getHotels() = app.hotels.findAll()

    fun doAddHotel() {
        val launcherIntent = Intent(view, HotelView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doEditHotel(hotel: HotelModel) {
        val launcherIntent = Intent(view, HotelView::class.java)
        launcherIntent.putExtra("hotel_edit", hotel)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doShowHotelsMap() {
        val launcherIntent = Intent(view, HotelMapView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { getHotels() }
    }
    private fun registerEditCallback() {
        editIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }

    }
}