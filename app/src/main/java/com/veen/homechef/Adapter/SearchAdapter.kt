package com.veen.homechef.Adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.*
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Model.Cart.AddCart.AddCartRequest
import com.veen.homechef.Model.Cart.AddCart.AddCartResponse
import com.veen.homechef.Model.Search.SearchData
import com.veen.homechef.Model.Search.SearchPrice
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import com.veen.homechef.Utils.RecyclerViewClickListener
import com.veen.homechef.fragment.HomeFragment
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchAdapter(private val context: Context, private val data: List<SearchData>, private val searchfragment: HomeFragment) :
    RecyclerView.Adapter<SearchAdapter.ItemViewAbout> () {
    private var itemprice: String? = ""
    private var itemplate: String? = ""
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodname = itemView.findViewById<TextView>(R.id.search_foodname)
        val foodimage = itemView.findViewById<ImageView>(R.id.search_image)
        val addcart = itemView.findViewById<TextView>(R.id.search_addcart)
        val description = itemView.findViewById<TextView>(R.id.search_fooddescription)
        val price = itemView.findViewById<TextView>(R.id.search_amount)
        val selectamount = itemView.findViewById<TextView>(R.id.search_buyamount)
        val more = itemView.findViewById<TextView>(R.id.description_more)
        val des = itemView.findViewById<TextView>(R.id.des_view)
        val layout = itemView.findViewById<LinearLayout>(R.id.parttwo)
        val rellayout = itemView.findViewById<RelativeLayout>(R.id.partone)
        val close = itemView.findViewById<TextView>(R.id.des_close)
        val testamount = itemView.findViewById<TextView>(R.id.testamount)
        val testplate = itemView.findViewById<TextView>(R.id.testplate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list7, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        holder.foodname.text = HtmlCompat.fromHtml(
            data[position].food_name,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        holder.description.text = HtmlCompat.fromHtml(
            data[position].description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        if (holder.description.length() >= 15) {
            holder.more.visibility = View.VISIBLE
        }

        holder.more.setOnClickListener {
            holder.layout.visibility = View.GONE
            holder.rellayout.visibility = View.VISIBLE
            holder.des.text = HtmlCompat.fromHtml(
                data[position].description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }

        holder.selectamount.text = data[position].price[0].name + "-" + data[position].price[0].amount
        holder.price.text = data[position].currency + " " + data[position].price[0].amount
        holder.testamount.text = data[position].price[0].amount
        holder.testplate.text = data[position].price[0].plate_size_id
        val orderlist: SearchData = data[position]

        holder.selectamount.setOnClickListener {
            var orderStatusAdapter: SearchPriceAdapter? = null
            var manageorderlist: List<SearchPrice> = ArrayList()
            val dialog = this.let { Dialog(searchfragment.requireContext()) }
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialogue_country)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
            manageorderlist = orderlist.price
            orderStatusAdapter = SearchPriceAdapter(context, manageorderlist!!)
            orderStatusAdapter!!.notifyDataSetChanged()
            var textcountry: TextView? = null
            textcountry = dialog.findViewById(R.id.textView50_alert)
            textcountry.text = "Please Select Food Quantity"
            var countryRecyclerView: RecyclerView? = null
            countryRecyclerView = dialog.findViewById(R.id.rv_country_list)
            countryRecyclerView.layoutManager = LinearLayoutManager(context)
            countryRecyclerView.itemAnimator = DefaultItemAnimator()
            countryRecyclerView.adapter = orderStatusAdapter

            orderStatusAdapter!!.setRecyclerViewClickListener(object :
                RecyclerViewClickListener {
                override fun onClick(id: Int, position: Int) {
                    holder.selectamount.text = orderStatusAdapter!!.getItem(position).name + "-" + orderStatusAdapter!!.getItem(position).amount
                    itemprice = orderStatusAdapter!!.getItem(position).amount
                    itemplate = orderStatusAdapter!!.getItem(position).plate_size_id
                    holder.price.text = data[0].currency + " " + itemprice

                    dialog.dismiss()
                }
            })

            val window = dialog.getWindow()
            if (window != null) {
                window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
            }
        }

        holder.close.setOnClickListener {
            holder.layout.visibility = View.VISIBLE
            holder.rellayout.visibility = View.GONE
        }

        Picasso.get().load(data[position].image).into(holder.foodimage)


        holder.addcart.setOnClickListener {
            try {
                if (itemprice == "") {
                    var gettoken = AppUtils.getsavetoken(context)
                    var getsaveloginID = AppUtils.getsaveloginID(context).toInt()
                    var getsavechefID = AppUtils.getsavechefID(context).toInt()
                    var itemid = data[position].id.toInt()

                    RetrofitInstance.instence?.addtocart(
                        gettoken, AddCartRequest(
                            getsavechefID,
                            itemid,
                            holder.testplate.text.toString(),
                            holder.testamount.text.toString(),
                            "1",
                            getsaveloginID
                        )
                    )!!.enqueue(object : Callback<AddCartResponse> {
                        override fun onResponse(
                            call: Call<AddCartResponse>,
                            response: Response<AddCartResponse>
                        ) {
                            if (response.body()!!.status == true) {
                                Toast.makeText(context, "Item added.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Item already added.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<AddCartResponse>, t: Throwable) {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    var gettoken = AppUtils.getsavetoken(context)
                    var getsaveloginID = AppUtils.getsaveloginID(context).toInt()
                    var getsavechefID = AppUtils.getsavechefID(context).toInt()
                    var itemid = data[position].id.toInt()

                    RetrofitInstance.instence?.addtocart(
                        gettoken, AddCartRequest(
                            getsavechefID,
                            itemid,
                            itemplate!!,
                            itemprice!!,
                            "1",
                            getsaveloginID
                        )
                    )!!.enqueue(object : Callback<AddCartResponse> {
                        override fun onResponse(
                            call: Call<AddCartResponse>,
                            response: Response<AddCartResponse>
                        ) {
                            if (response.body()!!.status == true) {
                                Toast.makeText(context, "Item added.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Item already added.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<AddCartResponse>, t: Throwable) {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    })}
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}