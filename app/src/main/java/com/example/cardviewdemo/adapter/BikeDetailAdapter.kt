package com.example.cardviewdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.databinding.ItemBikedetailsBinding
import com.example.cardviewdemo.databinding.ItemWallpapesBinding
import com.example.cardviewdemo.services.model.BikeDetails
import com.example.cardviewdemo.services.model.BikeDetailsItem

class BikeDetailAdapter(val context: Context,val bikedetailList:ArrayList<BikeDetailsItem>): RecyclerView.Adapter<BikeDetailAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ):ViewHolder {
        val  binding= ItemBikedetailsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BikeDetailAdapter.ViewHolder, position: Int) {
        with(holder){
            with(bikedetailList[position]){
                binding.bName.text = name
                binding.bBikes.text=bikes.toString()
                binding.bLat.text=lat.toString()
                binding.bFree.text=free.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return bikedetailList.size
    }
    class ViewHolder(val binding: ItemBikedetailsBinding):RecyclerView.ViewHolder(binding.root){

    }
}