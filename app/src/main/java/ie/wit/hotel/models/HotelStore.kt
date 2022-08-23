package ie.wit.hotel.models

interface HotelStore {
    fun findAll(): List<HotelModel>
    fun create(hotel: HotelModel)
    fun update(hotel: HotelModel)
    fun delete(hotel: HotelModel)
    fun findById(id:Long) : HotelModel?
}