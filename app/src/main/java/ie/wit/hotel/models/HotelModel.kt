package ie.wit.hotel.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HotelModel(var id: Long = 0,
                      var title: String = "",
                      var description: String = "") : Parcelable