package com.example.cardviewdemo.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.cardviewdemo.R
import kotlinx.android.synthetic.main.activity_show_image.*

class ProductImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)

        val actionBar= supportActionBar
        actionBar!!.title="ShowImageActivity"
        actionBar.setDisplayHomeAsUpEnabled(true)


        val ivImage=findViewById<ImageView>(R.id.product_Item)
        val getImage = intent.getStringExtra("image")

        progress_bar_Show.visibility = View.GONE

        Glide.with(this)
            .load(getImage)
            .apply(RequestOptions().signature(ObjectKey(getImage!!))) // here you add some value , if the next time you add the same value then it will load from cache otherwise if you put new value you will download , then save in cache
            .into(ivImage)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}