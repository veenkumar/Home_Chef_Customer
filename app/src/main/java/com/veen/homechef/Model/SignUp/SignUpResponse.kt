package com.veen.homechef.Model.SignUp

data class SignUpResponse(
    val `data`: SignUpData,
    val msg: String,
    val status: Boolean,
    val token: String
)