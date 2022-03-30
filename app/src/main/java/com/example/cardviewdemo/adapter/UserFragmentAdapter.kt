package com.example.cardviewdemo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardviewdemo.R
import com.example.cardviewdemo.chat.MessagingActivity
import com.example.cardviewdemo.fragments.BottomSheetProfileDetailUser
import com.example.cardviewdemo.services.model.Users
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

class UserFragmentAdapter : RecyclerView.Adapter<UserFragmentAdapter.UserFragmentHolder> {

      var usersArrayList = ArrayList<Users>()
     lateinit var  context: Context
     lateinit var bottomSheetProfileDetailUser: BottomSheetProfileDetailUser
      var isChat: Boolean = false
    constructor(){

    }
     constructor(usersArrayList: ArrayList<Users>,context: Context,isChat: Boolean)
    {
        this.usersArrayList = usersArrayList
        this.context = context
        this.isChat = isChat
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFragmentHolder {
      //  val inflater = LayoutInflater.from(context)
        val view: View =LayoutInflater.from(context).inflate(R.layout.user_list_item_view, parent, false)
        return UserFragmentHolder(view)
    }

    override fun onBindViewHolder(holder: UserFragmentHolder, position: Int) {
        val users: Users = usersArrayList[position]
        val imageUrl: String = users.getImageUrl()
        val userName: String = users.getUsername()
        val bio: String = users.getBio()
        val user_status: String = users.getStatus()
        if (isChat) {
            try {
                if (user_status.contains("online")  && isNetworkConnected()) {
                    holder.iv_status_user_list.setBackgroundResource(R.drawable.online_status)
                } else {
                    holder.iv_status_user_list.setBackgroundResource(R.drawable.offline_status)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            holder.iv_status_user_list.visibility = View.GONE
        }
        if (imageUrl == "default") {
            holder.iv_profile_image.setImageResource(R.drawable.sample_img)
        } else {
            Glide.with(context).load(imageUrl).into(holder.iv_profile_image)
        }
        holder.tv_name.text = userName
        holder.iv_profile_image.setOnClickListener(View.OnClickListener {
            bottomSheetProfileDetailUser =
                BottomSheetProfileDetailUser(userName, imageUrl, bio)
            val manager = (context as AppCompatActivity?)!!.supportFragmentManager
            bottomSheetProfileDetailUser.show(manager, "edit")
        })
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MessagingActivity::class.java)
            intent.putExtra("userId", users.getId())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return usersArrayList.size
    }


    @Throws(InterruptedException::class, IOException::class)
    fun isNetworkConnected(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }



    class UserFragmentHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_profile_image: CircleImageView = itemView.findViewById(R.id.profile_image)
        var tv_name: TextView = itemView.findViewById(R.id.user_name_list)
        var iv_status_user_list: ImageView = itemView.findViewById(R.id.iv_status_user_list)

    }

}