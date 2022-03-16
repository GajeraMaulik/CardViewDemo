package com.example.cardviewdemo.services

import com.example.cardviewdemo.services.notifications.MyResponse
import com.example.cardviewdemo.services.notifications.Sender
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIServices {
     @Headers("Content-Type:application/json",
         "Authorization:key=AAAAA98HE5A:APA91bFqJUuvhONzRp0Mu_mJgvHAeFuHgf_LE-PuGA0kY7F5enxLcqaAE_5bY0UxP4LS7t5-3tzFz0BV696H4qKZCGKfLZY56P5VIWhxeHc-Q7VZk9GOmZY2oj97LR9HQ8Ub1RentG-Y")
     @POST("fcm/send")
     fun sendNotification(@Body body: Sender?): Call<MyResponse?>
    /*    @Headers("Authorization: key =$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("/fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<MyResponse>


    @Headers("Authorization: Bearer $OAUTH_TOKEN","Content-type:$CONTENT_TYPE")
    @POST("/v1/projects/cardviewdemo-4027f/messages:send")
    suspend fun Notification(@Body notification: Message): Response<MyResponse>

      fun sendNotification(@Body body: Sender?): Call<MyResponse>*/

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