package com.app.pharmadawa.ui.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.veen.homechef.Model.location.city.CityData
import com.veen.homechef.R
import com.veen.homechef.Utils.RecyclerViewClickListener

class CityListAdapter(private val context: Context, private var cityList: ArrayList<CityData>): RecyclerView.Adapter<CityListAdapter.MyViewHolder>() {

    private lateinit var recyclerViewClickListener: RecyclerViewClickListener

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvCountryName = itemView.findViewById<TextView>(R.id.tv_country_name)
        init {
            tvCountryName.setOnClickListener {
                recyclerViewClickListener.onClick(R.id.tv_country_name, adapterPosition)
            }
        }
    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_country_list, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return cityList.size
    }

    //Bind View Holder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvCountryName.text = getItem(position).name
    }


    fun getItem(position: Int): CityData {
        return cityList[position]
    }


    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }

}