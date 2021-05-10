package com.veen.homechef.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.veen.homechef.Model.Profile.viewdetails.ViewDetailsItemData
import com.veen.homechef.R
import com.squareup.picasso.Picasso

class ViewOrderAdapter(private val context: Context, private val data: List<ViewDetailsItemData>) :
    RecyclerView.Adapter<ViewOrderAdapter.ItemViewAbout> () {
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Vieworderfood = itemView.findViewById<TextView>(R.id.view_foodname)
        val Vieworderstatus = itemView.findViewById<TextView>(R.id.view_orderstatus)
        val Vieworderqty = itemView.findViewById<TextView>(R.id.view_qty)
        val Vieworderprice = itemView.findViewById<TextView>(R.id.view_price)
        val Vieworderimage = itemView.findViewById<ImageView>(R.id.view_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list1, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
       holder.Vieworderfood.text = HtmlCompat.fromHtml(data[position].food_name, HtmlCompat.FROM_HTML_MODE_LEGACY)
       holder.Vieworderstatus.text = HtmlCompat.fromHtml(data[position].order_status_type, HtmlCompat.FROM_HTML_MODE_LEGACY)
       holder.Vieworderqty.text = HtmlCompat.fromHtml("Qty : "+ data[position].qty, HtmlCompat.FROM_HTML_MODE_LEGACY)
       holder.Vieworderprice.text = HtmlCompat.fromHtml("Rs. " + data[position].price, HtmlCompat.FROM_HTML_MODE_LEGACY)
       Picasso.get().load(data[position].image).into(holder.Vieworderimage)

    }

    override fun getItemCount(): Int {
        return data.size
    }
}
