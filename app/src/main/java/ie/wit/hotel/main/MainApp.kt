package ie.wit.hotel.main

import android.app.Application
import android.util.Log.i
import ie.wit.hotel.models.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MainApp : Application(), AnkoLogger {
    lateinit var users : UserStore

    lateinit var hotels: HotelStore

    lateinit var currentUser: UserModel

    override fun onCreate() {
        super.onCreate()
        //hotels = HotelMemStore()
        hotels = HotelJSONStore(applicationContext)


        info("App Started")
      //hotels.add(HotelModel("One", "About one..."))
        //hotels.add(HotelModel("Two", "About two..."))
        //hotels.add(HotelModel("Three", "About three..."))
    }
}