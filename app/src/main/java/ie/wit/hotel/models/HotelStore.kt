package ie.wit.hotel.models

interface HotelStore {
    fun findAll(): List<HotelModel>
    fun create(hotel: HotelModel)
    fun update(hotel: HotelModel)
}