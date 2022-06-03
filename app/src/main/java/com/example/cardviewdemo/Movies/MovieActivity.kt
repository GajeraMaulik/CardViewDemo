package com.example.cardviewdemo.Movies

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.cardviewdemo.R
import com.example.cardviewdemo.services.APIServices
import com.example.cardviewdemo.services.model.ImageSlider
import com.example.cardviewdemo.services.notifications.Client
import com.example.cardviewdemo.slider.ImageSliderAdapter
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_images.*
import kotlinx.android.synthetic.main.activity_movie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MovieActivity : AppCompatActivity() {

    private var viewpager: ViewPager? = null
    var currentPage = 0
    var NUM_PAGES = 0

    lateinit var adapter: MoviesAdapter
    var imagesList:ArrayList<Movies> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        viewpager = findViewById(R.id.addPager)

        initImageSlider()
        getMovies()
    }

    fun getMovies(){
        val retrofit = Client.getRetroInstance("https://movie-details1.p.rapidapi.com/imdb_api/").create(APIServices::class.java)
        val call = retrofit.getMovies()
        call.enqueue(object : Callback<ArrayList<Movies>> {
            //@SuppressLint("NotifyDataSetChanged")
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<ArrayList<Movies>>, response: Response<ArrayList<Movies>>) {

                imagesList.clear()
                try {
                    Log.d("mmm123", "after--------->${response.body().toString()}")

                    if (response.isSuccessful ) {

                        Log.d("mmm123", "before --------->${response.body().toString()}")

                        val productList = response.body()
                        progress_bar.visibility = View.GONE

                        imagesList.addAll(productList!!)

                        adapter = MoviesAdapter(this@MovieActivity,imagesList)
                        rvMovieView.adapter = adapter

                        Log.d("mmm123", "udsudsss$imagesList")

                    } /*else {
                        progress_bar.visibility = View.INVISIBLE
                        no_Data.visibility = View.VISIBLE
                    }*/
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ArrayList<Movies>>, t: Throwable) {
                Toast.makeText(this@MovieActivity, "Something went wrong $t", Toast.LENGTH_SHORT).show()
                Log.d("mmm123", "eroor  -----> $t")
            }

        })


    }



    private fun initImageSlider() {

        val imageList: ArrayList<ImageSlider> = ArrayList()

        imageList.add(ImageSlider("Image",
            "https://cdn.vectorstock.com/i/1000x1000/55/09/special-offer-banner-with-person-shopping-vector-23245509.webp"))
        imageList.add(ImageSlider("Logo",
            "https://thumbs.dreamstime.com/z/grocery-shopping-promotional-sale-banner-fast-shopping-cart-full-fresh-colorful-food-grocery-shopping-promotional-sale-banner-168812786.jpg"))
        imageList.add(ImageSlider("Logo",
            "https://i.pinimg.com/474x/97/2b/89/972b897ce5ecd3537de401d6a24a0d84.jpg"))
        imageList.add(ImageSlider("Logo",
            "https://image.shutterstock.com/image-vector/sale-banner-template-design-260nw-487080769.jpg"))
        imageList.add(ImageSlider("Logo",
            "https://media.istockphoto.com/vectors/sale-template-best-offer-banner-sale-50-percent-off-shopping-vector-vector-id1313822264"))



        //Set the pager with an adapter
        viewpager?.adapter = ImageSliderAdapter(this, imageList)
        val indicator: CirclePageIndicator = findViewById<View>(R.id.addindicator) as CirclePageIndicator
        indicator.setViewPager(viewpager)
        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.radius = 5 * density
        NUM_PAGES = imageList.size

        // Auto start of viewpager
        val handler = Handler(Looper.getMainLooper())
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            viewpager?.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 1000, 1000)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(pos: Int) {}
        })
    }

}