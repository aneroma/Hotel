package ie.wit.hotel.views.hotellist

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ie.wit.hotel.R
import ie.wit.hotel.models.HotelModel
import ie.wit.hotel.views.BaseView
import kotlinx.android.synthetic.main.activity_hotel_list.*


class HotelListView : BaseView(), HotelListener {

    lateinit var presenter: HotelListPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_list)
        super.init(toolbar, true)



        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener(bottomListener)

        presenter = initPresenter(HotelListPresenter(this)) as HotelListPresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.loadHotels(switch2.isChecked, false)


        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {

                presenter.loadHotelsSearch(newText!!)
                recyclerView.adapter?.notifyDataSetChanged()

                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                presenter.loadHotelsSearch(query!!)
                recyclerView.adapter?.notifyDataSetChanged()

                return true
            }

        })

        /*
        Change the toggle icon and set fav boolean when switch button is selected
         */
        switch2.setOnClickListener {
            if(switch2.isChecked){
                switch2.setThumbResource(R.drawable.favourite)
                presenter.loadHotels(fav = true)
                recyclerView.adapter?.notifyDataSetChanged()
            }
            else{
                switch2.setThumbResource(R.drawable.heart_border_white)
                presenter.loadHotels(fav = false)
                recyclerView.adapter?.notifyDataSetChanged()

            }

        }
    }

//    fun doOnOptionsItemSelected(item: MenuItem?) {
//        when (item?.itemId) {
//            R.id.undo_fav -> toast("ToDo")
//
//        }
//    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        /*
        Check the orientation and inform user that is has been changed
         */
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "Chnaged to landscape view", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "Changed to portrait view", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    Display ringforts
     */
    override fun showHotels(hotels: List<HotelModel>) {
        recyclerView.adapter = HotelAdapter(hotels, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)

    }

    /*
    When ringfort clicked invoke the edit ringfort method
     */
    override fun onHotelClick(hotel: HotelModel) {

        presenter.doEditHotel(hotel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        presenter.loadHotels(switch2.isChecked, false)
        super.onActivityResult(requestCode, resultCode, data)
    }

    /*
    Bottom navigation
     */
    private val bottomListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.logoutBottom ->  {
                presenter.doLogout()
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in)
            }
            R.id.item_add ->{
                presenter.doAddHotel()
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in)
            }
            R.id.settings_bottom -> {

                presenter.doSettings()
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
            R.id.item_map -> {
                presenter.doShowHotelsMap()
                overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
            }
        }
        false
    }



}

