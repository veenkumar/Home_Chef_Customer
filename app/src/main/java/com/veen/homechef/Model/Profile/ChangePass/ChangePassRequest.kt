package com.veen.homechef.Model.Profile.ChangePass

data class ChangePassRequest(
    val new_password: String,
    val old_password: String,
    val user_id: Int
)