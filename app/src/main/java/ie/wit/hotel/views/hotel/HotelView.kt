package ie.wit.hotel.views.hotel

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.hotel.R
import ie.wit.hotel.databinding.ActivityHotelBinding
import ie.wit.hotel.models.HotelModel
import timber.log.Timber.i

class HotelView : AppCompatActivity() {

    private lateinit var binding: ActivityHotelBinding
    lateinit var presenter: HotelPresenter
    var hotel = HotelModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = HotelPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheHotel(binding.hotelTitle.text.toString(), binding.description.text.toString())
            presenter.doSelectImage()
        }

        binding.hotelLocation.setOnClickListener {
            presenter.cacheHotel(binding.hotelTitle.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hotel, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.hotelTitle.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.enter_hotel_title, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    presenter.doAddOrSave(binding.hotelTitle.text.toString(), binding.description.text.toString())
                }
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    fun showHotel(hotel: HotelModel) {
        binding.hotelTitle.setText(hotel.title)
        binding.description.setText(hotel.description)

        Picasso.get()
            .load(hotel.image)
            .into(binding.hotelImage)
        if (hotel.image != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_hotel_image)
        }

    }

    fun updateImage(image: Uri){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.hotelImage)
        binding.chooseImage.setText(R.string.change_hotel_image)
    }

    }

