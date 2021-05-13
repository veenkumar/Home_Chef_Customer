package com.veen.homechef.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Adapter.*
import com.veen.homechef.Model.HomePage.menu.MenuData
import com.veen.homechef.Model.HomePage.menu.MenuRequest
import com.veen.homechef.Model.HomePage.menu.MenuResponse
import com.veen.homechef.Model.Search.SearchData
import com.veen.homechef.Model.Search.SearchRequest
import com.veen.homechef.Model.Search.SearchResponse
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.search_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageAdapter
    private lateinit var searchadapter: SearchAdapter
    private var searchview: ImageView? = null
    private var layoutdisconnected: LinearLayout? = null
    private var homeheader: TextView? = null
    private var search: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        try {
            recyclerView = view.findViewById(R.id.imagerecyclerview)
            searchview = view.findViewById(R.id.home_search)
            layoutdisconnected = view.findViewById(R.id.layoutdisconnected)
            homeheader = view.findViewById(R.id.home_header)
            search = view.findViewById(R.id.home_search1)

            homeheader!!.setText("HOME CHEF")

            val networkConnection = NetworkConnection(requireContext())
            networkConnection.observe(requireActivity(), Observer { isConnected ->
                if (isConnected) {
                    layoutdisconnected!!.visibility = View.GONE

                    ChefDetails()

                    searchview!!.setOnClickListener {
                        var gettoken = AppUtils.getsavetoken(context)
                        val mdialogview =
                            LayoutInflater.from(context).inflate(R.layout.search_dialog, null)
                        var searchrecycle =
                            mdialogview.findViewById<RecyclerView>(R.id.home_searchbar_view)
                        val mBuilder = AlertDialog.Builder(context)
                            .setView(mdialogview)

                        val mAlertDialog = mBuilder.show()

                        mdialogview.home_searchbarID.setOnClickListener {
                            try {
                                RetrofitInstance.instence?.search(
                                    gettoken, SearchRequest(
                                        "1",
                                        mdialogview.home_searchbar.text.toString()
                                    )
                                )!!.enqueue(object : Callback<SearchResponse> {
                                    override fun onResponse(
                                        call: Call<SearchResponse>,
                                        response: Response<SearchResponse>
                                    ) {
                                        if (response.body()!!.status == true) {
                                            if (response.isSuccessful) {
                                                bindSearchdata(response.body()!!.data, searchrecycle)
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                                        Log.d("TAG", "onFailure: Failed")
                                    }
                                })
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        mdialogview.home_searchbar_closed.setOnClickListener {
                            mAlertDialog.dismiss()
                        }

                    }

                } else {
                    layoutdisconnected!!.visibility = View.VISIBLE
                    home_loader?.visibility = View.GONE
                    home_layout?.visibility = View.GONE
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view
    }

    private fun ChefDetails() {
        try {
                RetrofitInstance.instence?.menu(
                    MenuRequest(
                        "",
                        "",
                        ""
                    )
                )!!.enqueue(object : Callback<MenuResponse> {
                    override fun onResponse(
                        call: Call<MenuResponse>,
                        response: Response<MenuResponse>
                    ) {
                        if (response.isSuccessful) {
                            bindImageView(
                                response.body()!!.data)
                            home_loader?.visibility = View.GONE
                            home_layout?.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<MenuResponse>, t: Throwable) {
                        Log.d("TAG", "onFailure: failed")
                    }
                })
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun bindSearchdata(data: List<SearchData>, searchrecycle: RecyclerView) {
        try {
                searchrecycle.layoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                searchadapter = SearchAdapter(requireContext(), data, this)
                searchrecycle.adapter = searchadapter
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun bindImageView(
        data: List<MenuData>) {
        try {
                recyclerView.layoutManager =
                    GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
                adapter = ImageAdapter(requireContext(), data, requireActivity().supportFragmentManager)
                recyclerView.adapter = adapter
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}