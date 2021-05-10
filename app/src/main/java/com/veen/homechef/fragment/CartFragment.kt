package com.veen.homechef.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Activity.Checkout
import com.veen.homechef.Adapter.CartAdapter
import com.veen.homechef.Adapter.NetworkConnection
import com.veen.homechef.Model.Cart.CartView.CartData
import com.veen.homechef.Model.Cart.CartView.CartViewRequest
import com.veen.homechef.Model.Cart.CartView.CartViewResponse
import com.veen.homechef.R
import com.veen.homechef.Utils.CartListner
import com.veen.homechef.Utils.AppUtils
import kotlinx.android.synthetic.main.fragment_cart.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment(), CartListner {
    private var checkout: TextView? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var adapter: CartAdapter
    private lateinit var totalamount: TextView
    private var singlerun: String? = ""
    private var mainLayout: LinearLayout? = null
    private var progressBar: ProgressBar? = null
    private var layoutdisconnected: LinearLayout? = null

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

//       val view = inflater.inflate(R.layout.item_list5, container, false)

       recyclerView = view.findViewById(R.id.cartrecycler)
       totalamount = view.findViewById(R.id.carttotalprice)
       checkout = view.findViewById(R.id.checkout)
       mainLayout = view.findViewById(R.id.mainLayout)
       progressBar = view.findViewById(R.id.cart_progressBar)
       layoutdisconnected = view.findViewById(R.id.layoutdisconnected)

       val networkConnection = NetworkConnection(requireContext())
       networkConnection.observe(requireActivity(), Observer { isConnected ->
           if (isConnected) {
               layoutdisconnected!!.visibility = View.GONE
               try {
                   checkout!!.setOnClickListener {
                       startActivity(Intent(requireContext(), Checkout::class.java))
                   }
                   cartView()

               } catch (e: JSONException) {
                   e.printStackTrace()
               }
           } else {
               layoutdisconnected!!.visibility = View.VISIBLE
               mainLayout!!.visibility = View.GONE
               progressBar!!.visibility = View.GONE
           }
       })


       return view
    }

    private fun cartView() {
        val getloginID = AppUtils.getsaveloginID(context)
        val gettoken = AppUtils.getsavetoken(context)

        try {
                RetrofitInstance.instence?.cartview(
                    gettoken, CartViewRequest(
                        getloginID.toInt()
                    )
                )!!.enqueue(object : Callback<CartViewResponse> {
                    override fun onResponse(
                        call: Call<CartViewResponse>,
                        response: Response<CartViewResponse>
                    ) {
                        if (response.body()!!.status == false) {
                            singlerun = "1"
                            mainLayout!!.visibility = View.GONE
                            progressBar!!.visibility = View.GONE
                            cart_empty?.visibility = View.VISIBLE
                        } else {
                            if (response.isSuccessful) {
                                mainLayout!!.visibility = View.VISIBLE
                                progressBar!!.visibility = View.GONE
                                totalamount?.text = response.body()!!.data[0].currency+ " " + response.body()!!.data[0].total_amount.toString()

                                bindmycart(response.body()!!.data[0].cart_data)
                            }
                        }
                    }

                    override fun onFailure(call: Call<CartViewResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                    }
                })
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun bindmycart(data: List<CartData>) {
        try {
            recyclerView!!.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = CartAdapter(requireContext(), this, data)
            recyclerView!!.adapter = adapter
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun onQuantityListner() {
        cartView()
    }

}