package com.example.cardviewdemo.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.icu.number.NumberRangeFormatter.with
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.cardviewdemo.R
import com.example.cardviewdemo.data.Image
import com.example.cardviewdemo.imagepicker.ImagePicker.Companion.with
import com.squareup.picasso.Picasso

class ImageAdapter(private  var items:ArrayList<Image>,private val context: Context): RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.images_views, parent, false))

    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        val item = items[position]

 /*   Glide.with(context).load(item)
        .placeholder(R.drawable.loading)
        .into(holder.imageView)

*/
            Picasso.get()
                .load(item.imageUrl)
                .placeholder(R.drawable.loading)
                .into(holder.imageView)


        holder.imageView.setOnClickListener {
            setupDialog(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_Image)
       /* val prog :ProgressBar = view.findViewById(R.id.progressBar)
        val noData:ImageView = view.findViewById(R.id.noData)*/
    }
    private fun setupDialog(item:Image) {
        val dialog = Dialog(context, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_wallpaper)
        val dialogImageView = dialog.findViewById<ImageView>(R.id.dialogImageView)
        val setWallpaperBtn = dialog.findViewById<Button>(R.id.setWallpaperBtn)
        val dialogProgressBar = dialog.findViewById<ProgressBar>(R.id.dialogProgressBar)

        dialogProgressBar.visibility = View.VISIBLE
        setWallpaperBtn.visibility = View.GONE

        //load image into Picasso
        Picasso.get().load(item.imageUrl).into(dialogImageView, object : com.squareup.picasso.Callback {
            override fun onSuccess() {
                setWallpaperBtn.visibility = View.VISIBLE
                dialogProgressBar.visibility = View.GONE
            }


            override fun onError(e: Exception?) {
                Log.d("errorLoad", e.toString())
            }

        })

       /* setWallpaperBtn.setOnClickListener {
            val wallpaperManager = WallpaperManager.getInstance(context) as WallpaperManager
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val bitmap: Bitmap = dialogImageView.drawable.toBitmap()

                    // for async set wallpaper
                    withContext(Dispatchers.IO) { wallpaperManager.setBitmap(bitmap) }
                    Toast.makeText(context, "Wallpaper successfully installed!", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    Toast.makeText(context, "Error: $e", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }


            }

        }
        dialog.show()*/

    }

}