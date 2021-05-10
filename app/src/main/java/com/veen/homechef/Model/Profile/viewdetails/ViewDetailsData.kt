package com.veen.homechef.Model.Profile.viewdetails

data class ViewDetailsData(
    val address: String,
    val currency: String,
    val discount: Int,
    val email: String,
    val item_data: List<ViewDetailsItemData>,
    val name: String,
    val order_id: String,
    val phone_no: String,
    val pincode: String,
    val sub_total_amount: Int,
    val shipping_charge: Int,
    val total_amount: Int
)