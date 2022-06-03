package com.example.cardviewdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.databinding.ItemNewslistBinding
import com.example.cardviewdemo.databinding.ItemOrderinfoBinding
import com.example.cardviewdemo.services.model.*
import com.example.cardviewdemo.xmlparse.itemList

class NewsAdapter(
    val context: Context,
    val itemList: ArrayList<Item>,
    arrayOf: Array<String>,
    intArrayOf: IntArray,

    ) :RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val  binding=ItemNewslistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        with(holder){
            with(itemList[position]){
                val t =binding.Title.text.equals(this.getTitle()).toString()
                val l=binding.Link.text.equals(this.getLink()).toString()
                val p=binding.pubDate.text.equals(this.getPubdate()).toString()
                val d=binding.Description.text.equals(this.getDescription()).toString()


                this.setTitle(t)
                this.setLink(l)
                this.setPubDate(p)
                this.setDescription(d)
            }
     /*       with(sourceList[position]){
                binding.sourceLink.text=this.url
                binding.sourcetext.text=this.text

            }
            with(guidList[position]){
                binding.isPermaLink.text=this.isPermaLink
                binding.Guidtext.text=this.text

            }*/
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    inner class ViewHolder(val binding: ItemNewslistBinding):RecyclerView.ViewHolder( binding.root)

}