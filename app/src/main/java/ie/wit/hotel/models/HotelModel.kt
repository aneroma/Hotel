package ie.wit.hotel.models


import android.net.Uri
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity
data class HotelModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                      var fbId : String = "",
                      var name: String = "",
                      var description: String = "",
                      var image: String = "",
                      @Embedded var location : Location = Location(),
                      var user: Long = 0,
                      var visited: Boolean = false,
                      var visitedDate: String = "",
                      var favourite: Boolean = false,
                      var rating: Float = 0f
) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

