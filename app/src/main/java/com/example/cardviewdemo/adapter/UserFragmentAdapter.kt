package com.example.cardviewdemo.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardviewdemo.R
import com.example.cardviewdemo.chat.*
import com.example.cardviewdemo.constants.Constants
import com.example.cardviewdemo.databinding.ActivityMessagingViewBinding
import com.example.cardviewdemo.fragments.BottomSheetProfileDetailUser
import com.example.cardviewdemo.services.model.Chats
import com.example.cardviewdemo.services.model.Users
import com.example.cardviewdemo.services.repository.FirebaseInstanceDatabase
import com.example.cardviewdemo.viewModel.DatabaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import org.chromium.base.ContextUtils.getApplicationContext
import java.io.IOException
import java.util.HashMap

class UserFragmentAdapter : RecyclerView.Adapter<UserFragmentAdapter.UserFragmentHolder> {

    var usersArrayList = ArrayList<Users>()
    lateinit var binding: ActivityMessagingViewBinding
    lateinit var firebaseInstanceDatabase: FirebaseInstanceDatabase

    lateinit var context: Context
    var chats = Chats()

    lateinit var databaseViewModel: DatabaseViewModel
    lateinit var bottomSheetProfileDetailUser: BottomSheetProfileDetailUser
    var isChat: Boolean = false

    val chatsArrayList =ArrayList<Chats>()
    var thelastmessage: String? = null

    constructor() {

    }

    constructor(usersArrayList: ArrayList<Users>, context: Context, isChat: Boolean) {
        this.usersArrayList = usersArrayList
        this.context = context
        this.isChat = isChat
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFragmentHolder {
        //  val inflater = LayoutInflater.from(context)
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.user_list_item_view, parent, false)
        databaseViewModel = DatabaseViewModel()

        return UserFragmentHolder(view)
    }

    override fun onBindViewHolder(holder: UserFragmentHolder, position: Int) {
        val users: Users = usersArrayList[position]
        val imageUrl: String = users.getImageUrl()
        val userName: String = users.getUsername()
        val bio: String = users.getBio()
        val user_status: String = users.getStatus()
        firebaseInstanceDatabase = FirebaseInstanceDatabase()

        if (isChat) {
            try {
                if (user_status.contains("online") && isNetworkConnected()) {
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
/*
        if (isChat) {
            lastmessage(users.getId(), holder.tv_last_message)
        } else holder.tv_last_message.visibility = View.GONE
*/

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




    class UserFragmentHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var iv_profile_image: CircleImageView = itemView.findViewById(R.id.profile_image)
        var tv_name: TextView = itemView.findViewById(R.id.user_name_list)
        var iv_status_user_list: ImageView = itemView.findViewById(R.id.iv_status_user_list)
        var tv_last_message: TextView = itemView.findViewById(R.id.last_message)

    }

}

