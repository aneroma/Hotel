package ie.wit.hotel.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.hotel.helpers.exists
import ie.wit.hotel.helpers.read
import ie.wit.hotel.helpers.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "hotels.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<HotelModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class HotelJSONStore(private val context: Context) : HotelStore {

    var hotels = mutableListOf<HotelModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<HotelModel> {
        logAll()
        return hotels
    }

    override fun create(hotel: HotelModel) {
       hotel.id = generateRandomId()
        hotels.add(hotel)
        serialize()
    }


    override fun update(hotel: HotelModel) {
        val hotelsList = findAll() as ArrayList<HotelModel>
        var foundHotel: HotelModel? = hotelsList.find { p -> p.id == hotel.id }
        if (foundHotel != null) {
            foundHotel.title = hotel.title
            foundHotel.description = hotel.description
            foundHotel.image = hotel.image
            foundHotel.lat = hotel.lat
            foundHotel.lng = hotel.lng
            foundHotel.zoom = hotel.zoom
        }
        serialize()
    }
    override fun delete(hotel:HotelModel) {
        hotels.remove(hotel)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hotels, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
       hotels = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        hotels.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>, JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}