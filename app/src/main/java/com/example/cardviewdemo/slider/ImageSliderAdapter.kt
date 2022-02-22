package com.example.cardviewdemo.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.cardviewdemo.R
import com.example.cardviewdemo.data.ImageSlider

class ImageSliderAdapter(private val mContext: Context, private val imageList: List<ImageSlider>) :
    PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(mContext)
    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val view: View = inflater.inflate(R.layout.item_image_slider, collection, false) as ViewGroup
        val imageSlider = view.findViewById<ImageView>(R.id.ivSlide)
        Glide.with(mContext).load(imageList[position].strImage).apply(RequestOptions()
            .signature(ObjectKey(imageList[position]))) // here you add some value , if the next time you add the same value then it will load from cache otherwise if you put new value you will download , then save in cache
            .into(imageSlider)
        collection.addView(view)
        return view
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return imageList[position].name
    }


}