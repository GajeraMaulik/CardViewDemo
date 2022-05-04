package com.example.cardviewdemo.Listview


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.cardviewdemo.R


class ImageAdapter(
    private val items: ArrayList<String>,
    private val context: Context,
    private val mItemClickListener: ItemClickListener,
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
   /* private val itemTitles = arrayOf(
        "Facebook plans to rebrand company with new name: Report",
        "China calls missile launch 'routine test' of new technology",
        "7 Cross-Industry Technology Trends That Will Disrupt the World",
        "Technology gives Hyderabad City Police the edge",
        "Importance of up skilling for women in technology sector",
        "Add L&T Technology Services, target price Rs 5350: HDFC Securities",
        "T20 World Cup: Bat-tracking technology set for debut",
        "Operational Technology in the Crosshairs"
    )*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = items[position]
        //holder.textTitle.text = itemTitles[position]
      //  holder.textTitle.text = currentItem

        //set Image dynamic
        Glide.with(context).load(currentItem).apply(RequestOptions()
             .placeholder(R.drawable.loading)
            .signature(ObjectKey(currentItem))) // here you add some value , if the next time you add the same value then it will load from cache otherwise if you put new value you will download , then save in cache
            .into(holder.image)


        holder.image.setOnClickListener {
            mItemClickListener.onItemClick(position, 0, currentItem, holder.image)
        }


    }


    override fun getItemCount(): Int {
        return items.size
    }
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.item_image)
       // val textTitle: TextView = itemView.findViewById(R.id.item_title)
    }

}




