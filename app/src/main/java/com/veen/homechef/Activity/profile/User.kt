package com.veen.homechef.Activity.profile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.CityListAdapter
import com.app.pharmadawa.ui.notification.CountryListAdapter
import com.app.pharmadawa.ui.notification.StateListAdapter
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Adapter.NetworkConnection
import com.veen.homechef.Model.Profile.updateprofile.UpdateProfileRequest
import com.veen.homechef.Model.Profile.updateprofile.UpdateProfileResponse
import com.veen.homechef.Model.Profile.uploadimage.UploadImageRequest
import com.veen.homechef.Model.Profile.uploadimage.UploadImageResponse
import com.veen.homechef.Model.Profile.viewprofile.ViewProfileData
import com.veen.homechef.Model.Profile.viewprofile.ViewProfileRequest
import com.veen.homechef.Model.Profile.viewprofile.ViewProfileResponse
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
import com.veen.homechef.Utils.Constants
import com.veen.homechef.Utils.Helper
import com.veen.homechef.Utils.RecyclerViewClickListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class User : AppCompatActivity() {
    private var rl_photo_parent: RelativeLayout? = null
    private var iv_profile_photo: CircleImageView? = null
    private val pickImage = 56789
    private var encodedImage = ""

    private lateinit var countryListAdapter: CountryListAdapter
    private lateinit var stateListAdapter: StateListAdapter
    private lateinit var cityListAdapter: CityListAdapter

    private var countryList: ArrayList<CountryData> = ArrayList()
    private var stateList: ArrayList<StateData> = ArrayList()
    private var cityList: ArrayList<CityData> = ArrayList()

    private var countryId: Int? = null
    private var stateId: Int? = null
    private var cityId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_user)

        try {
            countryListAdapter = CountryListAdapter(applicationContext, countryList!!)
            stateListAdapter = StateListAdapter(applicationContext, stateList!!)
            cityListAdapter = CityListAdapter(applicationContext, cityList!!)
            iv_profile_photo = findViewById(R.id.iv_profile_photo)
            rl_photo_parent = findViewById(R.id.rl_photo_parent)


            val networkConnection = NetworkConnection(applicationContext)
            networkConnection.observe(this, Observer { isConnected ->
                if (isConnected) {
                    profile_loader.visibility = View.VISIBLE
                    layoutdisconnected.visibility = View.GONE

                    ViewProfile()

                    viewprofile_btn1.setOnClickListener {
                        showprofile_layout.visibility = View.GONE
//                    editprofile_layout.visibility = View.VISIBLE
                    }

                    viewprofile_country1.setOnClickListener {
                        getCountry()
//                    getCountrydata()
                    }

                    viewprofile_state1.setOnClickListener {
                        dialogState()
                        getStateList(countryId.toString())
                    }

                    viewprofile_city1.setOnClickListener {
                        dialogCity()
                        getCityList(stateId.toString())
                    }

                    viewprofile_btn1!!.setOnClickListener {
                        ProfileUpdate(applicationContext)
                    }

                    rl_photo_parent!!.setOnClickListener {
                        if (!Helper.isReadExternalStoragePermissionAllowed(this) || (!Helper.isCameraPermission(
                                this
                            ))
                        ) {
                            requestPermissions(
                                arrayOf(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.CAMERA
                                ), Constants.STORAGE_PERMISSION_CODE_PHOTO
                            )
                        } else {
                            val gallery = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.INTERNAL_CONTENT_URI
                            )
                            startActivityForResult(gallery, pickImage)
                        }
                    }

                } else {
                    layoutdisconnected.visibility = View.VISIBLE
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
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
        countryRecyclerView.adapter = cityListAdapter
        cityListAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(id: Int, position: Int) {
                viewprofile_city1!!.setText(cityListAdapter.getItem(position).name)
                cityId = cityListAdapter.getItem(position).id!!.toInt()
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
        countryRecyclerView.adapter = stateListAdapter
        stateListAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(id: Int, position: Int) {
                viewprofile_state1!!.setText(stateListAdapter.getItem(position).name)
                if (stateId != stateListAdapter.getItem(position).id!!.toInt()) {
                    stateId = stateListAdapter.getItem(position).id!!.toInt()
                    cityListAdapter.notifyDataSetChanged()
                    getCityList(stateId.toString())
                    viewprofile_city1!!.text = ""
                    viewprofile_city1!!.setHint("City")
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
                                cityList!!.clear()
                                cityList!!.addAll(response.body()!!.data)
                                cityListAdapter!!.notifyDataSetChanged()
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
        countryRecyclerView.adapter = countryListAdapter
        countryListAdapter.setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(id: Int, position: Int) {
                viewprofile_country1!!.setText(countryListAdapter.getItem(position).name)
                if (countryId != countryListAdapter.getItem(position).id!!.toInt()) {
                    countryId = countryListAdapter.getItem(position).id!!.toInt()
                    stateId = 0
                    cityId = 0
                    stateListAdapter.notifyDataSetChanged()
                    cityListAdapter.notifyDataSetChanged()
                    getStateList(countryId.toString())
                    viewprofile_state1!!.text = ""
                    viewprofile_city1!!.text = ""
                    viewprofile_state1!!.setHint("State")
                    viewprofile_city1!!.setHint("City")
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
                                stateList!!.clear()
                                stateList!!.addAll(response.body()!!.data)
                                stateListAdapter!!.notifyDataSetChanged()
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
                        countryList.clear()
                        countryList.addAll(response.body()!!.data)
                        countryListAdapter!!.notifyDataSetChanged()
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        if (requestCode == Constants.STORAGE_PERMISSION_CODE_PHOTO) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val gallery = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                )
                startActivityForResult(gallery, pickImage)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            val imageUri = data!!.data
            iv_profile_photo!!.setImageURI(imageUri)
            val bitmap: Bitmap
            try {
                bitmap = BitmapFactory.decodeStream(
                    applicationContext.getContentResolver().openInputStream(
                        imageUri!!
                    )
                )
//                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500 , 500, false)
                encodedImage = getEncoded64ImageStringFromBitmap(bitmap).toString()
                profileImageUpdate(encodedImage)
                Log.e("exp", encodedImage)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun profileImageUpdate(encodedImage: String) {
        try {
            val getsaveloginID = AppUtils.getsaveloginID(applicationContext).toInt()
            val getsavetoken = AppUtils.getsavetoken(applicationContext)

            RetrofitInstance.instence?.uploadimages(
                getsavetoken, UploadImageRequest(
                    encodedImage,
                    getsaveloginID
                )
            )!!.enqueue(object : Callback<UploadImageResponse> {
                override fun onResponse(
                    call: Call<UploadImageResponse>,
                    response: Response<UploadImageResponse>
                ) {
                    if (response.body()!!.status == true) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "" + response.body()!!.msg,
                                Toast.LENGTH_SHORT
                            ).show()
                            ViewProfile()
                        }
                    }
                }

                override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteFormat = stream.toByteArray()
        val imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP)
        return imgString
    }

    //view profile API starts here
    private fun ViewProfile() {
        try {
            var getloginID = AppUtils.getsaveloginID(applicationContext).toInt()
            var gettoken = AppUtils.getsavetoken(applicationContext)

            RetrofitInstance.instence?.profileview(
                gettoken, ViewProfileRequest(
                    getloginID
                )
            )!!.enqueue(object : Callback<ViewProfileResponse> {
                override fun onResponse(
                    call: Call<ViewProfileResponse>,
                    response: Response<ViewProfileResponse>
                ) {
                    if (response.body()!!.status == true) {
                        if (response.isSuccessful) {
                            profilelayout.visibility = View.VISIBLE
                            profile_loader.visibility = View.GONE
                            bindProfileInfo(response.body()!!.data)
                        }
                    } else {
                        Toast.makeText(applicationContext, "No data available", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                override fun onFailure(call: Call<ViewProfileResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Data Not Fetching", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun ProfileUpdate(requireContext: Context) {
        try {
            var loginID = AppUtils.getsaveloginID(applicationContext)
            var gettoken = AppUtils.getsavetoken(applicationContext)

            RetrofitInstance.instence?.profileupdate(
                gettoken, UpdateProfileRequest(
                    viewprofile_address1.text.toString(),
                    cityId!!.toString(),
                    countryId!!.toString(),
                    loginID,
                    viewprofile_name1.text.toString(),
                    viewprofile_number1.text.toString(),
                    viewprofile_pincode1.text.toString(),
                    stateId!!.toString()
                )
            )!!.enqueue(object : Callback<UpdateProfileResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileResponse>,
                    response: Response<UpdateProfileResponse>
                ) {
                    profilelayout.visibility = View.VISIBLE
                    profile_loader.visibility = View.GONE
                    startActivity(Intent(requireContext, User::class.java))
                    finish()
                    Toast.makeText(requireContext, "Profile Update", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindProfileInfo(data: ViewProfileData) {
        try {
            viewprofile_name1.setText(data.name, TextView.BufferType.EDITABLE)
            viewprofile_number1.setText(data.phone, TextView.BufferType.EDITABLE)
            viewprofile_pincode1.setText(data.pincode, TextView.BufferType.EDITABLE)
            viewprofile_country1.setText(data.country_name)
            viewprofile_state1.setText(data.state_name)
            viewprofile_city1.setText(data.city_name)
            viewprofile_address1.setText(data.address, TextView.BufferType.EDITABLE)
            Picasso.get().load(data.profile_pic).into(iv_profile_photo)
            countryId = data.country_id.toInt()
            stateId = data.state_id.toInt()
            cityId = data.city_id.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}