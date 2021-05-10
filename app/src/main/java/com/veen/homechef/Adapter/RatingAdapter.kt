package com.veen.homechef.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.veen.homechef.Model.chef.ChefData
import com.veen.homechef.R
import com.veen.homechef.Utils.RatingInterface

class RatingAdapter(private val context: Context, private val data: List<ChefData>, private var ratingInterface: RatingInterface) :
    RecyclerView.Adapter<RatingAdapter.ItemViewAbout> () {
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.chef_name)
        val rating = itemView.findViewById<RatingBar>(R.id.ratingBar)
        val value = itemView.findViewById<TextView>(R.id.rating_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list10, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        holder.name.text = data[position].name

        holder.rating.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener {
                _, rating, _ ->
            holder.value.text = rating.toString()
            ratingInterface.getRating(rating.toString(), data[position].id.toInt())
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}