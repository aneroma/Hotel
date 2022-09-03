package ie.wit.hotel.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.hotel.helpers.*
import java.util.*
import org.jetbrains.anko.AnkoLogger

val JSON_FILE = "hotels.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<HotelModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class HotelJSONStore: HotelStore,  AnkoLogger {

    val context: Context
    var hotels = mutableListOf<HotelModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<HotelModel> {
        return hotels
    }

    override fun create(hotel: HotelModel) {
        hotel.id = generateRandomId()
        hotels.add(hotel)
        serialize()
    }
    override fun findById(id:Long) : HotelModel? {
        val foundHotel: HotelModel? = hotels.find { it.id == id }
        return foundHotel
    }

    override fun update(hotel: HotelModel) {
        val hotelsList = findAll() as ArrayList<HotelModel>
        var foundHotel: HotelModel? = hotelsList.find { p -> p.id == hotel.id }
        if (foundHotel != null) {
            foundHotel.name = hotel.name
            foundHotel.description = hotel.description
            foundHotel.image = hotel.image
            foundHotel.location = hotel.location
            foundHotel.visited= hotel.visited
            foundHotel.visitedDate= hotel.visitedDate
            foundHotel.rating = hotel.rating
        }
        serialize()
    }
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hotels, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        hotels = Gson().fromJson(jsonString, listType)
    }
    override fun delete(hotel:HotelModel) {
        val foundHotel: HotelModel? = hotels.find { it.id == hotel.id }
        hotels.remove(foundHotel)
        serialize()
    }
    override fun clear() {
        hotels.clear()
    }
}