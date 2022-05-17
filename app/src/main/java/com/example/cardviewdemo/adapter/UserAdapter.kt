package com.example.cardviewdemo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.R
import com.example.cardviewdemo.chat.MessagingActivity
import com.example.cardviewdemo.services.model.Users
class UserAdapter(val context:Context, private val userList: ArrayList<Users>): RecyclerView.Adapter<UserAdapter.UserviewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserviewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserviewHolder(view)
    }

    override fun onBindViewHolder(holder: UserviewHolder, position: Int) {
         val currentUser = userList[position]
        holder.textName.text = currentUser.getUsername()

        holder.itemView.setOnClickListener {
            val intent = Intent(context,MessagingActivity::class.java)

            intent.putExtra("Username",currentUser.getUsername())
            intent.putExtra("Uid",currentUser.getId())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  userList.size
    }

    class UserviewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val textName : TextView = itemView.findViewById(R.id.txt_name)
    }


}