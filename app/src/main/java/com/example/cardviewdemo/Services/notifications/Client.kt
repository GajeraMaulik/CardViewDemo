package com.example.cardviewdemo.Services.notifications

import com.example.cardviewdemo.Services.APIServices
import okhttp3.CertificatePinner
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Client {
  /*  private val retrofit: Retrofit? = null

    fun getClient(url: String?): Retrofit? {
        return retrofit
            ?: Retrofit.Builder()
                .baseUrl(url!!)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }*/
//    companion object{
//        //private val retrofit =
//
//
//
//      val api: APIServices =
//          Retrofit.Builder()
//              .baseUrl(Constants.BASE_URL)
//              .addConverterFactory(GsonConverterFactory.create())
//              .build().create(APIServices::class.java)
//
//    }
   // object ApiClient {
       // private lateinit var interceptor: HttpLoggingInterceptor
        private lateinit var okHttpClient: OkHttpClient
        lateinit var api:APIServices
        private var retrofit: Retrofit? = null



        fun getClient(url:String): Retrofit
                 {
            //    interceptor = HttpLoggingInterceptor()
             //   interceptor.level = HttpLoggingInterceptor.Level.BODY


                val certificatePinner: CertificatePinner = CertificatePinner.Builder()
                    .add("api.github.com", "sha256/WoiWRyIOVNa9ihaBciRSC7XHjliYS9VwUGOIud4PB18=")
                    .build()

                okHttpClient = OkHttpClient.Builder()
                  //  .addInterceptor(interceptor)
                   // .addInterceptor(interceptor)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .callTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    //.certificatePinner(certificatePinner)

                    /* .connectionSpecs(Arrays.asList(
                         ConnectionSpec.MODERN_TLS,
                         ConnectionSpec.COMPATIBLE_TLS))
                     .followRedirects(true)
                     .followSslRedirects(true)
                     .retryOnConnectionFailure(true)
                     .connectTimeout(20, TimeUnit.SECONDS)
                     .readTimeout(20, TimeUnit.SECONDS)
                     .writeTimeout(20, TimeUnit.SECONDS)
                     .cache(null)*/
                    .build()

                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClient)
                        .build()
                }


                return retrofit!!

            }

        fun toRequestBody(value: String): RequestBody {
            return value.toRequestBody("text/plain".toMediaTypeOrNull())
        }

     /*   fun header(): HashMap<String, Any> {
            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["os-name"] = CommonUtils.getOSName()
            hashMap["os-version"] = CommonUtils.getOSVersion()
            hashMap["platform"] = "Android"
            hashMap["AppVersion"] = BuildConfig.VERSION_CODE
            hashMap["AppBuildVersion"] = BuildConfig.VERSION_NAME
            return hashMap
        }*/

    //}
}