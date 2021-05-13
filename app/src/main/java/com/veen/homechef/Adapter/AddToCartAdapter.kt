package com.veen.homechef.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.veen.homechef.R
import com.veen.homechef.db.AddToCart
import com.veen.homechef.db.CartDatabase
import de.hdodenhof.circleimageview.CircleImageView

class AddToCartAdapter(
    private val context: Context,
    private val savedcart: List<AddToCart>
) :
    RecyclerView.Adapter<AddToCartAdapter.ItemViewAbout> () {
    private var database: AddToCart? = null
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartimage = itemView.findViewById<CircleImageView>(R.id.cart_image)
        val cartitem = itemView.findViewById<TextView>(R.id.cart_item)
        val cartprice = itemView.findViewById<TextView>(R.id.cart_price)
        val cartplate = itemView.findViewById<TextView>(R.id.cart_plate)
        val cartqty = itemView.findViewById<TextView>(R.id.cart_qty)
        val carttrash = itemView.findViewById<TextView>(R.id.cart_trash)
        val cartdelete = itemView.findViewById<TextView>(R.id.cart_delete)
        val cartadd = itemView.findViewById<TextView>(R.id.cart_add)
        val cartlayout = itemView.findViewById<LinearLayout>(R.id.cartlayout)
        val cartprogreebar = itemView.findViewById<ProgressBar>(R.id.cart_progressBar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        return ItemViewAbout(LayoutInflater.from(parent.context).inflate(R.layout.item_list5, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        Picasso.get().load(savedcart[position].image).into(holder.cartimage)
        holder.cartitem.text = savedcart[position].name
        holder.cartprice.text = savedcart[position].amount
        holder.cartqty.text = savedcart[position].size
        holder.cartplate.text = savedcart[position].size

        holder.carttrash.setOnClickListener {
            var id = savedcart[position].id
            CartDatabase(context.applicationContext).getNoteDao().deleteByTitle(id)
            Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show()
        }

        holder.cartadd.setOnClickListener {
            var id = savedcart[position].id
            var qty = holder.cartqty.text.toString().toInt() + 1
            holder.cartqty.text = qty.toString()

            CartDatabase(context.applicationContext).getNoteDao().updatenote(qty, id)
        }

        holder.cartdelete.setOnClickListener {
            var id = savedcart[position].id
            var qty = holder.cartqty.text.toString().toInt() - 1
            holder.cartqty.text = qty.toString()

            CartDatabase(context.applicationContext).getNoteDao().updatenote(qty, id)
        }
    }

    override fun getItemCount() = savedcart.size
}