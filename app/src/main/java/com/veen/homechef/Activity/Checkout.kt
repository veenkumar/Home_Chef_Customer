package com.veen.homechef.Activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Activity.timer.Timer
import com.veen.homechef.Adapter.CheckoutItemAdapter
import com.veen.homechef.Adapter.CouponAdapter
import com.veen.homechef.Model.coupon.Fetch.FetchCouponData
import com.veen.homechef.Model.coupon.Fetch.FetchCouponRequest
import com.veen.homechef.Model.coupon.Fetch.FetchCouponResponse
import com.veen.homechef.Model.coupon.remove.CouponRemovedRequest
import com.veen.homechef.Model.coupon.remove.CouponRemovedResponse
import com.veen.homechef.Model.itemdetails.CheckoutItemData
import com.veen.homechef.Model.itemdetails.CheckoutItemItem
import com.veen.homechef.Model.itemdetails.CheckoutItemRequest
import com.veen.homechef.Model.itemdetails.CheckoutItemResponse
import com.veen.homechef.Model.order.OrderRequest
import com.veen.homechef.Model.order.OrderResponse
import com.veen.homechef.R
import com.veen.homechef.Retrofit.Retrofitfirebase
import com.veen.homechef.Utils.AppUtils
import com.veen.homechef.Utils.CouponListner
import com.veen.homechef.firebase.FirebaseService
import com.veen.homechef.firebase.NotificationData
import com.veen.homechef.firebase.PushNotification
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.coupon_recyclerview.*
import kotlinx.android.synthetic.main.coupon_recyclerview.view.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.search_dialog.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

const val TOPIC = "/topics/myTopic2"

class Checkout : AppCompatActivity(), CouponListner {
    companion object{
        private val REQUEST_PERMISSION_REQUEST_CODE = 2020
    }

    val TAG = "MainActivity"

    private lateinit var mAlertDialog: AlertDialog
    private var currentLocationLatitute: String? = ""
    private var currentLocationLongitute: String? = ""

    private lateinit var checkoutrecyclerView: RecyclerView
    private lateinit var adapter: CheckoutItemAdapter
    private lateinit var couponadapter: CouponAdapter
    private var cname: EditText? = null
    private var cemail: EditText? = null
    private var cphone: EditText? = null
    private var cpincode: EditText? = null
    private var cbutton: Button? = null
    private var caddress: EditText? = null
    private var clocation: TextView? = null
    private var recipientToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        try {
            cname = findViewById(R.id.checkout_name)
            cemail = findViewById(R.id.checkout_email)
            cphone = findViewById(R.id.checkout_phone)
            cpincode = findViewById(R.id.checkout_pincode)
            cbutton = findViewById(R.id.checkout_done)
            caddress = findViewById(R.id.checkout_address)
            clocation = findViewById(R.id.getlocation)
            checkoutrecyclerView = findViewById(R.id.checkout_recyclerview)

            val getsaveloginID = AppUtils.getsaveloginID(applicationContext)
            val gettoken = AppUtils.getsavetoken(applicationContext)

            getCurrentLocation()

            FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                FirebaseService.token = it.token
                recipientToken = it.token
//                etToken.setText(it.token)
            }
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

            CheckoutItemDetails(getsaveloginID, gettoken)

            checkout_appliedcoupon2.setOnClickListener {
                try {
                    RetrofitInstance.instence?.removecoupon(gettoken, CouponRemovedRequest(
                        getsaveloginID.toInt()
                    ))!!.enqueue(object : Callback<CouponRemovedResponse> {
                        override fun onResponse(
                            call: Call<CouponRemovedResponse>,
                            response: Response<CouponRemovedResponse>
                        ) {
                            if (response.body()!!.status == true) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "" + response.body()!!.msg,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    CheckoutItemDetails(getsaveloginID, gettoken)
                                }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "" + response.body()!!.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<CouponRemovedResponse>, t: Throwable) {
                            Log.d("TAG", "onFailure: Failed")
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            checkout_appliedcoupon.setOnClickListener {
                applycoupon(gettoken, getsaveloginID)
            }

            getlocation!!.setOnClickListener {
                //check permission
                if (ContextCompat.checkSelfPermission(
                        applicationContext,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        , REQUEST_PERMISSION_REQUEST_CODE
                    )
                }else {
                    loader.visibility = View.VISIBLE
                    getCurrentLocation()
                }
            }

            cbutton!!.setOnClickListener {
                if (TextUtils.isEmpty(clocation!!.text.toString())) {
                    clocation!!.setError("field can't be empty")
                } else if (TextUtils.isEmpty(cname!!.text.toString())) {
                    cname!!.setError("field can't be empty")
                } else if (TextUtils.isEmpty(cemail!!.text.toString())) {
                    cemail!!.setError("field can't be empty")
                } else if (TextUtils.isEmpty(cphone!!.text.toString())) {
                    cphone!!.setError("field can't be empty")
                } else if (TextUtils.isEmpty(cpincode!!.text.toString())) {
                    cpincode!!.setError("field can't be empty")
                } else if (TextUtils.isEmpty(caddress!!.text.toString())) {
                    caddress!!.setError("field can't be empty")
                } else {
                    val title = "Home Chef"
                    val message = "Your Order Request received!"
//                    val recipientToken = etToken.text.toString()
                    if(title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                        PushNotification(
                            NotificationData(title, message),
                            recipientToken
                        ).also {
                            sendNotification(it)
                        }
                    }
                    paymentsuccess(getsaveloginID, gettoken)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun applycoupon(gettoken: String, getsaveloginID: String) {
        val mdialogview =
            LayoutInflater.from(this).inflate(R.layout.coupon_recyclerview, null)
        var couponrecycler =
            mdialogview.findViewById<RecyclerView>(R.id.coupon_recycler)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mdialogview)
         mAlertDialog = mBuilder.show()

        try {
            RetrofitInstance.instence?.fetchcoupon(gettoken, FetchCouponRequest(
                getsaveloginID.toInt()
            )
            )!!.enqueue(object : Callback<FetchCouponResponse> {
                override fun onResponse(
                    call: Call<FetchCouponResponse>,
                    response: Response<FetchCouponResponse>
                ) {
                    if (response.body()!!.status == true) {
                        if (response.isSuccessful) {
                            bindCoupondata(response.body()!!.data, couponrecycler)
                        }
                    }
                }

                override fun onFailure(call: Call<FetchCouponResponse>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (checkout_appliedcoupon1.text == "Coupon Applied") {
            mAlertDialog.dismiss()
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = Retrofitfirebase.api.postNotification(notification)
            if(response.isSuccessful) {
//                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindCoupondata(data: List<FetchCouponData>, couponrecycler: RecyclerView?) {
        try {
            couponrecycler?.layoutManager =
                LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            couponadapter = CouponAdapter(applicationContext, this, data)
            couponrecycler?.adapter = couponadapter
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun CheckoutItemDetails(getsaveloginID: String, gettoken: String) {
        try {
            RetrofitInstance.instence?.checkoutitem(gettoken, CheckoutItemRequest(
                getsaveloginID.toInt()
            ))!!.enqueue(object : Callback<CheckoutItemResponse> {
                override fun onResponse(
                    call: Call<CheckoutItemResponse>,
                    response: Response<CheckoutItemResponse>
                ) {
                    if (response.body()!!.status == true) {
                        if (response.isSuccessful) {
                            loader.visibility = View.GONE
                            checkout_done.visibility = View.VISIBLE
                            checkoutlayout.visibility = View.VISIBLE
                            bindmyItem(response.body()!!.data)
                            ViewmyItem(response.body()!!.data.item_list)
                        } else {
                            Toast.makeText(applicationContext, ""+response.body()!!.msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<CheckoutItemResponse>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                    loader.visibility = View.VISIBLE
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun ViewmyItem(itemList: List<CheckoutItemItem>) {
        try {
            checkoutrecyclerView.layoutManager =
                LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            adapter = CheckoutItemAdapter(applicationContext, itemList)
            checkoutrecyclerView.adapter = adapter
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun bindmyItem(data: CheckoutItemData) {
        checkout_totalamount.text = data.currency + " " + HtmlCompat.fromHtml(data.total.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        checkout_shipping.text = "(+) " + data.currency + " " + HtmlCompat.fromHtml(data.shipping_charge.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        checkout_discount.text = "(-) " + data.currency + " " + data.discount
        checkout_subtotal.text = data.currency + " " + HtmlCompat.fromHtml(data.subtotal.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        if (data.coupon_status == 1) {
            checkout_appliedcoupon1.setText("Coupon Applied")
            checkout_appliedcoupon2.visibility = View.VISIBLE
        } else {
            checkout_appliedcoupon1.setText("Coupon Not Applied")
            checkout_appliedcoupon2.visibility = View.GONE
        }
    }

    private fun paymentsuccess(getsaveloginID: String, gettoken: String) {
        try {
            RetrofitInstance.instence?.ordernow(gettoken, OrderRequest(
                caddress!!.text.toString(),
                clocation!!.text.toString(),
                cemail!!.text.toString(),
                cname!!.text.toString(),
                0,
                cphone!!.text.toString(),
                cpincode!!.text.toString(),
                getsaveloginID.toInt()
            ))!!.enqueue(object : Callback<OrderResponse> {
                override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                    if (response.body()!!.status == true) {
                        if (response.isSuccessful) {
                            val orderID = response.body()!!.order_id.toString()

                            AppUtils.saveorderID(applicationContext, orderID)
                            Toast.makeText(applicationContext, "Order Complete", Toast.LENGTH_SHORT)
                                .show()
//                            startActivity(Intent(applicationContext, MyOrder::class.java))
                            startActivity(Intent(applicationContext, Timer::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Sorry! cart is empty.", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_REQUEST_CODE && grantResults.size > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }else{
                Toast.makeText(this,"Permission Denied!",Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun getCurrentLocation() {

        var locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        //now getting address from latitude and longitude

        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses:List<Address>

        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
        }
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest,object : LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@Checkout)
                        .removeLocationUpdates(this)
                    if (locationResult != null && locationResult.locations.size > 0){
                        var locIndex = locationResult.locations.size-1

                        var latitude = locationResult.locations.get(locIndex).latitude
                        var longitude = locationResult.locations.get(locIndex).longitude

                        currentLocationLatitute = latitude.toString()
                        currentLocationLongitute = longitude.toString()

                        addresses = geocoder.getFromLocation(latitude,longitude,1)

                        var address:String = addresses[0].getAddressLine(0)
                        clocation!!.text = address
                        if (clocation != null){
                            loader.visibility = View.GONE
                            checkout_done.visibility = View.VISIBLE
                            checkoutlayout.visibility = View.VISIBLE
                        }
                    }
                }
            }, Looper.getMainLooper())

    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCouponRefresh() {
        val getsaveloginID = AppUtils.getsaveloginID(applicationContext)
        val gettoken = AppUtils.getsavetoken(applicationContext)

        CheckoutItemDetails(getsaveloginID, gettoken)
        mAlertDialog.dismiss()
    }
}