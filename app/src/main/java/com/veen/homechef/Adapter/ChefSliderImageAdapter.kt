package com.veen.homechef.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.veen.homechef.Activity.MainItemDetails
import com.veen.homechef.Model.HomePage.menu.FoodImage
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils

class ChefSliderImageAdapter(
    internal val mContext: Context,
    var listOfPhotos: ArrayList<FoodImage>,
    var id: String,
    var name: String,
    var supportFragment: FragmentManager
) : PagerAdapter() {


    override fun getCount(): Int {
        return listOfPhotos.size
    }

    internal var mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater =
            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun isViewFromObject(@NonNull view: View, @NonNull `object`: Any): Boolean {
        return view === `object`
    }

    @SuppressLint("CutPasteId")
    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        val itemView =
            mLayoutInflater.inflate(R.layout.item_list9, container, false)

        val ivPhoto = itemView.findViewById<ImageView>(R.id.getimage)

        ivPhoto.setOnClickListener {
            val testchefid = id
            AppUtils.savechefID(mContext, testchefid)
            val bundle = Bundle()
            bundle.putString("chefName", name)
            val viewfragment = MainItemDetails()
            viewfragment.setArguments(bundle)
            ChangeFragmemt(viewfragment, supportFragment, true)
        }

//        Log.d("image===", listOfPhotos[position].food_image)
        Glide.with(mContext).load(listOfPhotos[position].food_image).thumbnail(0.01f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(ivPhoto)
        container.addView(itemView)

        itemView.setTag("myview" + position);


        return itemView
    }


    override fun destroyItem(@NonNull container: ViewGroup, position: Int, @NonNull `object`: Any) {
//        container.removeView(`object` as RelativeLayout)
    }


    override fun getItemPosition(@NonNull `object`: Any): Int {
        return POSITION_NONE
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