package com.veen.homechef.Activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.veen.donorsystem.api.RetrofitInstance
import com.veen.homechef.Adapter.MenuItemAdapter
import com.veen.homechef.Adapter.SubCategoryAdapter
import com.veen.homechef.Model.HomePage.menuitem.MenuItemData
import com.veen.homechef.Model.HomePage.menuitem.MenuItemRequest
import com.veen.homechef.Model.HomePage.menuitem.MenuitemResponse
import com.veen.homechef.Model.HomePage.subcategory.MenuSubData
import com.veen.homechef.Model.HomePage.subcategory.MenuSubRequest
import com.veen.homechef.Model.HomePage.subcategory.MenuSubResponse
import com.veen.homechef.R
import com.veen.homechef.Utils.AppUtils
import kotlinx.android.synthetic.main.activity_main_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainItemDetails : Fragment() {
    private var chefname: TextView? = null
    private var chefrecycler: RecyclerView? = null
    private var cheffooddetailsrecycler: RecyclerView? = null
    private lateinit var subadapter: SubCategoryAdapter
    private lateinit var adapter: MenuItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.activity_main_item, container, false)

        try {
            var gettoken = AppUtils.getsavetoken(requireContext())
            var getchefName = requireArguments().getString("chefName", "")
            var getfoodsubID = requireArguments().getString("foodsubID", "")
            var getchefID = AppUtils.getsavechefID(requireContext()).toInt()

//        AppUtils.savechefID(applicationContext, getchefID.toString())
            var getsavechefID = AppUtils.getsavechefID(requireContext())

            chefrecycler = root.findViewById(R.id.chef_details)
            chefname = root.findViewById(R.id.chef_chefname)
            cheffooddetailsrecycler = root.findViewById(R.id.chef_fooddetails)

            chefname!!.text = getchefName

            foodSubCategory()

            if (getfoodsubID != "0") {
                chefname!!.text = getchefName
                FoodMenu(gettoken, getsavechefID, getfoodsubID)
            }

            if (getfoodsubID == "") {
                chefname!!.text = getchefName
                FoodMenuItem(gettoken, getchefID)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return root
    }

    private fun FoodMenu(gettoken: String, getsavechefID: String, getfoodsubID: String) {
        try {
            RetrofitInstance.instence?.menuitem(MenuItemRequest(
                getsavechefID.toInt(),
                getfoodsubID.toInt()
            )
            )!!.enqueue(object : Callback<MenuitemResponse> {
                override fun onResponse(
                    call: Call<MenuitemResponse>,
                    response: Response<MenuitemResponse>
                ) {
                    if (response.body()!!.status == true) {
                        if (response.isSuccessful) {
                            bindchefdetails(response.body()!!.data)
                        }
                    }
                }

                override fun onFailure(call: Call<MenuitemResponse>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun FoodMenuItem(gettoken: String, getsavechefID: Int) {
        try {
            RetrofitInstance.instence?.menuitem(MenuItemRequest(
                getsavechefID,
                0
            )
            )!!.enqueue(object : Callback<MenuitemResponse> {
                override fun onResponse(
                    call: Call<MenuitemResponse>,
                    response: Response<MenuitemResponse>
                ) {
                    if (response.body()!!.status == true) {
                        if (response.isSuccessful) {
                            bindchefdetails(
                                response.body()!!.data
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<MenuitemResponse>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun foodSubCategory() {
        try {
            var getchefID = AppUtils.getsavechefID(requireContext()).toInt()

            RetrofitInstance.instence?.subcategory(MenuSubRequest(
                getchefID
            ))!!.enqueue(object : Callback<MenuSubResponse> {
                override fun onResponse(
                    call: Call<MenuSubResponse>,
                    response: Response<MenuSubResponse>
                ) {
                    if (response.isSuccessful) {
                        mainitemlayout.visibility = View.VISIBLE
                        progressBar_mainitem.visibility = View.GONE
                        if (response.isSuccessful) {
                            bindcheffooddetails(response.body()!!.data)
//                        Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<MenuSubResponse>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindcheffooddetails(data: List<MenuSubData>) {
        try {
            cheffooddetailsrecycler!!.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            subadapter= SubCategoryAdapter(requireContext(), data, requireActivity().supportFragmentManager)
            cheffooddetailsrecycler!!.adapter=subadapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun bindchefdetails(data: List<MenuItemData>) {
        try {
            chefrecycler!!.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter= MenuItemAdapter(requireContext(), data, this)
            chefrecycler!!.adapter=adapter
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}