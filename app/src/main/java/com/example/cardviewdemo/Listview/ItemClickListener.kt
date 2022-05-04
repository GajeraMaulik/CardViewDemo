package com.example.cardviewdemo.Listview

import android.view.View

interface ItemClickListener {
    fun onItemClick(position: Int, image: Int, text: String, view: View) {
    }


}
