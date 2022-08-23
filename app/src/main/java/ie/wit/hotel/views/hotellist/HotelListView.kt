package ie.wit.hotel.views.hotellist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.hotel.R
import ie.wit.hotel.databinding.ActivityHotelListBinding
import ie.wit.hotel.main.MainApp
import ie.wit.hotel.models.HotelModel
import timber.log.Timber

class HotelListView : AppCompatActivity(), HotelListener {

    lateinit var app: MainApp
    lateinit var binding: ActivityHotelListBinding
    lateinit var presenter: HotelListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("Recycler View Loaded")
        super.onCreate(savedInstanceState)
        binding = ActivityHotelListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = HotelListPresenter(this)
        //app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter =
            HotelAdapter(presenter.getHotels(), this)

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        //update the view
        binding.recyclerView.adapter?.notifyDataSetChanged()
        Timber.i("recyclerView onResume")
        super.onResume()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddHotel() }
            R.id.item_map -> { presenter.doShowHotelsMap() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHotelClick(hotel: HotelModel) {
        presenter.doEditHotel(hotel)

    }

}