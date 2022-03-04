package com.example.cardviewdemo.services

import com.example.cardviewdemo.constants.Constants.Companion.CONTENT_TYPE
import com.example.cardviewdemo.constants.Constants.Companion.SERVER_KEY
import com.example.cardviewdemo.services.model.NotificationData
import com.example.cardviewdemo.services.model.PushNotification
import com.example.cardviewdemo.services.notifications.MyResponse
import com.example.cardviewdemo.services.notifications.Sender
import okhttp3.ResponseBody
import org.jetbrains.annotations.NotNull
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

 interface APIServices {
    /*@Headers("Content-Type:application/json",
        "Authorization:key=AAAAA98HE5A:APA91bFqJUuvhONzRp0Mu_mJgvHAeFuHgf_LE-PuGA0kY7F5enxLcqaAE_5bY0UxP4LS7t5-3tzFz0BV696H4qKZCGKfLZY56P5VIWhxeHc-Q7VZk9GOmZY2oj97LR9HQ8Ub1RentG-Y")*/
     @Headers("Authorization: key =$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("/fcm/send")

    suspend fun postNotification(@Body notification:PushNotification): Response<MyResponse>

   //    fun sendNotification(@Body body: Sender?): Call<MyResponse?>?
}