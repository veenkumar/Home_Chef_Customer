package com.veen.homechef.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.veen.homechef.Model.HomePage.dashboard.DashboardResItem
import com.veen.homechef.R


class MySliderImageAdapter(
    internal var mContext: Context, var listOfPhotos: ArrayList<DashboardResItem>
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

        Glide.with(mContext).load(listOfPhotos[position].image).thumbnail(0.01f)
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
}