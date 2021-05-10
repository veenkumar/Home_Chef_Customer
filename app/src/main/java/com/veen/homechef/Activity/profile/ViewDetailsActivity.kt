package com.veen.homechef.Activity.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Model.Profile.viewdetails.ViewDetailsItemData
import com.veen.homechef.Model.Profile.viewdetails.ViewDetailsRequest
import com.veen.homechef.Model.Profile.viewdetails.ViewDetailsResponse
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import com.veen.homechef.Adapter.ViewOrderAdapter
import kotlinx.android.synthetic.main.activity_view_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewDetailsActivity : AppCompatActivity() {
    private var vorder: TextView? = null
    private var vorderdate: TextView? = null
    private var vdeliveryadd: TextView? = null
    private var vdiscount: TextView? = null
    private var vshipping: TextView? = null
    private var vtotal: TextView? = null
    private var vsubtotal: TextView? = null
    private var adapter: ViewOrderAdapter? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_details)

        try {
            val getloginID = AppUtils.getsaveloginID(applicationContext).toInt()
            val gettoken = AppUtils.getsavetoken(applicationContext)
            val foodOrderID = intent.getStringExtra("orderID")!!.toInt()
            val orderdateID = intent.getStringExtra("OrderDate")


            recyclerView = findViewById(R.id.viewdetails_fooddetails)
            vorder = findViewById(R.id.viewdetails_order)
            vorderdate = findViewById(R.id.viewdetails_orderdate)
            vdeliveryadd = findViewById(R.id.viewdetails_deliveryaddress)
            vdiscount = findViewById(R.id.viewdetails_discount)
            vshipping = findViewById(R.id.viewdetails_shipping)
            vtotal = findViewById(R.id.viewdetails_total)
            vsubtotal = findViewById(R.id.viewdetails_subtotal)

            vorderdate?.text = "Order on : " + orderdateID

            RetrofitInstance.instence?.vieworderdetails(gettoken, ViewDetailsRequest(
                foodOrderID,
                getloginID
            ))!!.enqueue(object : Callback<ViewDetailsResponse> {
                override fun onResponse(
                    call: Call<ViewDetailsResponse>,
                    response: Response<ViewDetailsResponse>
                ) {
                    if (response.isSuccessful) {
                        viewdetailslayout.visibility = View.VISIBLE
                        viewdetails_refresh.visibility = View.GONE
                        vorder?.text = "Order no # " + response.body()!!.data.order_id
                        vsubtotal?.text = "Subtotal : " + response.body()!!.data.currency + " " + response.body()!!.data.sub_total_amount
                        vdeliveryadd?.text = "Delivery Address : " + response.body()!!.data.address
                        vdiscount?.text = "Discount Amount : " + response.body()!!.data.currency + " "+ response.body()!!.data.discount
                        vshipping?.text = "Delivery Charge : " + response.body()!!.data.currency + " " + response.body()!!.data.shipping_charge
                        vtotal?.text = "Total Amount : " + response.body()!!.data.currency + " " + response.body()!!.data.total_amount

                        bindvieworder(response.body()!!.data.item_data)
                    }
                }

                override fun onFailure(call: Call<ViewDetailsResponse>, t: Throwable) {
//                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindvieworder(list: List<ViewDetailsItemData>) {
        try {
            recyclerView.layoutManager =
                LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            adapter= ViewOrderAdapter(applicationContext, list)
            recyclerView.adapter=adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}