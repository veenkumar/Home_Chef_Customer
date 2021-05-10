package com.veen.homechef.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Activity.Splash
import com.veen.homechef.R
import com.veen.homechef.Activity.profile.ChangePassword
import com.veen.homechef.Activity.profile.GiftCardDetails
import com.veen.homechef.Activity.profile.MyOrder
import com.veen.homechef.Activity.profile.User
import com.veen.homechef.Adapter.NetworkConnection
import com.veen.homechef.Model.Profile.viewprofile.ViewProfileRequest
import com.veen.homechef.Model.Profile.viewprofile.ViewProfileResponse
import com.veen.homechef.Utils.AppUtils
import kotlinx.android.synthetic.main.fragment_user.layoutdisconnected
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(), View.OnClickListener {
    private var user_profile: TextView? = null
    private var user_pass: TextView? = null
    private var user_order: TextView? = null
    private var user_gift: TextView? = null
    private var user_logout: TextView? = null
    private var user_name: TextView? = null
    private lateinit var profilelayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        try {
            MyClass.activity = requireActivity()
            user_profile = root.findViewById(R.id.profile_update)
            user_pass = root.findViewById(R.id.profile_pass)
            user_order = root.findViewById(R.id.profile_order)
            user_gift = root.findViewById(R.id.profile_gift)
            user_logout = root.findViewById(R.id.profile_logout)
            user_name = root.findViewById(R.id.profile_name)
            profilelayout = root.findViewById(R.id.profilelayout)

            val networkConnection = NetworkConnection(requireContext())
            networkConnection.observe(requireActivity(), Observer { isConnected ->
                if (isConnected) {
                    layoutdisconnected!!.visibility = View.GONE
                    profilelayout.visibility = View.VISIBLE

                    var getloginID = AppUtils.getsaveloginID(requireContext()).toInt()
                    var gettoken = AppUtils.getsavetoken(requireContext())

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
                                    user_name!!.text = response.body()!!.data.name
                                }
                            }

                        }

                        override fun onFailure(call: Call<ViewProfileResponse>, t: Throwable) {
                            Log.d("TAG", "onFailure: Failed")
                        }
                    })

                    user_profile!!.setOnClickListener(this)
                    user_pass!!.setOnClickListener(this)
                    user_order!!.setOnClickListener(this)
                    user_gift!!.setOnClickListener(this)
                    user_logout!!.setOnClickListener(this)

                } else {
                    layoutdisconnected!!.visibility = View.VISIBLE
                    profilelayout.visibility = View.GONE
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return root
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.profile_update -> {
                startActivity(Intent(requireContext(), User::class.java))
            }
            R.id.profile_pass -> {
                startActivity(Intent(requireContext(), ChangePassword::class.java))
            }
            R.id.profile_order -> {
                startActivity(Intent(requireContext(), MyOrder::class.java))
//                startActivity(Intent(requireContext(), Timer::class.java))
            }
            R.id.profile_gift -> {
                startActivity(Intent(requireContext(), GiftCardDetails::class.java))
            }
            R.id.profile_logout -> {
                var deleteloginID = AppUtils.deleteloginID(requireContext())
                deleteloginID
                startActivity(Intent(requireContext(), Splash::class.java))
                ProfileFragment.MyClass.activity?.finish()
            }
        }
    }

    class MyClass{
        companion object{
            var activity: Activity? = null
        }
    }
}