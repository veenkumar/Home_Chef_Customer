package com.veen.homechef.Model.Profile.updateprofile

data class UpdateProfileRequest(
    val address: String,
    val city: String,
    val country: String,
    val id: String,
    val name: String,
    val phone: String,
    val pincode: String,
    val state: String
)