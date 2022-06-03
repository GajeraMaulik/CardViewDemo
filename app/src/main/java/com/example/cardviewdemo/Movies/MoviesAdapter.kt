package com.example.cardviewdemo.Movies

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.cardviewdemo.R
import com.example.cardviewdemo.databinding.ItemMoviesBinding
import kotlinx.android.synthetic.main.item_movies.view.*

class MoviesAdapter(var context: Context,var moviesList:ArrayList<Movies>):RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.ViewHolder {
        val  binding= ItemMoviesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesAdapter.ViewHolder, position: Int) {
        with(holder){
            with(moviesList[position]){
                Glide.with(context).load(image)
                    .placeholder(R.drawable.loading)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemView.movieImage)

                binding.mTitle.text = director_names.toString()

            }
        }
    }



    override fun getItemCount(): Int {
        return moviesList.size
    }

    class ViewHolder(val binding:ItemMoviesBinding):RecyclerView.ViewHolder(binding.root) {

    }
}