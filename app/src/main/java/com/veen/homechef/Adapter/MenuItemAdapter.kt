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
import com.squareup.picasso.Picasso
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Activity.MainItemDetails
import com.veen.homechef.Model.Cart.AddCart.AddCartRequest
import com.veen.homechef.Model.Cart.AddCart.AddCartResponse
import com.veen.homechef.Model.HomePage.menuitem.MenuItemData
import com.veen.homechef.Model.HomePage.menuitem.Price
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import com.veen.homechef.Utils.RecyclerViewClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

 class MenuItemAdapter(
     private val context: Context,
     private val data: List<MenuItemData>,
     private val mainItemDetails: MainItemDetails
 ) :
    RecyclerView.Adapter<MenuItemAdapter.ItemViewAbout> () {
     private var itemprice: String? = ""
     private var itemplate: String? = ""

    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodname = itemView.findViewById<TextView>(R.id.chef_foodname)
        val foodimage = itemView.findViewById<ImageView>(R.id.gift_image)
        val addcart = itemView.findViewById<TextView>(R.id.chef_addcart)
        val description = itemView.findViewById<TextView>(R.id.chef_fooddescription)
        val price = itemView.findViewById<TextView>(R.id.chef_amount)
        val selectamount = itemView.findViewById<TextView>(R.id.chef_buyamount)
        val more = itemView.findViewById<TextView>(R.id.description_more)
        val des = itemView.findViewById<TextView>(R.id.des_view)
        val layout = itemView.findViewById<LinearLayout>(R.id.parttwo)
        val rellayout = itemView.findViewById<RelativeLayout>(R.id.partone)
        val close = itemView.findViewById<TextView>(R.id.des_close)
        val testamount = itemView.findViewById<TextView>(R.id.testamount)
        val testplate = itemView.findViewById<TextView>(R.id.testplate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_list4, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) = try {
        holder.foodname.text = HtmlCompat.fromHtml(
            data[position].name,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        holder.description.text = HtmlCompat.fromHtml(
            data[position].description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        holder.selectamount.text = data[position].price[0].name + "-" + data[position].price[0].amount
        holder.price.text = data[position].currency + " " + data[position].price[0].amount
        holder.testamount.text = data[position].price[0].amount
        holder.testplate.text = data[position].price[0].plate_size_id

        if (holder.description.length() >= 30) {
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

        holder.close.setOnClickListener {
            holder.layout.visibility = View.VISIBLE
            holder.rellayout.visibility = View.GONE
        }

        Picasso.get().load(data[position].image).into(holder.foodimage)
        val orderlist: MenuItemData = data[position]

        holder.selectamount.setOnClickListener {
            var orderStatusAdapter: MenuPriceAdapter? = null
            var manageorderlist: List<Price> = ArrayList()
            val dialog = this.let { Dialog(context) }
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialogue_country)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
            manageorderlist = orderlist.price
            orderStatusAdapter = MenuPriceAdapter(context, manageorderlist!!)
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

        holder.addcart.setOnClickListener {
            val gettoken = AppUtils.getsavetoken(context)
            val getsaveloginID = AppUtils.getsaveloginID(context)
            val getsavechefID = AppUtils.getsavechefID(context).toInt()
            val itemid = data[position].id.toInt()
            val getsavePhoneUnique = AppUtils.getsavePhoneUnique(context)

            if (getsaveloginID!= "") {
                try {
                    if (itemprice == "") {
                        RetrofitInstance.instence?.addtocart(AddCartRequest(
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
                        RetrofitInstance.instence?.addtocart(AddCartRequest(
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
            } else {
                try {
                    if (itemprice == "") {
                        RetrofitInstance.instence?.addtocart(AddCartRequest(
                                getsavechefID,
                                itemid,
                                holder.testplate.text.toString(),
                                holder.testamount.text.toString(),
                                "1",
                                getsavePhoneUnique
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
                        RetrofitInstance.instence?.addtocart(AddCartRequest(
                                getsavechefID,
                                itemid,
                                itemplate!!,
                                itemprice!!,
                                "1",
                                getsavePhoneUnique
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

        /*holder.addcart.setOnClickListener {
            var image = data[position].image
            var amount = data[position].currency + " " + data[position].price[0].amount
            var size = holder.testplate.text.toString()
            var name = data[position].name
            var description = data[position].description
            var savecart = AddToCart(image, name,description,amount,size)

            CartDatabase(context).getNoteDao().addToCart(savecart)

            Toast.makeText(context, "Add Successful", Toast.LENGTH_SHORT).show()
        }*/


    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun getItemCount(): Int {
        return data.size
    }
}