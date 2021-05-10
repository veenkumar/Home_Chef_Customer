package com.veen.homechef.Model.SignUp

data class SignUpRequest(
    val email: String,
    val name: String,
    val password: String,
    val phone_no: String
)