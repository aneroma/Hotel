package ie.wit.hotel.views.hotel

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import ie.wit.hotel.R
import ie.wit.hotel.models.HotelModel
import ie.wit.hotel.models.Location
import ie.wit.hotel.views.BaseView
import kotlinx.android.synthetic.main.activity_hotel.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HotelView : BaseView(), AnkoLogger {

    val calender = Calendar.getInstance()
    val year = calender.get(Calendar.YEAR)
    val month = calender.get(Calendar.MONTH)
    val day = calender.get(Calendar.DAY_OF_MONTH)

    var location = Location()
    lateinit var presenter: HotelPresenter
    var hotel = HotelModel()
    lateinit var map: GoogleMap

    val REQUEST_IMAGE_CAPTURE = 1

    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel)
        super.init(toolbar, true)

        presenter = HotelPresenter(this)

        mapView1.onCreate(savedInstanceState);
        mapView1.getMapAsync {
            presenter.doConfigureMap(it)
        }

        chooseImage.setOnClickListener {
            presenter.doSelectImage()
        }

        mapView1.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }

        toggleButton2.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                toggleButton2.setBackgroundResource(R.drawable.favourite)
            }else {
                toggleButton2.setBackgroundResource(R.drawable.heart)
            }
        }

        share.setOnClickListener{
            presenter.shareHotel()
        }

        visitedL.setOnCheckedChangeListener{_, isChecked ->
            hotel.visited = isChecked
        }


        visitedDateL.setOnClickListener {
            val datepicker = DatePickerDialog(
                this, R.style.DatePickerDialogTheme,
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    visitedDateL.setText("$day - $month - $year")}, year, month, day
            )
            datepicker.show()

        }

        takePic.setOnClickListener {
            takePic()
        }



    }

    override fun showHotel(hotel: HotelModel) {
        hotelName.setText(hotel.name)
        hotelDescription.setText(hotel.description)

        visitedL.isChecked = hotel.visited == true

        visitedDateL.setText(hotel.visitedDate)

        if(hotel.favourite == true){
            toggleButton2.setBackgroundResource(R.drawable.favourite)
            toggleButton2.isChecked = true
        }else if(hotel.favourite == false){
            toggleButton2.setBackgroundResource(R.drawable.heart)
            toggleButton2.isChecked = false
        }

        ratingBar.setRating(hotel.rating)

        Glide.with(this).load(hotel.image).into(hotelImage);

        if (hotel.image != null) {
            chooseImage.setText(R.string.change_hotel_image)
        }
        this.showLocation(hotel.location)
    }

    override fun showLocation(location: Location) {
        lat.setText("%.6f".format(location.lat))
        lng.setText("%.6f".format(location.lng))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hotel, menu)
        if (presenter.edit && menu != null) menu.getItem(1).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item?.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_save -> {
                if (hotelName.text.toString().isEmpty()) {
                    toast(R.string.enter_hotel_details)
                } else {
                    presenter.doAddOrSave(hotelName.text.toString(), hotelDescription.text.toString(), visitedDateL.text.toString(), visitedL.isChecked, toggleButton2.isChecked, ratingBar.getRating() )
                    info("Hotel created with the details - " +
                            "\nID: ${presenter.hotel.id}" +
                            "\nName: ${presenter.hotel.name}" +
                            "\nDesc: ${presenter.hotel.description}" +
                            "\nLocation: (Lat: ${location.lat}, Lng: ${location.lng})" +
                            "\nVisted: ${presenter.hotel.visited}" +
                            "\nDate Visited: ${presenter.hotel.visitedDate}" +
                            "\nFavourite: ${presenter.hotel.favourite}" +
                            "\nRating: ${presenter.hotel.rating}")
                }
            }
            R.id.home -> {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                onBackPressed()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }


    }

    override fun onBackPressed() {
        presenter.doCancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView1.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView1.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView1.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView1.onResume()
        presenter.doResartLocationUpdates()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView1.onSaveInstanceState(outState)
    }

    fun takePic() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {

                val photoFile: File? = try {
                    createImageFile()

                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "...",
                        it
                    )

                    val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }


    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

}
