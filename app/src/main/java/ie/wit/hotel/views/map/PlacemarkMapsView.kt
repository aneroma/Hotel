package ie.wit.hotel.views.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import ie.wit.hotel.databinding.ActivityHotelMapsBinding
import ie.wit.hotel.databinding.ContentHotelMapsBinding
import ie.wit.hotel.main.MainApp
import ie.wit.hotel.models.HotelModel

class HotelMapView : AppCompatActivity() , GoogleMap.OnMarkerClickListener{

    private lateinit var binding: ActivityHotelMapsBinding
    private lateinit var contentBinding: ContentHotelMapsBinding
    lateinit var app: MainApp
    lateinit var presenter: HotelMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityHotelMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = HotelMapPresenter(this)

        contentBinding = ContentHotelMapsBinding.bind(binding.root)

        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync{
            presenter.doPopulateMap(it)
        }
    }
    fun showHotel(hotel: HotelModel) {
        contentBinding.currentTitle.text = hotel.title
        contentBinding.currentDescription.text = hotel.description
        Picasso.get()
            .load(hotel.image)
            .into(contentBinding.imageView2)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }


}