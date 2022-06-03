package com.example.cardviewdemo.listview

import android.view.View
import com.example.cardviewdemo.services.model.ProductsItem

interface ItemClickListener {
    fun onItemClick(position: Int, image: Int, text: String, view: View) {
    }
    fun onItemsClick(position: Int,image: Int,text:ProductsItem,view: View){

    }


}
