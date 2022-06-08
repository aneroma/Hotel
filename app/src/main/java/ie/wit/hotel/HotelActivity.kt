package ie.wit.hotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ie.wit.hotel.databinding.ActivityHotelBinding
import timber.log.Timber
import timber.log.Timber.i

class HotelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHotelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())

        i("Hotel Activity started...")

        binding.btnAdd.setOnClickListener() {
            val hotelTitle = binding.hotelTitle.text.toString()
            if (hotelTitle.isNotEmpty()) {
                i("add Button Pressed: $hotelTitle")
            } else {
                Snackbar
                    .make(it, "Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}