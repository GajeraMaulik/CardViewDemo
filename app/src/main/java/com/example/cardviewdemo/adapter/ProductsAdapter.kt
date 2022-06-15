package com.example.cardviewdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.cardviewdemo.R
import com.example.cardviewdemo.databinding.ItemWallpapesBinding
import com.example.cardviewdemo.listview.ItemClickListener
import com.example.cardviewdemo.services.model.*
import kotlinx.android.synthetic.main.item_wallpapes.view.*

class ProductsAdapter:RecyclerView.Adapter<ProductsAdapter.ViewHolder>{

    lateinit var context:Context
    lateinit var productList:ArrayList<ProductsItem>
    lateinit var mItemClickListener:ItemClickListener
constructor()
    constructor( context: Context,  productList: ArrayList<ProductsItem>,  mItemClickListener: ItemClickListener){
        this.context=context
        this.productList=productList
        this.mItemClickListener=mItemClickListener
    }

 var rate: ProductsItem.Rating
    init {
        rate= ProductsItem.Rating()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdapter.ViewHolder {

        val  binding= ItemWallpapesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        //return holder.bind(productList.get(position))

        with(holder) {
            with(productList[position]) {
/*               Picasso.get()
                    .load(thumbnail)
                    // .resize(512,512)
                    .placeholder(R.drawable.loading)
                    .into(holder.itemView.images)*/
                Glide.with(context).load(image)
                    .placeholder(R.drawable.loading)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemView.image_view)

                binding.pTitle.text = title
                binding.pPrice.text = price.toString()
              //  binding.pDescription.text =description
                binding.pCategory.text = category
              //  binding.pRate.text = rate.rate.toString()
                //binding.pCount.text = rate.count.toString()

                itemView.image_view.setOnClickListener {
                    mItemClickListener.onItemsClick(position,0,productList[position],holder.itemView.image_view)
                }




            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

      class ViewHolder(val binding: ItemWallpapesBinding) : RecyclerView.ViewHolder(binding.root)
}


