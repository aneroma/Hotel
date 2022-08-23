package ie.wit.hotel.views.hotellist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.hotel.databinding.CardHotelBinding
import ie.wit.hotel.models.HotelModel

interface HotelListener {
    fun onHotelClick(hotel: HotelModel)
}

class HotelAdapter constructor(private var hotels: List<HotelModel>,
                                   private val listener: HotelListener) :
    RecyclerView.Adapter<HotelAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardHotelBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hotel = hotels[holder.adapterPosition]
        holder.bind(hotel, listener)
    }

    override fun getItemCount(): Int = hotels.size

    class MainHolder(private val binding : CardHotelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hotel: HotelModel, listener: HotelListener) {
            binding.hotelTitle.text = hotel.title
            binding.description.text = hotel.description
            Picasso.get()
                .load(hotel.image)
                .resize(200,200)
                .into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onHotelClick(hotel) }
        }
    }
}
