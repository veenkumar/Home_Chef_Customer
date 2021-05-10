package com.veen.homechef.Adapter

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.veen.homechef.Activity.MainItemDetails
import com.veen.homechef.Model.HomePage.menu.MenuData
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import com.squareup.picasso.Picasso
import java.util.*

class ImageAdapter(
    private val context: Context,
    private val data: List<MenuData>,
    private val supportFragment: FragmentManager) :
    RecyclerView.Adapter<ImageAdapter.ItemViewAbout> () {
    private var currentPage = 0
    private var NUM_PAGES = 0

    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dishimage = itemView.findViewById<ViewPager>(R.id.home_dishimage)
        val chefimage = itemView.findViewById<ImageView>(R.id.home_chefimage)
        val imageslayout = itemView.findViewById<RelativeLayout>(R.id.imageslayout)
        val chefname = itemView.findViewById<TextView>(R.id.chef_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list3, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        try {
            Picasso.get().load(data[position].chef_image).into(holder.chefimage)
//            Picasso.get().load(data[position].food_image[0].food_image).into(holder.dishimage)
            holder.chefname.text = data[position].name

            holder.imageslayout.setOnClickListener {
                val testchefid = data[position].id
                AppUtils.savechefID(context, testchefid)
                val bundle = Bundle()
                bundle.putString("chefName", data[position].name)
                val viewfragment = MainItemDetails()
                viewfragment.setArguments(bundle)
                ChangeFragmemt(viewfragment, supportFragment, true)
            }

//            for (i in 0 until data[position].food_image[position].food_image.length)
            for (i in 0..9){
                NUM_PAGES = data[position].food_image.size
                val adapter = ChefSliderImageAdapter(context, data[position].food_image, data[position].id, data[position].name, supportFragment)
                holder.dishimage.adapter = adapter
            }
            // Auto start of viewpager
            val handler = Handler()
            val Update = Runnable {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0
                }
                holder.dishimage.setCurrentItem(currentPage++, true)
            }
            val swipeTimer = Timer()
            swipeTimer.schedule(object : TimerTask() {
                override fun run() {
                    handler.post(Update)
                }
            }, 3000, 3000)
            val adapter = ChefSliderImageAdapter(context, data[position].food_image, data[position].id, data[position].name, supportFragment)
            holder.dishimage.adapter = adapter

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun ChangeFragmemt(fragment: Fragment?, fm: FragmentManager, addtobackstag: Boolean) {
        val tr = fm.beginTransaction()
        tr.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        if (addtobackstag) {
            tr.replace(R.id.nav_host_fragment, fragment!!).addToBackStack("a").commit()
        } else {
            tr.replace(R.id.nav_host_fragment, fragment!!).commit()
        }
    }


}
