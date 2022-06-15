package com.example.cardviewdemo.JsonParsing

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.adapter.ProductsAdapter
import com.example.cardviewdemo.databinding.ItemPostBinding
import com.example.cardviewdemo.databinding.ItemWallpapesBinding
import com.example.cardviewdemo.listview.ItemClickListener
import com.example.cardviewdemo.services.model.ProductsItem

class JsonParsingAdapter: RecyclerView.Adapter<JsonParsingAdapter.ViewHolder> {


    lateinit var context: Context
    lateinit var postList:ArrayList<PostItem>
    lateinit var mItemClickListener: ItemClickListener

    constructor()
    constructor(context: Context, postList: ArrayList<PostItem> /*mItemClickListener: ItemClickListener*/){
        this.context=context
        this.postList=postList
        //this.mItemClickListener=mItemClickListener
    }





    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): JsonParsingAdapter.ViewHolder {
        val  binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JsonParsingAdapter.ViewHolder, position: Int) {
        with(holder){
            with(postList[position]){

                binding.pUserId.text = userId.toString()
                binding.pId.text = id.toString()
                binding.pTitle.text = title
                binding.pBoby.text = body
            }
        }
    }


    override fun getItemCount(): Int {
        return postList.size
    }
    class ViewHolder(val binding: ItemPostBinding):RecyclerView.ViewHolder(binding.root)

}