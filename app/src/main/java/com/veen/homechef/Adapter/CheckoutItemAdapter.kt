package com.veen.homechef.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.veen.homechef.Model.itemdetails.CheckoutItemItem
import com.veen.homechef.R

class CheckoutItemAdapter(private val context: Context, private val data: List<CheckoutItemItem>) :
    RecyclerView.Adapter<CheckoutItemAdapter.ItemViewAbout> () {
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemname = itemView.findViewById<TextView>(R.id.itemdetails_name)
        val itemview1 = itemView.findViewById<TextView>(R.id.itemdetails_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list8, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        holder.itemname.text = HtmlCompat.fromHtml(data[position].food_name, HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.itemview1.text = data[position].qty + " X " + data[position].price + " = " + data[position].currency + " " + data[position].item_subtotal
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
