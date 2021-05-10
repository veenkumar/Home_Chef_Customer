package com.veen.donorsystem.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val url = "http://103.205.64.158/~nsystechlg/homechef/api/customer_api/"

    private var retrofit: Retrofit? = null
    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private fun getloginClient(): Retrofit? {
        retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        return retrofit
    }


    val instence: Api? = getloginClient()?.create(Api::class.java)
}
