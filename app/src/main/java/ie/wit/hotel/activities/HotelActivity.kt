package ie.wit.hotel.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ie.wit.hotel.databinding.ActivityHotelBinding
import ie.wit.hotel.main.MainApp
import ie.wit.hotel.models.HotelModel
import timber.log.Timber
import timber.log.Timber.i

class HotelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHotelBinding
    var hotel = HotelModel()
    var app : MainApp? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Timber.plant(Timber.DebugTree())

        //i("Hotel Activity started...")

        app = application as MainApp
        i("Hotel Activity started...")

        binding.btnAdd.setOnClickListener() {
            hotel.title = binding.hotelTitle.text.toString()
            hotel.description = binding.description.text.toString()
            if (hotel.title.isNotEmpty()) {
                //hotels.add(hotel)
                app!!.hotels.add(hotel.copy())
                i("add Button Pressed: ${hotel}")
                for (i in  app!!.hotels.indices)
                { i("Hotel[$i]:${this.app!!.hotels[i]}") }
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}