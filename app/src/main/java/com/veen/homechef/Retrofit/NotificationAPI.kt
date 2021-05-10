package com.veen.homechef.Retrofit

import com.veen.homechef.firebase.Constants.Companion.CONTENT_TYPE
import com.veen.homechef.firebase.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=AAAAtISKfqg:APA91bHaoxXNs_y5y_2OfsVAIvBcv1k6Yvsv5PmI1tJHX6hMV4tGs-LnU_7e0KavZjMufPBIKebfRjDFUSa_uHmTstcWvCZaQJraHr9YNxrzBHlhA79HKtPkdHouojdNCWTV7ZkN7pOu", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}