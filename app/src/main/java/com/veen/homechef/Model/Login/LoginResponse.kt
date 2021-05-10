package com.veen.homechef.Model.Login

data class LoginResponse(
    val `data`: LoginData,
    val msg: String,
    val status: Boolean,
    val token: String
)