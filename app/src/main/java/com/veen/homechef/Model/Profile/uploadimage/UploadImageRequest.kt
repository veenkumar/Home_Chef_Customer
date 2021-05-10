package com.veen.homechef.Model.Profile.uploadimage

data class UploadImageRequest(
    val base64_img: String,
    val user_id: Int
)