package ie.wit.hotel.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.hotel.R
import ie.wit.hotel.databinding.ActivityHotelBinding
import ie.wit.hotel.helpers.showImagePicker
import ie.wit.hotel.main.MainApp
import ie.wit.hotel.models.HotelModel
import timber.log.Timber
import timber.log.Timber.i

class HotelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHotelBinding
    var hotel = HotelModel()

    lateinit var app : MainApp//? = null//reference to the MainApp object
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    val IMAGE_REQUEST = 1
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        //Timber.plant(Timber.DebugTree())
        i("Hotel Activity started...")
        app = application as MainApp//how it is initialised:

        if (intent.hasExtra("hotel_edit")) {
            edit = true
            hotel = intent.extras?.getParcelable("hotel_edit")!!
            binding.hotelTitle.setText(hotel.title)
            binding.description.setText(hotel.description)
            binding.btnAdd.setText(R.string.save_hotel)
            binding.hotelLocation.setOnClickListener {
                i ("Set Location Pressed")
            }
            binding.btnAdd.setText(R.string.save_hotel)
            Picasso.get()
                .load(hotel.image)
                .into(binding.hotelImage)
            if (hotel.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_hotel_image)
            }
        }



        binding.btnAdd.setOnClickListener() {
            hotel.title = binding.hotelTitle.text.toString()
            hotel.description = binding.description.text.toString()
            if (hotel.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_hotel_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.hotels.update(hotel.copy())
                } else {

                    app.hotels.create(hotel.copy())
                }
            }
             i("add Button Pressed: ${hotel}")
            //for (i in app.hotels.indices) {
            // i("Placemark[$i]:${this.app.hotels[i]}")
            //}
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
        binding.hotelLocation.setOnClickListener {
            val launcherIntent = Intent(this, MapActivity::class.java)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }


        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.menu_hotel, menu)
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.item_cancel -> {
                    finish()
                }
            }
            return super.onOptionsItemSelected(item)
        }
    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            hotel.image = result.data!!.data!!
                            Picasso.get()
                                .load(hotel.image)
                                .into(binding.hotelImage)
                            binding.chooseImage.setText(R.string.change_hotel_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
       }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { i("Map Loaded") }
    }
    }

