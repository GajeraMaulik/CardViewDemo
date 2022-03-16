package com.example.cardviewdemo.services.notifications

import com.example.cardviewdemo.services.APIServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client1 {
     val retrofit: Retrofit? = null

    lateinit var api: APIServices

    fun getClient(url: String): Retrofit {
        return retrofit ?: Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}