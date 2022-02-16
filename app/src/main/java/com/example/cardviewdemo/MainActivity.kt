package com.example.cardviewdemo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.cardviewdemo.crudstatic.CRUDActivity
import com.example.cardviewdemo.listview.ListActivity
import com.example.cardviewdemo.progressbar.ProgressDialogActivity
import com.example.cardviewdemo.slider.ImageSlider
import com.example.cardviewdemo.slider.ImageSliderAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.viewpagerindicator.CirclePageIndicator
import java.util.*
import kotlin.collections.ArrayList


class MainActivity() : AppCompatActivity() {

    // lateinit var mViewPager : ViewPager


    private var viewpager: ViewPager? = null

    var currentPage = 0
    var NUM_PAGES = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager = findViewById(R.id.viewPager)

        initImageSlider()
    }

    private fun initImageSlider() {

        val imageList: ArrayList<ImageSlider> = ArrayList()

        imageList.add(ImageSlider("Image",
            "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1170&q=80"))
        imageList.add(ImageSlider("Logo",
            "https://images.unsplash.com/photo-1531297484001-80022131f5a1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1120&q=80"))
        imageList.add(ImageSlider("Logo",
            "https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1170&q=80"))
        imageList.add(ImageSlider("Logo",
            "https://images.unsplash.com/photo-1498050108023-c5249f4df085?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1172&q=80"))
        imageList.add(ImageSlider("Logo",
            "https://images.unsplash.com/photo-1496171367470-9ed9a91ea931?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1170&q=80"))
        imageList.add(ImageSlider("Logo",
            "https://images.unsplash.com/photo-1514826786317-59744fe2a548?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"))


        //Set the pager with an adapter
        viewpager?.adapter = ImageSliderAdapter(this, imageList)
        val indicator: CirclePageIndicator = findViewById<View>(R.id.indicator) as CirclePageIndicator
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
        indicator.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(pos: Int) {}
        })
    }

    fun listview(v: View?) {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
    }

    fun crudview(view: View) {
        val intent = Intent(this, CRUDActivity::class.java)
        startActivity(intent)
    }

    fun bsdview(view: View) {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this)
            .inflate(R.layout.activity_bottom_sheet_dialog,
                findViewById<ConstraintLayout>(R.id.bottomsheet))
        view.findViewById<View>(R.id.oder).setOnClickListener {
            Toast.makeText(this, "Oder Done", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<ImageView>(R.id.close).setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(view)
        dialog.show()
    }

    fun pdview(view: View){
        val intent = Intent(this, ProgressDialogActivity::class.java)
        startActivity(intent)

    }
    fun cameraView(view: View){
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)

    }



}



