package ie.wit.hotel.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.hotel.R
import ie.wit.hotel.adapters.HotelAdapter
import ie.wit.hotel.adapters.HotelListener
import ie.wit.hotel.databinding.ActivityHotelListBinding


import ie.wit.hotel.main.MainApp
import ie.wit.hotel.models.HotelModel


class HotelListActivity : AppCompatActivity(), HotelListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityHotelListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = HotelAdapter(app.hotels.findAll(),this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hotel, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, HotelActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHotelClick(hotel: HotelModel) {
        val launcherIntent = Intent(this, HotelActivity::class.java)
        launcherIntent.putExtra("hotel_edit", hotel)
        startActivityForResult(launcherIntent,0)
    }
}

//class HotelAdapter constructor(private var hotels: List<HotelModel>) :
    //RecyclerView.Adapter<HotelAdapter.MainHolder>() {

   // override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {

       // val binding = CardHotelBinding
          //  .inflate(LayoutInflater.from(parent.context), parent, false)

       // return MainHolder(binding)
   // }

  //  override fun onBindViewHolder(holder: MainHolder, position: Int) {
      //  val hotel = hotels[holder.adapterPosition]
       // holder.bind(hotel)
    //}

    //override fun getItemCount(): Int = hotels.size

    //class MainHolder(private val binding : CardHotelBinding) :
       // RecyclerView.ViewHolder(binding.root) {

        //fun bind(hotel: HotelModel) {
           // binding.hotelTitle.text = hotel.title
            //binding.description.text = hotel.description
        //}

    //}
//}