package com.example.cardviewdemo.services

import android.util.Log.d
import okhttp3.CertificatePinner
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.simplexml.SimpleXmlConverterFactory
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
        val baseUrl = "https://burgers1.p.rapidapi.com/"
            //"https://best-manga-anime-wallpapers.p.rapidapi.com"

    fun getRetroInstance(url :String): Retrofit {
        val YOURKEY = "0a9c666ed1msh5ab2e3f50223725p117176jsn0b13bc7107e4"

        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)


        okHttpClient = OkHttpClient.Builder()
            //  .addInterceptor(interceptor)
            // .addInterceptor(interceptor)
            .connectTimeout(310, TimeUnit.SECONDS)
            .callTimeout(310, TimeUnit.SECONDS)
            .connectTimeout(310, TimeUnit.SECONDS)
            .readTimeout(310, TimeUnit.SECONDS)
            .writeTimeout(310, TimeUnit.SECONDS)
          /*  .addInterceptor { chain->
                val request =chain.request().newBuilder().addHeader("Authorization","$YOURKEY").build()
                chain.proceed(request)
            }*/
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

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(url)

                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }


        val apiServices: APIServices by lazy {
            retrofit.create(APIServices::class.java)
        }
        return retrofit
    }


/*        public static OwnerDetailService getOwnerDetails() {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

            ownerDetailService = new Retrofit.Builder()
                .baseUrl("https://www.tradetu.com/bus_api/public/api/v1/vaahan/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(OwnerDetailService.class);

            return ownerDetailService;

        */


        fun getClient(url:String): Retrofit {
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
    fun getNews(url: String):Retrofit{

         val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        okHttpClient = OkHttpClient.Builder()
            //  .addInterceptor(interceptor)
            // .addInterceptor(interceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)

            .writeTimeout(120, TimeUnit.SECONDS)
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

        if (retrofit == null){
         retrofit = Retrofit.Builder()
             .client(okHttpClient)
            .baseUrl(url)
             .addConverterFactory(SimpleXmlConverterFactory.create())
//             .addConverterFactory(
//                 TikXmlConverterFactory.create(
//                     TikXml.Builder()
//                         .exceptionOnUnreadXml(false)
//                         .addTypeConverter(String.javaClass, HtmlEscapeStringConverter())
//                         .build()
//                 )
//             )
            .build()
            d("url","$url")
        }
        return retrofit!!

    }

    fun getPaging():Retrofit {
        val BASE_URL = "https://rickandmortyapi.com/api/"
        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val apiServices: APIServices by lazy {
            retrofit.create(APIServices::class.java)
        }
        return retrofit
    }

    fun getBikeDetails():Retrofit{
        val BASE_URL = "https://community-citybikes.p.rapidapi.com/"
        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val apiServices: APIServices by lazy {
            retrofit.create(APIServices::class.java)
        }
        return retrofit
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