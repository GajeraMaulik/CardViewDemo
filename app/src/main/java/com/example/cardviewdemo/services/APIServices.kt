package com.example.cardviewdemo.services

import android.annotation.SuppressLint
import androidx.annotation.XmlRes
import com.example.cardviewdemo.Movies.*
import com.example.cardviewdemo.Paging.RickandMortyList
import com.example.cardviewdemo.services.model.*
import com.example.cardviewdemo.services.notifications.MyResponse
import com.example.cardviewdemo.services.notifications.Sender
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import retrofit2.Call
import retrofit2.http.*
import com.example.cardviewdemo.services.model.Item as Item


interface APIServices {
    @Headers("Content-Type:application/json",
        "Authorization:key=AAAAkDonE_M:APA91bFvZOr69axqTEKhdlkP95_XPTDDJ7MqhfX1iM6QiyAfFyWdEGnzImOMmSU4IVQh8DnXpr36o5KYXGZp5-hRX2lZJkN-WVm3lx_dcyPtVDkkgZ5qjWTif5vwnGrGcbJL0_glZuOo")
               // "AAAAA98HE5A:APA91bFqJUuvhONzRp0Mu_mJgvHAeFuHgf_LE-PuGA0kY7F5enxLcqaAE_5bY0UxP4LS7t5-3tzFz0BV696H4qKZCGKfLZY56P5VIWhxeHc-Q7VZk9GOmZY2oj97LR9HQ8Ub1RentG-Y")
    @POST("fcm/send")
    fun sendNotification(@Body body: Sender?): Call<MyResponse>

   // @GET("rssfeedstopstories.cms")
   // fun getFeed(): Call<Feed?>?
    @POST("rss?hl=en-IN&gl=IN&ceid=IN:en")
    fun getNews():Call<News>

    @POST("rss?hl=en-IN&gl=IN&ceid=IN:en")
    fun getChannel():Call<Channel>

    @SuppressLint("SupportAnnotationUsage")
    @Headers("Content-Type:application/Xml")
    @GET("rss?hl=en-IN&gl=IN&ceid=IN:en")
    @XmlRes
    fun getItem():Call<Item>


    @POST("rss?hl=en-IN&gl=IN&ceid=IN:en")
    fun getSource(@Body body: Source):Call<Source>


    /*@POST("rss?hl=en-IN&gl=IN&ceid=IN:en")
    fun getGuid(@Body body:Guid):Call<Guid>*/



    //https://gorest.co.in/public-api/users
    @GET("users")
    @Headers("Accept:application/json","Content-Type:application/json")
    fun getUsersList(): Call<UserList>

    //https://gorest.co.in/public-api/users?name=a
    @GET("users")
    @Headers("Accept:application/json","Content-Type:application/json")
    fun searchUsers(@Query("name") searchText: String): Call<UserList>


    //https://gorest.co.in/public-api/users/121
    @GET("users/{user_id}")
    @Headers("Accept:application/json","Content-Type:application/json")
    fun getUser(@Path("user_id") user_id: String): Call<UserResponse>


    @POST("users")
    @Headers("Accept:application/json", "Content-Type:application/json",
        "Authorization: Bearer 73668350bdf06c66f3388408c1a712b378c3e25da599753b21b664a6261e246c")
    fun createUser(@Body params: User): Call<UserResponse>

    @PATCH("users/{user_id}")
    @Headers("Accept:application/json", "Content-Type:application/json",
        "Authorization: Bearer 73668350bdf06c66f3388408c1a712b378c3e25da599753b21b664a6261e246c")
    fun updateUser(@Path("user_id") user_id: String, @Body params: User): Call<UserResponse>

    @DELETE("users/{user_id}")
    @Headers("Accept:application/json", "Content-Type:application/json",
        "Authorization: Bearer 73668350bdf06c66f3388408c1a712b378c3e25da599753b21b664a6261e246c")
    fun deleteUser(@Path("user_id") user_id: String): Call<UserResponse>

   @GET("products")
  /*  @Headers("Accept:application/json","Content-Type:application/json",
        " X-RapidAP-Key : 0a9c666ed1msh5ab2e3f50223725p117176jsn0b13bc7107e4")*/
   fun getImages():Call<ArrayList<ProductsItem>>

   @GET("movies")
   @Headers("X-RapidAPI-Key:0a9c666ed1msh5ab2e3f50223725p117176jsn0b13bc7107e4")
   fun getMovies() :Call<MoviesItem>

   @GET("recent?page=0&sensitivity=0")
   @Headers("X-RapidAPI-Key:0a9c666ed1msh5ab2e3f50223725p117176jsn0b13bc7107e4")
   fun getItems():Call<ArrayList<Root>>

   @GET("character")
   suspend fun  getDataFromApi(@Query("page") query : Int): RickandMortyList

   @GET("valenbisi.json")
   @Headers("X-RapidAPI-Key:0a9c666ed1msh5ab2e3f50223725p117176jsn0b13bc7107e4")
   fun getBikeDetails() : Call<ArrayList<BikeDetailsItem>>
}
