package com.example.cardviewdemo.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.cardviewdemo.listview.ItemClickListener
import com.example.cardviewdemo.R

class ListAdapter (private val context: Context, private  val mItemClickListener: ItemClickListener) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {


    private val itemTitles = arrayOf(
        "Facebook plans to rebrand company with new name: Report",
        "China calls missile launch 'routine test' of new technology",
        "7 Cross-Industry Technology Trends That Will Disrupt the World",
        "Technology gives Hyderabad City Police the edge",
        "Importance of up skilling for women in technology sector",
        "Add L&T Technology Services, target price Rs 5350: HDFC Securities",
        "T20 World Cup: Bat-tracking technology set for debut",
        "Operational Technology in the Crosshairs"
    )

    private val itemDetails = arrayOf(
        "Facebook Inc., facing intense scrutiny over its business practices, is planning to rebrand the company with a new name that focuses on the metaverse, according to The Verge.",
        "China says its launch of a new spacecraft was merely a test to see whether the vehicle could be re-used",
        "Recent McKinsey analysis examines which technologies will have the most momentum in the next 10 years. These are the trends security teams need to know to protect their organizations effectively.",
        "Hyderabad: The increasing use of technology in day to day policing is one factor that has kept the Hyderabad City Police stay ahead of its counterparts. A look at the adoption of various technological applications, for crime detection and prevention and also to deliver services to the public, shows that the City Police is moving forward in leveraging web-based and state of art mobile technologies as tools in improving the operational efficiency of the force.",
        "According to a joint study by LinkedIn and World Economic Forum, women make up only 25% of the STEM (Science, Technology, Engineering, Math) workforce. Only 22% of artificial intelligence (AI) professionals and 12% of machine-learning (ML) experts are women. The already under-represented group is also the worst affected by the pandemic.",
        "L&T Technology Services Ltd., incorporated in the year 2012, is a Mid Cap company (having a market cap of Rs 53972.50 Crore) operating in IT Software sector.",
        "In a bid to provide an “unparalleled” experience to cricket fans all across the globe for the T20 World Cup 2021, the  International Cricket Council (ICC) is all set to introduce bat-tracking technology for the first time ever.",
        "Way back in 2015, I interviewed several officials working at utility companies for a column I was working on for Nextgov about why we had not at the time experienced a major attack against our critical infrastructure."
    )

    private val itemImages = intArrayOf(
        R.drawable.fb,
        R.drawable.image,
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4,

    )


    inner class ViewHolder(itemView: View ) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView
      // val textTitle: TextView
       //val textDes:TextView
        val cardview: CardView

        init {
            image = itemView.findViewById(R.id.item_image)
          // textTitle = itemView.findViewById(R.id.item_title)
         //   textDes = itemView.findViewById(R.id.item_details)
            cardview=itemView.findViewById(R.id.Card_View)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(v)
    }

    override  fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
      // holder.textTitle.text = itemTitles[position]
      // holder.textDes.text = itemDetails[position]
        holder.image.setImageResource(itemImages[position])
        holder.image.setOnClickListener{
            mItemClickListener.onItemClick(position, itemImages[position],itemDetails[position],holder.image)
           YoYo.with(Techniques.ZoomIn).duration(800).playOn(it)


        }

    }


    override fun getItemCount(): Int {
        return itemTitles.size
    }



 }






