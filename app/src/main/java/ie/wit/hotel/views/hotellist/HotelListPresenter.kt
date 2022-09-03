package ie.wit.hotel.views.hotellist


import com.google.firebase.auth.FirebaseAuth
import ie.wit.hotel.models.HotelModel
import ie.wit.hotel.views.BasePresenter
import ie.wit.hotel.views.BaseView
import ie.wit.hotel.views.VIEW
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class HotelListPresenter(view: BaseView) : BasePresenter(view) {

    fun doAddHotel() {
        view?.navigateTo(VIEW.HOTEL)
    }


    fun doEditHotel(hotel: HotelModel) {
        view?.navigateTo(VIEW.HOTEL, 0, "hotel_edit", hotel)
    }

    fun doShowHotelsMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    /*
    Load hotels based on what the switch icon being selected or not
    if true show hotels which are favourites
    If false show all hotelss
     */
    fun loadHotels(checked: Boolean, fav: Boolean) {
        doAsync {
            val hotels = app.hotels.findAll()
            uiThread {
                view?.showHotels(hotels)
            }
        }
    }


    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        view?.navigateTo(VIEW.LOGIN)
    }

    fun doSettings() {
        view?.navigateTo((VIEW.SETTINGS))
    }

    /*
    Load hotels based on the users search criteria
     */
    fun loadHotelsSearch(containingString: String)
    {
        view?.showHotels(app.hotels.findAll().filter { it.name.toLowerCase().contains(containingString.toLowerCase()) })
    }

    fun loadHotels(fav: Boolean) {

    }
}
