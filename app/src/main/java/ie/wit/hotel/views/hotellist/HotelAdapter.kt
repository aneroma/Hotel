package ie.wit.hotel.views.hotellist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ie.wit.hotel.R
import ie.wit.hotel.models.HotelModel
import kotlinx.android.synthetic.main.card_hotel.view.*

interface HotelListener {
    fun onHotelClick(hotel: HotelModel)
}

class HotelAdapter constructor(private var hotels: List<HotelModel>,
                                  private val listener : HotelListener
) :
    RecyclerView.Adapter<HotelAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_hotel,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hotel = hotels[holder.adapterPosition]
        holder.bind(hotel, listener)

    }

    override fun getItemCount(): Int = hotels.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hotel: HotelModel, listener : HotelListener) {

            itemView.hotelCName.text = hotel.name
            itemView.hotelCDescription.text = hotel.description
            itemView.hotelCVisited.text = hotel.visitedDate
            Glide.with(itemView.context).load(hotel.image).into(itemView.imageC);
            itemView.setOnClickListener { listener.onHotelClick(hotel) }
        }
    }
}