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
import ie.wit.hotel.models.Location
import timber.log.Timber
import timber.log.Timber.i

class HotelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHotelBinding
    var hotel = HotelModel()
    lateinit var app : MainApp//? = null//reference to the MainApp object
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    //var location = Location(52.245696, -7.139102, 15f)

    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        edit = true

        binding = ActivityHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)


        app = application as MainApp//how it is initialised:

        i("Hotel Activity started...")

        if (intent.hasExtra("hotel_edit")) {
            edit = true
            hotel = intent.extras?.getParcelable("hotel_edit")!!
            binding.hotelTitle.setText(hotel.title)
            binding.description.setText(hotel.description)
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
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.hotelLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (hotel.zoom != 0f) {
                location.lat =  hotel.lat
                location.lng = hotel.lng
                location.zoom = hotel.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }


        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.menu_hotel, menu)
            if (edit && menu != null) menu.getItem(0).setVisible(true)
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.item_delete -> {
                    app.hotels.delete(hotel)
                    finish()
                }
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
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            hotel.lat = location.lat
                            hotel.lng = location.lng
                            hotel.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                    }
                }
            }
    }

