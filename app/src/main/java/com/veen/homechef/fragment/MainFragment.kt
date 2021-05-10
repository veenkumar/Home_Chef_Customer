package com.veen.homechef.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Adapter.MySliderImageAdapter
import com.veen.homechef.Adapter.NetworkConnection
import com.veen.homechef.Model.HomePage.dashboard.DashboardRes
import com.veen.homechef.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_dish.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var linearLayout: LinearLayout
    private lateinit var viewPager: ViewPager
    private var story_one: ImageView? = null
    private var story_two: ImageView? = null
    private var story_three: ImageView? = null
    private var story_four: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dish, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        story_one = view.findViewById(R.id.story_one)
        story_two = view.findViewById(R.id.story_two)
        story_three = view.findViewById(R.id.story_three)
        story_four = view.findViewById(R.id.story_four)
        progressBar = view.findViewById(R.id.progressBarmain)
        linearLayout = view.findViewById(R.id.mainlayoutitem)

        return  view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(requireActivity(), Observer { isConnected ->
            if (isConnected) {
                layoutdisconnected!!.visibility = View.GONE

                try {
                    ImageSliderView()

                    Picasso.get().load("http://103.205.64.158/~nsystechlg/homechef/assets/web/images/our-story-01.jpg").into(story_one)
                    Picasso.get().load("http://103.205.64.158/~nsystechlg/homechef/assets/web/images/our-story-02.jpg").into(story_two)
                    Picasso.get().load("http://103.205.64.158/~nsystechlg/homechef/assets/web/images/our-story-03.jpg").into(story_three)
                    Picasso.get().load("http://103.205.64.158/~nsystechlg/homechef/assets/web/images/our-story-04.jpg").into(story_four)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                layoutdisconnected!!.visibility = View.VISIBLE
                linearLayout.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun ImageSliderView() {
        try {
            RetrofitInstance.instence?.gethome()!!.enqueue(object : Callback<DashboardRes> {
                override fun onResponse(
                    call: Call<DashboardRes>,
                    response: Response<DashboardRes>
                ) {
                    if (response.isSuccessful) {
                        try {
                            progressBar.visibility = View.GONE
                            linearLayout.visibility = View.VISIBLE
                            for (i in 0 until response.body()!![0].image.length) {
                                NUM_PAGES = response.body()!!.size
                                val adapter = MySliderImageAdapter(requireContext(), response.body()!!)
                                viewPager.adapter = adapter
                            }
                            // Auto start of viewpager
                            val handler = Handler()
                            val Update = Runnable {
                                if (currentPage == NUM_PAGES) {
                                    currentPage = 0
                                }
                                viewPager.setCurrentItem(currentPage++, true)
                            }
                            val swipeTimer = Timer()
                            swipeTimer.schedule(object : TimerTask() {
                                override fun run() {
                                    handler.post(Update)
                                }
                            }, 3000, 3000)
                            val adapter = MySliderImageAdapter(requireContext(), response.body()!!)
                            viewPager.adapter = adapter
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<DashboardRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private var currentPage = 0
        private var NUM_PAGES = 0
    }

}
