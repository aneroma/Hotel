package ie.wit.hotel.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.hotel.R
import ie.wit.hotel.databinding.ActivityHotelListBinding
import ie.wit.hotel.databinding.CardHotelBinding

import ie.wit.hotel.main.MainApp
import ie.wit.hotel.models.HotelModel

class HotelListActivity : AppCompatActivity() {
    lateinit var app: MainApp
    private lateinit var binding: ActivityHotelListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = HotelAdapter(app.hotels)
    }
}
class HotelAdapter constructor(private var hotels: List<HotelModel>) :
    RecyclerView.Adapter<HotelAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {

        val binding = CardHotelBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hotel = hotels[holder.adapterPosition]
        holder.bind(hotel)
    }

    override fun getItemCount(): Int = hotels.size

    class MainHolder(private val binding : CardHotelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hotel: HotelModel) {
            binding.hotelTitle.text = hotel.title
            binding.description.text = hotel.description
        }
    }
}