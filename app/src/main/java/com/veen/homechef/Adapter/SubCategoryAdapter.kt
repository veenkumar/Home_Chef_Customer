package com.veen.homechef.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.veen.homechef.Activity.MainItemDetails
import com.veen.homechef.Model.HomePage.subcategory.MenuSubData
import com.veen.homechef.R

class SubCategoryAdapter(private val context: Context, private val data: List<MenuSubData>, private val supportFragment: FragmentManager) :
    RecyclerView.Adapter<SubCategoryAdapter.ItemViewAbout> () {
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipe = itemView.findViewById<TextView>(R.id.chef_recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list6, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        holder.recipe.text = HtmlCompat.fromHtml(data[position].name, HtmlCompat.FROM_HTML_MODE_LEGACY)


        holder.recipe.setOnClickListener {
//            Toast.makeText(context, "" + data[position].food_subcategory_id, Toast.LENGTH_SHORT).show()

//            if (data[position].id != 0) {
                val bundle = Bundle()
                bundle.putString("foodsubID", data[position].id.toString())
                val viewfragment = MainItemDetails()
                viewfragment.setArguments(bundle)
                ChangeFragmemt(viewfragment, supportFragment, true)
//                context.startActivity(Intent(context, MainItemDetails::class.java)
//                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    .putExtra("foodsubID", data[position].id))
//            }
//            else if (data[position].food_subcategory_id != "") {
//                context.startActivity(Intent(context, MainItemDetails::class.java)
//                    .addFlags(FLAG_ACTIVITY_NEW_TASK)
//                    .putExtra("foodsubID", data[position].food_subcategory_id.toInt()))
//            }
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
