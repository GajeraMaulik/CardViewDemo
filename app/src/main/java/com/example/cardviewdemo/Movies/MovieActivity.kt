package com.example.cardviewdemo.Movies

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.cardviewdemo.Paging.PagingActivityViewModel
import com.example.cardviewdemo.Paging.PagingAdapter
import com.example.cardviewdemo.R
import com.example.cardviewdemo.adapter.BikeDetailAdapter
import com.example.cardviewdemo.adapter.ProductsAdapter
import com.example.cardviewdemo.listview.ItemClickListener
import com.example.cardviewdemo.retrofit.ProductImageActivity
import com.example.cardviewdemo.services.APIServices
import com.example.cardviewdemo.services.model.ImageSlider
import com.example.cardviewdemo.services.model.ProductsItem
import com.example.cardviewdemo.services.Client
import com.example.cardviewdemo.services.model.BikeDetails
import com.example.cardviewdemo.services.model.BikeDetailsItem
import com.example.cardviewdemo.slider.ImageSliderAdapter
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.activity_images.*
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.activity_paging.*
import kotlinx.android.synthetic.main.item_wallpapes.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MovieActivity : AppCompatActivity(),ItemClickListener {

    private var viewpager: ViewPager? = null
    var currentPage = 0
    var NUM_PAGES = 0

    lateinit var pagingAdapter: PagingAdapter
    lateinit var productsAdapter: ProductsAdapter
    lateinit var bikeDetailAdapter: BikeDetailAdapter
    lateinit var moviesItem: MoviesItem
    var imagesList:ArrayList<Root> = ArrayList()
    var productLists : ArrayList<ProductsItem> = ArrayList()
    var bikeDetailsList:ArrayList<BikeDetailsItem> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val actionBar = supportActionBar
        actionBar!!.title = "Products"
        actionBar.setDisplayHomeAsUpEnabled(true)

        viewpager = findViewById(R.id.addPager)
        pagingAdapter = PagingAdapter()
        productsAdapter = ProductsAdapter(this,productLists,this)


        initRecyclerView()

        initViewModel()

        initImageSlider()

        getData()

      //  getData1()

        getBikeDateils()


    }

    fun initRecyclerView(){
        rvMovieView.apply {
            //  layoutManager = LinearLayoutManager(this@PagingActivity)
            //  val decoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            //addItemDecoration(decoration)
            pagingAdapter= PagingAdapter()
            rvMovieView.adapter = pagingAdapter

        }
    }

    fun initViewModel(){
        val viewModel = ViewModelProvider(this).get(PagingActivityViewModel::class.java)
        lifecycleScope.launch {
            //pageProgressBar.visibility = View.VISIBLE
            delay(1800L)
            mProgressBar.visibility = View.GONE


        }
        lifecycleScope.launchWhenCreated {
            viewModel.getListData().collectLatest {

                pagingAdapter.submitData(it)


            }
        }

    }

    fun initImageSlider() {

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
        }, 2000, 2000)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(pos: Int) {}
        })
    }

    fun getData() {
        val retrofit = Client.getRetroInstance("https://fakestoreapi.com/").create(APIServices::class.java)
        val call = retrofit.getImages()
        call.enqueue(object :Callback<ArrayList<ProductsItem>>{
            //@SuppressLint("NotifyDataSetChanged")
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<ArrayList<ProductsItem>>, response: Response<ArrayList<ProductsItem>>) {

                productLists.clear()
                try {
                    Log.d("mmm123", "after--------->${response.body().toString()}")

                    if (response.isSuccessful ) {

                        Log.d("mmm123", "before --------->${response.body().toString()}")

                        val productList = response.body()
                       // progress_bar.visibility = View.GONE

                        productLists.addAll(productList!!)

                        productsAdapter = ProductsAdapter(this@MovieActivity, productLists,this@MovieActivity )
                        rvMovieView1.adapter = productsAdapter

                        Log.d("mmm123", "udsudsss$productLists")

                    } else {
                        progress_bar.visibility = View.INVISIBLE
                        no_Data.visibility = View.VISIBLE
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ArrayList<ProductsItem>>, t: Throwable) {
                Toast.makeText(this@MovieActivity, "Something went wrong $t", Toast.LENGTH_SHORT).show()
                Log.d("mmm123", "eroor  -----> $t")
            }

        })

    }

    fun getBikeDateils(){
        val retrofit = Client.getBikeDetails().create(APIServices::class.java)
        val call = retrofit.getBikeDetails()
        call.enqueue(object : Callback<ArrayList<BikeDetailsItem>> {
            override fun onResponse(call: Call<ArrayList<BikeDetailsItem>>, response: Response<ArrayList<BikeDetailsItem>> ) {
                bikeDetailsList.clear()
                try {
                    if (response.isSuccessful ) {

                        Log.d("mmm123", "before --------->${response.body().toString()}")

                        val productList = response.body()
                        // progress_bar.visibility = View.GONE

                        bikeDetailsList.addAll(productList!!)

                        bikeDetailAdapter = BikeDetailAdapter(this@MovieActivity,bikeDetailsList)
                        rvMovieView2.adapter = bikeDetailAdapter

                        Log.d("mmmmmmm", "udsudsss$bikeDetailsList")

                    } else {
                        progress_bar.visibility = View.INVISIBLE
                        no_Data.visibility = View.VISIBLE
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ArrayList<BikeDetailsItem>>, t: Throwable) {
            }



        })
    }

    fun getData1() {
        val retrofit = Client.getRetroInstance("https://fakestoreapi.com/").create(APIServices::class.java)
        val call = retrofit.getImages()
        call.enqueue(object :Callback<ArrayList<ProductsItem>>{
            //@SuppressLint("NotifyDataSetChanged")
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<ArrayList<ProductsItem>>, response: Response<ArrayList<ProductsItem>>) {

                productLists.clear()
                try {
                    Log.d("mmm123", "after--------->${response.body().toString()}")

                    if (response.isSuccessful ) {

                        Log.d("mmm123", "before --------->${response.body().toString()}")

                        val productList = response.body()
                        // progress_bar.visibility = View.GONE

                        productLists.addAll(productList!!)

                        productsAdapter = ProductsAdapter(this@MovieActivity, productLists,this@MovieActivity )
                        rvMovieView2.adapter = productsAdapter

                        Log.d("mmm123", "udsudsss$productLists")

                    } else {
                        progress_bar.visibility = View.INVISIBLE
                        no_Data.visibility = View.VISIBLE
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ArrayList<ProductsItem>>, t: Throwable) {
                Toast.makeText(this@MovieActivity, "Something went wrong $t", Toast.LENGTH_SHORT).show()
                Log.d("mmm123", "eroor  -----> $t")
            }

        })

    }

    override fun onItemsClick(position: Int, image: Int, text: ProductsItem, view: View) {
        val intent = Intent(this, ProductImageActivity::class.java)
        //val putExtra = intent.putExtra("item_detail", 0)
        intent.putExtra("image",text.getImages())
        //intent.putExtra("text", text.getImages())

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition_image")
        startActivity(intent, options.toBundle())
        // startActivity(intent)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

