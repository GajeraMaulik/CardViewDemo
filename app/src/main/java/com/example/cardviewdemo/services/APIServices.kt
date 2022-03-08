package com.example.cardviewdemo.services

import com.example.cardviewdemo.constants.Constants.Companion.CONTENT_TYPE
import com.example.cardviewdemo.constants.Constants.Companion.OAUTH_TOKEN
import com.example.cardviewdemo.constants.Constants.Companion.SERVER_KEY
import com.example.cardviewdemo.services.model.Message
import com.example.cardviewdemo.services.model.Notificationpush
import com.example.cardviewdemo.services.model.PushNotification
import com.example.cardviewdemo.services.notifications.MyResponse
import com.example.cardviewdemo.services.notifications.Sender
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

 interface APIServices {
        @Headers("Authorization: key =$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("/fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<MyResponse>


    @Headers("Authorization: Bearer $OAUTH_TOKEN","Content-type:$CONTENT_TYPE")
    @POST("/v1/projects/cardviewdemo-4027f/messages:send")
    suspend fun Notification(@Body notification: Message): Response<MyResponse>

      fun sendNotification(@Body body: Sender?): Call<MyResponse>

     //    fun sendNotification(@Body body: Sender?): Call<MyResponse?>?
     /*  @POST("/message")
   fun postMessage(@Body body: Message): Call<Void>

     companion object {
         private const val BASE_URL = "http://10.0.2.2:8080/"

         fun create(): APIServices {
             val retrofit = Retrofit.Builder()
                 .baseUrl(BASE_URL)
                 .addConverterFactory(GsonConverterFactory.create())
                 .build()
             return retrofit.create(APIServices::class.java)
         }
     }}*/
 }