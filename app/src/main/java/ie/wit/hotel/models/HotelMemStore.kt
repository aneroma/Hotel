package ie.wit.hotel.models


import timber.log.Timber
import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class HotelMemStore : HotelStore {

    val hotels = ArrayList<HotelModel>()

    override fun findAll(): List<HotelModel> {
        return hotels
    }

    override fun create(hotel: HotelModel) {
        hotel.id = getId()
        hotels.add(hotel)
        logAll()
    }

    override fun update(hotel: HotelModel) {
        var foundHotel: HotelModel? = hotels.find { p -> p.id == hotel.id }
       if (foundHotel != null) {
            foundHotel.title = hotel.title
            foundHotel.description = hotel.description
           logAll()
        }
    }

    private fun logAll() {
       hotels.forEach { Timber.i("$it") }
    }
}