package com.example.cardviewdemo.listview

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.cardviewdemo.R

class ImageActivity() : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
        {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_image)

            val actionBar= supportActionBar
            actionBar!!.title="ImageActivity"
            actionBar.setDisplayHomeAsUpEnabled(true)


            val ivImage=findViewById<ImageView>(R.id.item_image_view)
            val getImage = intent.getStringExtra("image")


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





