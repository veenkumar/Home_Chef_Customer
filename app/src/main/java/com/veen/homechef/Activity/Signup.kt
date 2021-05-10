package com.veen.homechef.Activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.CityListAdapter
import com.app.pharmadawa.ui.notification.CountryListAdapter
import com.app.pharmadawa.ui.notification.StateListAdapter
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Model.SignUp.SignUpRequest
import com.veen.homechef.Model.SignUp.SignUpResponse
import com.veen.homechef.Model.location.city.CityData
import com.veen.homechef.Model.location.city.CityRequest
import com.veen.homechef.Model.location.city.CityResponse
import com.veen.homechef.Model.location.country.CountryData
import com.veen.homechef.Model.location.country.CountryResponse
import com.veen.homechef.Model.location.state.StateData
import com.veen.homechef.Model.location.state.StateRequest
import com.veen.homechef.Model.location.state.StateResponse
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import com.veen.homechef.Utils.RecyclerViewClickListener
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.gender_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


class Signup : AppCompatActivity() {
    private lateinit var string: String
    private var login: TextView? = null
    private var fname: EditText? = null
    private var semail: EditText? = null
    private var sphone: EditText? = null
    private var sgenderID: String? = null
    private var spass: EditText? = null
    private var scpass: EditText? = null
    private var signupbtn: Button? = null

    private lateinit var signupcountryListAdapter: CountryListAdapter
    private lateinit var signupstateListAdapter: StateListAdapter
    private lateinit var signupcityListAdapter: CityListAdapter

    private var signupcountryList: ArrayList<CountryData> = ArrayList()
    private var signupstateList: ArrayList<StateData> = ArrayList()
    private var signupcityList: ArrayList<CityData> = ArrayList()

    private var signupcountryId: Int? = null
    private var signupstateId: Int? = null
    private var signupcityId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_signup)

        try {
            fname = findViewById(R.id.signup_firstname)
            semail = findViewById(R.id.signup_email)
            sphone = findViewById(R.id.signup_phone)
            spass = findViewById(R.id.signup_password)
            signupbtn = findViewById(R.id.signup_btn)
            login = findViewById(R.id.signup_started)
            scpass = findViewById(R.id.signup_confirmpassword)
            signupcountryListAdapter = CountryListAdapter(applicationContext, signupcountryList!!)
            signupstateListAdapter = StateListAdapter(applicationContext, signupstateList!!)
            signupcityListAdapter = CityListAdapter(applicationContext, signupcityList!!)

            string = ""

            signup_gender.setOnClickListener {
                opengenderdialogbox()
            }

            signup_country.setOnClickListener {
                getCountry()
//                    getCountrydata()
            }

            signup_state.setOnClickListener {
                dialogState()
                getStateList(signupcountryId.toString())
            }

            signup_city.setOnClickListener {
                dialogCity()
                getCityList(signupstateId.toString())
            }

            var regexPassword =
                Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}")

            login!!.setOnClickListener {
                startActivity(Intent(applicationContext, Login::class.java))
            }

            signupbtn!!.setOnClickListener {
                if (TextUtils.isEmpty(fname!!.text.toString())) {
                    fname!!.setError("field can't be empty")
                } else if (TextUtils.isEmpty(semail!!.text.toString())) {
                    semail!!.setError("field can't be empty")
                } else if (TextUtils.isEmpty(sphone!!.text.toString())) {
                    sphone!!.setError("field can't be empty")
                } else if (sphone!!.length() <= 9){
                    sphone!!.setError("Please enter 10 to 13 digit number")
                } else if (TextUtils.isEmpty(spass!!.text.toString())) {
                    spass!!.setError("field can't be empty")
                } else if (TextUtils.isEmpty(scpass!!.text.toString())) {
                    scpass!!.setError("field can't be empty")
                } else if (spass!!.text.toString().length < 8 || regexPassword.matcher(spass!!.text.toString()).find()) {
                    Toast.makeText(applicationContext,"Password length should be at least 8 character long.", Toast.LENGTH_SHORT).show()
                } else {
                    if (spass!!.text.toString() == scpass!!.text.toString()) {
                        Registration()
                    } else {
                        Toast.makeText(applicationContext,"New & Confirm Password Not Matched",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun opengenderdialogbox() {
        val dialog = this.let { Dialog(it) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.gender_list)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
//        dialog.rg_gender.check(R.id.rb_male)
        dialog.rb_male.setOnClickListener {
            signup_gender.setText("Male")
            if(signup_gender.text.toString() == "Male"){
                sgenderID = "1"
            }
            dialog.dismiss()
        }
        dialog.rb_female.setOnClickListener {
            signup_gender.setText("Female")
            if(signup_gender.text.toString() == "Female"){
                sgenderID = "2"
            }
            dialog.dismiss()
        }
        dialog.rb_other.setOnClickListener {
            signup_gender.setText("Other")
            if(signup_gender.text.toString() == "Other"){
                sgenderID = "3"
            }
            dialog.dismiss()
        }
        val window = dialog.getWindow()
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun dialogCity() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialogue_country)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        var textcountry: TextView? = null
        textcountry = dialog.findViewById(R.id.textView50_alert)
        textcountry.text = "Please Select City"
        var countryRecyclerView: RecyclerView? = null
        countryRecyclerView = dialog.findViewById(R.id.rv_country_list)
        countryRecyclerView.layoutManager = LinearLayoutManager(this)
        countryRecyclerView.itemAnimator = DefaultItemAnimator()
        countryRecyclerView.adapter = signupcityListAdapter
        signupcityListAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(id: Int, position: Int) {
                signup_city!!.setText(signupcityListAdapter.getItem(position).name)
                signupcityId = signupcityListAdapter.getItem(position).id!!.toInt()
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

    private fun dialogState() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialogue_country)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        var textstate: TextView? = null
        textstate = dialog.findViewById(R.id.textView50_alert)
        textstate.text = "Please Select State"
        var countryRecyclerView: RecyclerView? = null
        countryRecyclerView = dialog.findViewById(R.id.rv_country_list)
        countryRecyclerView.layoutManager = LinearLayoutManager(this)
        countryRecyclerView.itemAnimator = DefaultItemAnimator()
        countryRecyclerView.adapter = signupstateListAdapter
        signupstateListAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(id: Int, position: Int) {
                signup_state!!.setText(signupstateListAdapter.getItem(position).name)
                if (signupstateId != signupstateListAdapter.getItem(position).id!!.toInt()) {
                    signupstateId = signupstateListAdapter.getItem(position).id!!.toInt()
                    signupcityListAdapter.notifyDataSetChanged()
                    getCityList(signupstateId.toString())
                    signup_city!!.text = ""
                    signup_city!!.setHint("City")
                }
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

    private fun getCityList(stateId: String) {
        try {
            var tokenkey = AppUtils.getsavetoken(applicationContext)
            RetrofitInstance.instence?.cityupdate(tokenkey, CityRequest(stateId.toInt()))!!.enqueue(
                object : Callback<CityResponse> {
                    override fun onResponse(
                        call: Call<CityResponse>,
                        response: Response<CityResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()!!.data != null) {
                                signupcityList!!.clear()
                                signupcityList!!.addAll(response.body()!!.data)
                                signupcityListAdapter!!.notifyDataSetChanged()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "City Not Available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCountry() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialogue_country)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        var textcountry: TextView? = null
        textcountry = dialog.findViewById(R.id.textView50_alert)
        textcountry.text = "Please Select Country"
        var countryRecyclerView: RecyclerView? = null
        countryRecyclerView = dialog.findViewById(R.id.rv_country_list)
        countryRecyclerView.layoutManager = LinearLayoutManager(this)
        countryRecyclerView.itemAnimator = DefaultItemAnimator()
        countryRecyclerView.adapter = signupcountryListAdapter
        signupcountryListAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(id: Int, position: Int) {
                signup_country!!.setText(signupcountryListAdapter.getItem(position).name)
                if (signupcountryId != signupcountryListAdapter.getItem(position).id!!.toInt()) {
                    signupcountryId = signupcountryListAdapter.getItem(position).id!!.toInt()
                    signupstateId = 0
                    signupcityId = 0
                    signupstateListAdapter.notifyDataSetChanged()
                    signupcityListAdapter.notifyDataSetChanged()
                    getStateList(signupcountryId.toString())
                    signup_state!!.text = ""
                    signup_city!!.text = ""
                    signup_state!!.setHint("State")
                    signup_city!!.setHint("City")
                }

                dialog.dismiss()
            }
        })
        //hit Country Api
        getCountrydata()

        val window = dialog.getWindow()
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun getStateList(countryId: String) {
        try {
            var tokenkey = AppUtils.getsavetoken(applicationContext)
            RetrofitInstance.instence?.stateupdate(tokenkey, StateRequest(countryId.toInt()))!!.enqueue(
                object : Callback<StateResponse> {
                    override fun onResponse(
                        call: Call<StateResponse>,
                        response: Response<StateResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()!!.data != null) {
                                signupstateList!!.clear()
                                signupstateList!!.addAll(response.body()!!.data)
                                signupstateListAdapter!!.notifyDataSetChanged()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "State Not Available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<StateResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCountrydata() {
        try {
            var tokenkey = AppUtils.getsavetoken(applicationContext)
            RetrofitInstance.instence?.countryupdate(tokenkey)!!.enqueue(object :
                Callback<CountryResponse> {
                override fun onFailure(call: Call<CountryResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<CountryResponse>,
                    response: Response<CountryResponse>
                ) {
                    if (response.isSuccessful) {
                        signupcountryList.clear()
                        signupcountryList.addAll(response.body()!!.data)
                        signupcountryListAdapter!!.notifyDataSetChanged()
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun Registration() {
        try {
            RetrofitInstance.instence?.signup(
                SignUpRequest(
                    semail!!.text.toString(),
                    fname!!.text.toString(),
                    spass!!.text.toString(),
                    sphone!!.text.toString()
                )
            )!!.enqueue(object : Callback<SignUpResponse> {
                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: Response<SignUpResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status) {
                            val LoginID = response.body()!!.data.id
                            val EmailID = response.body()!!.data.email
                            val ImageID = response.body()!!.data.image
                            val NameID = response.body()!!.data.name
                            val token = response.body()!!.token

                            AppUtils.saveloginID(applicationContext, LoginID)
                            AppUtils.saveemailID(applicationContext, EmailID)
                            AppUtils.saveImageID(applicationContext, ImageID)
                            AppUtils.savenameID(applicationContext, NameID)
                            AppUtils.savetoken(applicationContext, token)
                            Toast.makeText(
                                applicationContext,
                                "Registration Completed Successfully.",
                                Toast.LENGTH_SHORT
                            ) .show()
                            startActivity(Intent(this@Signup, Login::class.java))
                        }
                    } else /*if (!response.body()!!.status)*/ {
                        Toast.makeText(
                            applicationContext,
                            "Mobile or Email id already exist",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}