package com.veen.homechef.Activity.profile

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Model.Profile.myorder.MyOrderData
import com.veen.homechef.Model.Profile.myorder.MyOrderRequest
import com.veen.homechef.Model.Profile.myorder.MyOrderResponse
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import com.veen.homechef.Adapter.MyOrderAdapter
import com.veen.homechef.fragment.DatePickerFragment
import kotlinx.android.synthetic.main.fragment_my_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyOrder : AppCompatActivity(), DatePickerFragment.SetOnDateOfBirth {
    lateinit var myorder: RecyclerView
    private var adapter: MyOrderAdapter? = null
    private var checkdatetype: Boolean = false
    private var common_fromdate: TextView? = null
    private var common_todate: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_my_order)

        try {
            myorder = findViewById(R.id.myorder)
            val getloginID = AppUtils.getsaveloginID(applicationContext).toInt()
            val gettoken = AppUtils.getsavetoken(applicationContext)

            ViewOrder(gettoken, getloginID)

            common_resetbtn.setOnClickListener {
                common_orderID.text.clear()
                common_fromdate!!.text = ""
                common_todate!!.text = ""
                ViewOrder(gettoken, getloginID)
            }

            common_submit.setOnClickListener {
                if (common_orderID.text.toString() == "") {
                    if (TextUtils.isEmpty(common_fromdate!!.text.toString())) {
                        common_fromdate!!.setError("Field can't be empty!")
                    } else if (TextUtils.isEmpty(common_todate!!.text.toString())) {
                        common_todate!!.setError("Field can't be empty!")
                    } else {
                        getCommmonRecord(gettoken, getloginID)
                    }
                } else {
                    getCommonOrder(gettoken, getloginID)
                }

            }

            common_fromdate = findViewById(R.id.common_fromdate)
            common_fromdate?.setOnClickListener { showDatePickerDialog()
                checkdatetype = false}

            common_todate = findViewById(R.id.common_todate)
            common_todate?.setOnClickListener { showDatePickerDialog()
                checkdatetype = true}

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCommonOrder(gettoken: String, getloginID: Int) {
        try {
            RetrofitInstance.instence?.myorder(gettoken, MyOrderRequest(
                "",
                common_orderID.text.toString(),
                "",
                getloginID
            ))!!.enqueue(object : Callback<MyOrderResponse> {
                override fun onResponse(
                    call: Call<MyOrderResponse>,
                    response: Response<MyOrderResponse>
                ) {
                    if (response.isSuccessful) {
                        myorderlayout.visibility = View.VISIBLE
                        myorder_refresh.visibility = View.GONE
                        bindmyorder(response.body()!!.data)
                    }
                }

                override fun onFailure(call: Call<MyOrderResponse>, t: Throwable) {
//               Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCommmonRecord(gettoken: String, getloginID: Int) {
        try {
            RetrofitInstance.instence?.myorder(gettoken, MyOrderRequest(
                common_fromdate!!.text.toString(),
                "",
                common_todate!!.text.toString(),
                getloginID
            ))!!.enqueue(object : Callback<MyOrderResponse> {
                override fun onResponse(
                    call: Call<MyOrderResponse>,
                    response: Response<MyOrderResponse>
                ) {
                    if (response.isSuccessful) {
                        myorderlayout.visibility = View.VISIBLE
                        myorder_refresh.visibility = View.GONE
                        bindmyorder(response.body()!!.data)
                    }
                }

                override fun onFailure(call: Call<MyOrderResponse>, t: Throwable) {
//               Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun ViewOrder(gettoken: String, getloginID: Int) {
        try {
            RetrofitInstance.instence?.myorder(gettoken, MyOrderRequest(
                "",
                "",
                "",
                getloginID
            ))!!.enqueue(object : Callback<MyOrderResponse> {
                override fun onResponse(
                    call: Call<MyOrderResponse>,
                    response: Response<MyOrderResponse>
                ) {
                    if (response.isSuccessful) {
                        myorderlayout.visibility = View.VISIBLE
                        myorder_refresh.visibility = View.GONE
                        bindmyorder(response.body()!!.data)
                    }
                }

                override fun onFailure(call: Call<MyOrderResponse>, t: Throwable) {
//               Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindmyorder(data: List<MyOrderData>) {
        try {
            myorder.layoutManager =
                LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            adapter = MyOrderAdapter(applicationContext, data)
            myorder.adapter = adapter

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment()
        newFragment.setOnDateOfBirth(this)
        newFragment.show(supportFragmentManager, "datePicker")
    }

    override fun setDateOfBirth(date: StringBuilder?) {
        if (!date!!.equals("")) {
            if (checkdatetype) {
                common_todate?.text = date
            } else common_fromdate?.text = date
        }
    }
}