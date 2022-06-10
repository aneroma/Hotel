package ie.wit.hotel.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.hotel.databinding.CardHotelBinding
import ie.wit.hotel.models.HotelModel

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