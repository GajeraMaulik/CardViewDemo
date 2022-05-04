package com.example.cardviewdemo.Services

import com.example.cardviewdemo.Services.notifications.MyResponse
import com.example.cardviewdemo.Services.notifications.Sender
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIServices {
    @Headers("Content-Type:application/json",
        "Authorization:key=AAAAkDonE_M:APA91bFvZOr69axqTEKhdlkP95_XPTDDJ7MqhfX1iM6QiyAfFyWdEGnzImOMmSU4IVQh8DnXpr36o5KYXGZp5-hRX2lZJkN-WVm3lx_dcyPtVDkkgZ5qjWTif5vwnGrGcbJL0_glZuOo")
               // "AAAAA98HE5A:APA91bFqJUuvhONzRp0Mu_mJgvHAeFuHgf_LE-PuGA0kY7F5enxLcqaAE_5bY0UxP4LS7t5-3tzFz0BV696H4qKZCGKfLZY56P5VIWhxeHc-Q7VZk9GOmZY2oj97LR9HQ8Ub1RentG-Y")
    @POST("fcm/send")
    fun sendNotification(@Body body: Sender?): Call<MyResponse>
}
