package ie.wit.hotel.main

import android.app.Application
import ie.wit.hotel.models.HotelJSONStore
import ie.wit.hotel.models.HotelMemStore
import ie.wit.hotel.models.HotelModel
import ie.wit.hotel.models.HotelStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var hotels: HotelStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //hotels = HotelMemStore()
        hotels = HotelJSONStore(applicationContext)


        i("Hotel started")
      //hotels.add(HotelModel("One", "About one..."))
        //hotels.add(HotelModel("Two", "About two..."))
        //hotels.add(HotelModel("Three", "About three..."))
    }
}