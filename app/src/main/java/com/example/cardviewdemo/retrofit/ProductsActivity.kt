package com.example.cardviewdemo.retrofit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.cardviewdemo.adapter.ProductsAdapter
import com.example.cardviewdemo.databinding.ActivityImagesBinding
import com.example.cardviewdemo.listview.ItemClickListener
import com.example.cardviewdemo.services.APIServices
import com.example.cardviewdemo.services.model.*
import com.example.cardviewdemo.services.notifications.Client
import kotlinx.android.synthetic.main.activity_images.*
import kotlinx.android.synthetic.main.item_wallpapes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductsActivity() : AppCompatActivity(), ItemClickListener {
    lateinit var binding: ActivityImagesBinding
    lateinit var adapter:ProductsAdapter

     var imagesList:ArrayList<ProductsItem> = ArrayList()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Products"
        actionBar.setDisplayHomeAsUpEnabled(true)


        getData()

    }

    fun getData() {
        val retrofit = Client.getRetroInstance("https://fakestoreapi.com/").create(APIServices::class.java)
        val call = retrofit.getImages()
        call.enqueue(object :Callback<ArrayList<ProductsItem>>{
            //@SuppressLint("NotifyDataSetChanged")
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<ArrayList<ProductsItem>>, response: Response<ArrayList<ProductsItem>>) {

                imagesList.clear()
                try {
                    d("mmm123", "after--------->${response.body().toString()}")

                    if (response.isSuccessful ) {

                        d("mmm123", "before --------->${response.body().toString()}")

                        val productList = response.body()
                        progress_bar.visibility = View.GONE

                        imagesList.addAll(productList!!)

                        adapter = ProductsAdapter(this@ProductsActivity, imagesList,this@ProductsActivity )
                        rvProductView.adapter = adapter

                        d("mmm123","udsudsss$imagesList")

                    } else {
                        progress_bar.visibility = View.INVISIBLE
                        no_Data.visibility = View.VISIBLE
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }

        }

            override fun onFailure(call: Call<ArrayList<ProductsItem>>, t: Throwable) {
                Toast.makeText(this@ProductsActivity, "Something went wrong $t", Toast.LENGTH_SHORT).show()
                d("mmm123","eroor  -----> $t")
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




