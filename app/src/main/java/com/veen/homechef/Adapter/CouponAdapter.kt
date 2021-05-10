package com.veen.homechef.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Model.coupon.Fetch.FetchCouponData
import com.veen.homechef.Model.coupon.apply.ApplyCouponRequest
import com.veen.homechef.Model.coupon.apply.ApplyCouponResponse
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import com.veen.homechef.Utils.CouponListner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CouponAdapter(private val context: Context, private val pageRefresh: CouponListner, private val data: List<FetchCouponData>) :
    RecyclerView.Adapter<CouponAdapter.ItemViewAbout> () {
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coupon_code = itemView.findViewById<TextView>(R.id.coupon_code)
        val coupon_heading = itemView.findViewById<TextView>(R.id.coupon_heading)
        val coupon_paragraph = itemView.findViewById<TextView>(R.id.coupon_paragraph)
        val coupon_submit = itemView.findViewById<Button>(R.id.coupon_submit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.apply_coupon, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        try {
            holder.coupon_code.text =
                HtmlCompat.fromHtml(data[position].coupopn_code, HtmlCompat.FROM_HTML_MODE_LEGACY)
            holder.coupon_heading.text =
                HtmlCompat.fromHtml(data[position].title, HtmlCompat.FROM_HTML_MODE_LEGACY)
            holder.coupon_paragraph.text = "" + HtmlCompat.fromHtml(
                data[position].description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ) + " " + data[position].discount + " On Minimum Order "+ data[position].currency + data[position].minimum_price_range

            val getsaveloginID = AppUtils.getsaveloginID(context)
            val gettoken = AppUtils.getsavetoken(context)

            holder.coupon_submit.setOnClickListener {
                try {
                    RetrofitInstance.instence?.applycoupon(
                        gettoken, ApplyCouponRequest(
                            data[position].chef_id.toInt(),
                            data[position].id.toInt(),
                            getsaveloginID.toInt()
                        )
                    )!!.enqueue(object : Callback<ApplyCouponResponse> {
                        override fun onResponse(
                            call: Call<ApplyCouponResponse>,
                            response: Response<ApplyCouponResponse>
                        ) {
                            if (response.body()!!.status == true) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "" + response.body()!!.msg,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    pageRefresh.onCouponRefresh()
                                }
                            } else {
                                Toast.makeText(context, "" + response.body()!!.msg, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<ApplyCouponResponse>, t: Throwable) {
                            Log.d("TAG", "onFailure: Failed")
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}