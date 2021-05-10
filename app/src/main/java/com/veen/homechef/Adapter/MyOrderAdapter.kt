package com.veen.homechef.Adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Activity.Rating
import com.veen.homechef.Model.Profile.myorder.MyOrderData
import com.veen.homechef.R
import com.veen.homechef.Activity.profile.ViewDetailsActivity
import com.veen.homechef.Model.chef.ChefReq
import com.veen.homechef.Model.chef.ChefRes
import com.veen.homechef.Utils.AppUtils
import kotlinx.android.synthetic.main.search_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrderAdapter(private val context: Context, private val data: List<MyOrderData>) :
    RecyclerView.Adapter<MyOrderAdapter.ItemViewAbout> () {
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderid = itemView.findViewById<TextView>(R.id.order_id)
        val orderdate = itemView.findViewById<TextView>(R.id.order_date)
        val ordercount = itemView.findViewById<TextView>(R.id.order_itemcount)
        val orderitem = itemView.findViewById<TextView>(R.id.order_item)
        val orderdetails = itemView.findViewById<TextView>(R.id.order_viewdetails)
        val rating = itemView.findViewById<TextView>(R.id.order_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        holder.orderid.text = "Order Id # " + HtmlCompat.fromHtml(data[position].order_id, HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.orderdate.text = HtmlCompat.fromHtml(data[position].created_at, HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.ordercount.text = HtmlCompat.fromHtml(data[position].no_of_item + " items", HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.orderitem.text = HtmlCompat.fromHtml(data[position].food_name, HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.orderdetails.setOnClickListener {
            context.startActivity(Intent(context, ViewDetailsActivity::class.java)
                .addFlags(FLAG_ACTIVITY_NEW_TASK)
                .putExtra("orderID", data[position].order_id)
                .putExtra("OrderDate", data[position].created_at))
        }
        holder.rating.setOnClickListener {
//            val getloginID = AppUtils.getsaveloginID(context).toInt()
//            val gettoken = AppUtils.getsavetoken(context)
//            val orderID = data[position].order_id
//            getChefDetails(orderID, gettoken)

            context.startActivity(Intent(context, Rating::class.java)
                .addFlags(FLAG_ACTIVITY_NEW_TASK)
                .putExtra("orderID", data[position].order_id))

        }
    }

    private fun getChefDetails(orderID: String, gettoken: String) {
        try {
            RetrofitInstance.instence?.chefID(gettoken, ChefReq(
                orderID.toInt()
            ))!!.enqueue(object : Callback<ChefRes> {
                override fun onResponse(call: Call<ChefRes>, response: Response<ChefRes>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ChefRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}