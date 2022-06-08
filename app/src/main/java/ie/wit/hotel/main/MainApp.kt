package ie.wit.hotel.main

import android.app.Application
import com.github.ajalt.timberkt.Timber
import ie.wit.hotel.models.HotelModel
import timber.log.Timber.i

class MainApp : Application() {

    val hotels = ArrayList<HotelModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Hotel started")
      hotels.add(HotelModel("One", "About one..."))
        hotels.add(HotelModel("Two", "About two..."))
        hotels.add(HotelModel("Three", "About three..."))
    }
}