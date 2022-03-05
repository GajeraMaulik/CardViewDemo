package com.example.cardviewdemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.R
import com.example.cardviewdemo.chat.userName
import com.example.cardviewdemo.services.model.Message
import com.example.cardviewdemo.services.model.UserProfile
import com.example.cardviewdemo.util.DateUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.sent.view.*
class MessageAdapter(val context: Context, private val messageList:ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        if (viewType == 1){

            val view:View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return ReceiveViewHolder(view)

        }else{
            val view:View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)


        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]
        if (holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder

            holder.sentMessage.text = currentMessage.message
        //    holder.txtMyMessageTime.text = DateUtils.formatTime(currentMessage.time)
            try {
                holder.txtMyMessageTime.text = DateUtils.formatTime(currentMessage.time)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }else{
            val viewHolder = holder as ReceiveViewHolder

            holder.receiveMessage.text = currentMessage.message
        //    holder.txtOtherUser.text = user.Username
//            holder.txtOtherMessageTime.text = DateUtils.formatTime(currentMessage.time)
            try {
                holder.txtOtherMessageTime.text = DateUtils.formatTime(currentMessage.time)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class  SentViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){

        val sentMessage:TextView =itemView.txt_sent_message
        val txtMyMessageTime:TextView = itemView.txtMyMessageTime



    }
    class  ReceiveViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val receiveMessage:TextView =itemView.findViewById(R.id.txt_receive_message)
//        val txtOtherUser:TextView = itemView.findViewById(R.id.txtOtherUser)
        val txtOtherMessageTime:TextView = itemView.findViewById(R.id.txtOtherMessageTime)

    }

}