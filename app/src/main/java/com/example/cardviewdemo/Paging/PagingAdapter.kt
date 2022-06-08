package com.example.cardviewdemo.Paging

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardviewdemo.R
import kotlinx.android.synthetic.main.item_movies.view.*

class PagingAdapter:PagingDataAdapter<CharaterData, PagingAdapter.ViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  binding= LayoutInflater.from(parent.context).inflate(R.layout.item_movies,parent,false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(getItem(position)!!)
        Log.d("501010", "505050     ---->${getItem(position)}")

    }
    override fun getItemCount(): Int {
        return  super.getItemCount()
    }



    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val pimgeview :ImageView =view.findViewById(R.id.pImageView)
        val tvname :TextView =view.findViewById(R.id.tvName)
        val tvDec :TextView =view.findViewById(R.id.tvDec)

        fun bind( data: CharaterData){
                tvname.text=data.name
                tvDec.text=data.species
                Glide.with(pimgeview).load(data.image).placeholder(R.drawable.loading).circleCrop().into(itemView.pImageView)
        }
    }

    class DiffUtilCallBack:DiffUtil.ItemCallback<CharaterData>(){
        override fun areItemsTheSame(oldItem: CharaterData, newItem: CharaterData): Boolean {
            return oldItem.name ==newItem.name
        }

        override fun areContentsTheSame(oldItem: CharaterData, newItem: CharaterData): Boolean {
                return oldItem.name ==newItem.name && oldItem.species == newItem.species
        }

    }


}

