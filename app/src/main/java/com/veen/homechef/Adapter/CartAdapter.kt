package com.veen.homechef.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Model.Cart.CartView.CartData
import com.veen.homechef.Model.Cart.delete.DeleteCartRequest
import com.veen.homechef.Model.Cart.delete.DeleteCartResponse
import com.veen.homechef.Model.Cart.update.CartUpdateRequest
import com.veen.homechef.Model.Cart.update.CartUpdateResponse
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import com.veen.homechef.Utils.CartListner
import com.squareup.picasso.Picasso
import com.veen.homechef.db.AddToCart
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartAdapter(
        private val context: Context,
        private val pagerefresh: CartListner,
        private val data: List<CartData>) :
    RecyclerView.Adapter<CartAdapter.ItemViewAbout> () {
    inner class ItemViewAbout(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartimage = itemView.findViewById<CircleImageView>(R.id.cart_image)
        val cartitem = itemView.findViewById<TextView>(R.id.cart_item)
        val cartprice = itemView.findViewById<TextView>(R.id.cart_price)
        val cartplate = itemView.findViewById<TextView>(R.id.cart_plate)
        val cartqty = itemView.findViewById<TextView>(R.id.cart_qty)
        val carttrash = itemView.findViewById<TextView>(R.id.cart_trash)
        val cartdelete = itemView.findViewById<TextView>(R.id.cart_delete)
        val cartadd = itemView.findViewById<TextView>(R.id.cart_add)
        val cartlayout = itemView.findViewById<LinearLayout>(R.id.cartlayout)
        val cartprogreebar = itemView.findViewById<ProgressBar>(R.id.cart_progressBar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewAbout {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list5, parent, false)
        return ItemViewAbout(view)
    }

    override fun onBindViewHolder(holder: ItemViewAbout, position: Int) {
        try {
            holder.cartlayout.visibility = View.VISIBLE
            holder.cartprogreebar.visibility = View.GONE

            Picasso.get().load(data[position].image).into(holder.cartimage)
            holder.cartitem.text = "" + HtmlCompat.fromHtml(
                data[position].food_name,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            holder.cartprice.text = data[position].currency + " " + HtmlCompat.fromHtml(
                data[position].price,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            holder.cartqty.text = HtmlCompat.fromHtml(
                data[position].qty,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            holder.cartplate.text = HtmlCompat.fromHtml(
                data[position].plate_size_name,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            var updateitemid = data[position].id.toInt()
            val getsaveloginID = AppUtils.getsaveloginID(context)
            val getsavePhoneUnique = AppUtils.getsavePhoneUnique(context)

            holder.cartadd.setOnClickListener {
                holder.cartlayout.visibility = View.GONE
                holder.cartprogreebar.visibility = View.VISIBLE
                var qty = data[position].qty.toInt() + 1

                if (getsaveloginID != "") {
                    try {
                        RetrofitInstance.instence?.updatecartview(CartUpdateRequest(
                            updateitemid,
                            qty,
                            getsaveloginID
                        )
                        )!!.enqueue(object : Callback<CartUpdateResponse> {
                            override fun onResponse(
                                call: Call<CartUpdateResponse>,
                                response: Response<CartUpdateResponse>
                            ) {
                                if (response.isSuccessful) {
                                    holder.cartlayout.visibility = View.VISIBLE
                                    holder.cartprogreebar.visibility = View.GONE
                                    Toast.makeText(context, "Item qty updated.", Toast.LENGTH_SHORT)
                                        .show()
                                    pagerefresh.onQuantityListner()
                                }
                            }

                            override fun onFailure(call: Call<CartUpdateResponse>, t: Throwable) {
                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        RetrofitInstance.instence?.updatecartview(CartUpdateRequest(
                            updateitemid,
                            qty,
                            getsavePhoneUnique
                        )
                        )!!.enqueue(object : Callback<CartUpdateResponse> {
                            override fun onResponse(
                                call: Call<CartUpdateResponse>,
                                response: Response<CartUpdateResponse>
                            ) {
                                if (response.isSuccessful) {
                                    holder.cartlayout.visibility = View.VISIBLE
                                    holder.cartprogreebar.visibility = View.GONE
                                    Toast.makeText(context, "Item qty updated.", Toast.LENGTH_SHORT)
                                        .show()
                                    pagerefresh.onQuantityListner()
                                }
                            }

                            override fun onFailure(call: Call<CartUpdateResponse>, t: Throwable) {
                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


            }

            holder.cartdelete.setOnClickListener {
                holder.cartlayout.visibility = View.GONE
                holder.cartprogreebar.visibility = View.VISIBLE
                var qtydelete = data[position].qty.toInt()

                if (qtydelete > 1) {
                    var qtydelete1 = data[position].qty.toInt() - 1

                    if (getsaveloginID != "") {
                        try {
                            RetrofitInstance.instence?.updatecartview(CartUpdateRequest(
                                updateitemid,
                                qtydelete1,
                                getsaveloginID
                            )
                            )!!.enqueue(object : Callback<CartUpdateResponse> {
                                override fun onResponse(
                                    call: Call<CartUpdateResponse>,
                                    response: Response<CartUpdateResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        holder.cartlayout.visibility = View.VISIBLE
                                        holder.cartprogreebar.visibility = View.GONE
                                        Toast.makeText(context, "Item qty updated.", Toast.LENGTH_SHORT)
                                            .show()
                                        pagerefresh.onQuantityListner()
                                    }
                                }

                                override fun onFailure(call: Call<CartUpdateResponse>, t: Throwable) {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            })
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        try {
                            RetrofitInstance.instence?.updatecartview(CartUpdateRequest(
                                updateitemid,
                                qtydelete1,
                                getsavePhoneUnique
                            )
                            )!!.enqueue(object : Callback<CartUpdateResponse> {
                                override fun onResponse(
                                    call: Call<CartUpdateResponse>,
                                    response: Response<CartUpdateResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        holder.cartlayout.visibility = View.VISIBLE
                                        holder.cartprogreebar.visibility = View.GONE
                                        Toast.makeText(context, "Item qty updated.", Toast.LENGTH_SHORT)
                                            .show()
                                        pagerefresh.onQuantityListner()
                                    }
                                }

                                override fun onFailure(call: Call<CartUpdateResponse>, t: Throwable) {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            })
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }


                }
            }

            holder.carttrash.setOnClickListener {
                holder.cartlayout.visibility = View.GONE
                holder.cartprogreebar.visibility = View.VISIBLE

                if (getsaveloginID != "") {
                    try {
                        RetrofitInstance.instence?.deletecart(DeleteCartRequest(
                            updateitemid,
                            getsaveloginID
                        )
                        )!!.enqueue(object : Callback<DeleteCartResponse> {
                            override fun onResponse(
                                call: Call<DeleteCartResponse>,
                                response: Response<DeleteCartResponse>
                            ) {
                                if (response.isSuccessful) {
                                    holder.cartlayout.visibility = View.VISIBLE
                                    holder.cartprogreebar.visibility = View.GONE
                                    Toast.makeText(context, "Item deleted.", Toast.LENGTH_SHORT).show()
                                    pagerefresh.onQuantityListner()
//                                context.startActivity(Intent(context, MainActivity::class.java))
                                }
                            }

                            override fun onFailure(call: Call<DeleteCartResponse>, t: Throwable) {
                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    try {
                        RetrofitInstance.instence?.deletecart(DeleteCartRequest(
                            updateitemid,
                            getsavePhoneUnique
                        )
                        )!!.enqueue(object : Callback<DeleteCartResponse> {
                            override fun onResponse(
                                call: Call<DeleteCartResponse>,
                                response: Response<DeleteCartResponse>
                            ) {
                                if (response.isSuccessful) {
                                    holder.cartlayout.visibility = View.VISIBLE
                                    holder.cartprogreebar.visibility = View.GONE
                                    Toast.makeText(context, "Item deleted.", Toast.LENGTH_SHORT).show()
                                    pagerefresh.onQuantityListner()
//                                context.startActivity(Intent(context, MainActivity::class.java))
                                }
                            }

                            override fun onFailure(call: Call<DeleteCartResponse>, t: Throwable) {
                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
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