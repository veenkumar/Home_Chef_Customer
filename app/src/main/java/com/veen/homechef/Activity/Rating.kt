package com.veen.homechef.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Adapter.RatingAdapter
import com.veen.homechef.Model.chef.ChefData
import com.veen.homechef.Model.chef.ChefReq
import com.veen.homechef.Model.chef.ChefRes
import com.veen.homechef.Model.rating.RatingReq
import com.veen.homechef.Model.rating.RatingRes
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import com.veen.homechef.Utils.RatingInterface
import kotlinx.android.synthetic.main.activity_rating.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Rating : AppCompatActivity(), RatingInterface {
    lateinit var ratingview: RecyclerView
    private lateinit var adapter: RatingAdapter
    //private var cheflist: List<Int> = ArrayList()
    private  val ratinglist: MutableList<String> = java.util.ArrayList();
    private  val chefIDs: MutableList<Int> = java.util.ArrayList();

    private var cid: Int = 0
    private lateinit var rid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val OrderID = intent.getStringExtra("orderID")!!.toInt()
        val getloginID = AppUtils.getsaveloginID(applicationContext).toInt()
        val gettoken = AppUtils.getsavetoken(applicationContext)
        getChefDetails(OrderID, gettoken)
        ratingview = findViewById(R.id.ratingview)

        button.setOnClickListener {
            try {
                RetrofitInstance.instence?.ratingchef(gettoken, RatingReq(
                    chefIDs,
                    OrderID.toString(),
                    ratinglist,
                    getloginID
                ))!!.enqueue(object : Callback<RatingRes> {
                    override fun onResponse(call: Call<RatingRes>, response: Response<RatingRes>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "" + response.body()!!.msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<RatingRes>, t: Throwable) {
                        Log.d("TAG", "onFailure: Failed")
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun getChefDetails(orderID: Int, gettoken: kotlin.String) {
        try {
            RetrofitInstance.instence?.chefID(gettoken, ChefReq(
                orderID
            )
            )!!.enqueue(object : Callback<ChefRes> {
                override fun onResponse(call: Call<ChefRes>, response: Response<ChefRes>) {
                    if (response.isSuccessful) {
                        bindmyrating(response.body()!!.data)
                    }
                }

                override fun onFailure(call: Call<ChefRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindmyrating(data: List<ChefData>) {
        try {
            ratingview.layoutManager =
                LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            adapter = RatingAdapter(applicationContext, data, this)
            ratingview.adapter = adapter

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getRating(value: String, id: Int) {
        ratinglist.add(value)
        chefIDs.add(id)
    }

}