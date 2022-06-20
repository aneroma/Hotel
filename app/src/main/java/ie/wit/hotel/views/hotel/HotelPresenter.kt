package ie.wit.hotel.views.hotel

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ie.wit.hotel.databinding.ActivityHotelBinding
import ie.wit.hotel.helpers.showImagePicker
import ie.wit.hotel.main.MainApp
import ie.wit.hotel.models.HotelModel
import ie.wit.hotel.models.Location
import timber.log.Timber


class HotelPresenter(private val view: HotelView) {

    var hotel = HotelModel()
    var app: MainApp = view.application as MainApp
    var binding: ActivityHotelBinding = ActivityHotelBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    var edit = false;

    init {
        if (view.intent.hasExtra("hotel_edit")) {
            edit = true
            hotel = view.intent.extras?.getParcelable("hotel_edit")!!
            view.showHotel(hotel)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    fun doAddOrSave(title: String, description: String) {
        hotel.title = title
        hotel.description = description
        if (edit) {
            app.hotels.update(hotel)
        } else {
            app.hotels.create(hotel)
        }

        view.finish()

    }

    fun doCancel() {
        view.finish()

    }

    fun doDelete() {
        app.hotels.delete(hotel)
        view.finish()

    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (hotel.zoom != 0f) {
            location.lat = hotel.lat
            location.lng = hotel.lng
            location.zoom = hotel.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun cacheHotel(title: String, description: String) {
        hotel.title = title;
        hotel.description = description
    }

    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            hotel.image = result.data!!.data!!
                            view.updateImage(hotel.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }

            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            hotel.lat = location.lat
                            hotel.lng = location.lng
                            hotel.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                }               else -> {}
                }

            }

}
