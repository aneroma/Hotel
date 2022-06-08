package ie.wit.hotel.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ie.wit.hotel.databinding.ActivityHotelBinding
import ie.wit.hotel.models.HotelModel
import timber.log.Timber
import timber.log.Timber.i

class HotelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHotelBinding
    var hotel = HotelModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())

        i("Hotel Activity started...")

        binding.btnAdd.setOnClickListener() {
            hotel.title = binding.hotelTitle.text.toString()
            if (hotel.title.isNotEmpty()) {
                i("add Button Pressed: $hotel.title")
            } else {
                Snackbar
                    .make(it, "Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}