package com.example.cardviewdemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.R
import com.example.cardviewdemo.chat.channelid
import com.example.cardviewdemo.chat.userId_receiver
import com.example.cardviewdemo.chat.userId_sender
import com.example.cardviewdemo.fragments.ChatFragment
import com.example.cardviewdemo.fragments.unseen
import com.example.cardviewdemo.services.model.ChatList
import com.example.cardviewdemo.services.model.Chats
import com.example.cardviewdemo.services.repository.FirebaseInstanceDatabase
import com.example.cardviewdemo.util.DateUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import okhttp3.internal.assertThreadDoesntHoldLock
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

var  messsge:String?=null

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageHolder> {


    private val MSG_TYPE_LEFT_RECEIVED = 0
    private val MSG_TYPE_RIGHT_RECEIVED = 1
    private var context: Context? = null
     var firebaseInstanceDatabase= FirebaseInstanceDatabase()

    private var currentUser_sender: String? = null
    var chatArrayList: ArrayList<Chats> = ArrayList()
    var chatFragment=ChatFragment()

    constructor(){

    }
    constructor(chatArrayList: ArrayList<Chats>, context: Context?,senderId_receiverId: String) {
        this.chatArrayList = chatArrayList
        this.context = context
        this.currentUser_sender = senderId_receiverId
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.MessageHolder {
        return if (viewType == MSG_TYPE_RIGHT_RECEIVED) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            MessageHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            MessageHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
           val chats = chatArrayList[position]
       d("TAG","Message Adapter: ${chats.getMessage()}")
        messsge = chats.getMessage()
       val timeStamp: Long? = chats.getTimestamp()
       val isSeen: Boolean = chats.getSeen()
       //val intTimeStamp = timeStamp
       val time_msg_received:String = DateUtils.formatTime(timeStamp)
       holder.tv_time.text = time_msg_received
       holder.tv_msg.text = messsge

        if (position == chatArrayList.size -1) {
           if (isSeen) {
               holder.tv_seen.visibility = View.VISIBLE
               val seen:String = "Seen"
               holder.tv_seen.text = seen
               //chatList.setunreadcounter()


               firebaseInstanceDatabase.instance.child("ChatList").child("$userId_sender").child("$channelid")
                   .addListenerForSingleValueEvent(object : ValueEventListener{
                       override fun onDataChange(snapshot: DataSnapshot) {
                           if (snapshot.exists()) {
                               snapshot.child("unreadsmessage").ref.setValue(0)
                           }
                       }

                       override fun onCancelled(error: DatabaseError) {
                       }

                   })

/*
               val sender_unseen=HashMap<String,Any>()
               sender_unseen["unreadsmessage"]=0
               val receiver_unseen=HashMap<String,Any>()
               receiver_unseen["unreadsmessage"]= unseen
               if(chats.getCurrentuserId() == userId_sender && chats.getReceiverId()== userId_receiver || chats.getCurrentuserId()== userId_receiver && chats.getReceiverId()== userId_sender){
                   firebaseInstanceDatabase.instance.child("ChatList").child("$userId_sender")
                       .addListenerForSingleValueEvent(object : ValueEventListener {
                           override fun onDataChange(snapshot: DataSnapshot) {
                               if (chatList.getId().equals("$userId_sender")) {
                                   snapshot.child(channelid!!).ref.updateChildren(sender_unseen)
                               }else{
                                   snapshot.child(channelid!!).ref.updateChildren(receiver_unseen)
                               }
                           }

                           override fun onCancelled(error: DatabaseError) {
                           }

                       })
*/
           } else {
               holder.tv_seen.visibility = View.VISIBLE
               val delivered:String = "Delivered"
               holder.tv_seen.text = delivered

               firebaseInstanceDatabase.instance.child("ChatList").child("$userId_receiver").child("$channelid")
                   .addListenerForSingleValueEvent(object : ValueEventListener{
                       override fun onDataChange(snapshot: DataSnapshot) {
                           if (snapshot.exists()) {
                               snapshot.child("unreadsmessage").ref.setValue(unseen)
                           }
                       }

                       override fun onCancelled(error: DatabaseError) {
                       }

                   })
             //  chatFragment.getUnseenmessage()



           }
       } else {
           holder.tv_seen.visibility = View.GONE
       }

      /*  val currentMessage = chatArrayList?.get(position)
        val isSeen: Boolean?= currentMessage?.getseen()
        if (holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder

                holder.sentMessage.text = currentMessage!!.message.toString()
                if (isSeen == true) holder.tv_seen_sent.visibility = View.VISIBLE
                    val seen = "Seen"
                    holder.tv_seen_sent.text = seen
            //    holder.txtMyMessageTime.text = DateUtils.formatTime(currentMessage.time)
            try {
                holder.txtMyMessageTime.text = DateUtils.formatTime(currentMessage.gettimestamp())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }else{
            val viewHolder = holder as ReceiveViewHolder


                holder.receiveMessage.text = currentMessage?.message.toString()
            if (isSeen == false) holder.tv_seen_receive.visibility = View.VISIBLE
            val delivered = "Delivered"
            holder.tv_seen_receive.text = delivered

            //    holder.txtOtherUser.text = user.Username
//            holder.txtOtherMessageTime.text = DateUtils.formatTime(currentMessage.time)
            try {
                holder.txtOtherMessageTime.text = DateUtils.formatTime(currentMessage!!.gettimestamp())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/
    }




    fun timeStampConversionToTime(timeStamp: String): String {
        val date = Date(timeStamp)
        @SuppressLint("SimpleDateFormat") val jdf = SimpleDateFormat("hh:mm a")
        jdf.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return jdf.format(date)
    }

    override fun getItemCount(): Int {
        return chatArrayList.size
    }
/*    class  SentViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){

        val sentMessage:TextView =itemView.txt_sent_message
        val txtMyMessageTime:TextView = itemView.txtMyMessageTime
        val tv_seen_sent:TextView=itemView.tv_seen_sent



    }
    class  ReceiveViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val receiveMessage:TextView =itemView.txt_receive_message
        val txtOtherMessageTime:TextView = itemView.txtOtherMessageTime
        val tv_seen_receive:TextView =itemView.tv_seen_receive

    }*/

    class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_msg: TextView = itemView.findViewById(R.id.tv_chat_received)

        var tv_time: TextView  = itemView.findViewById(R.id.tv_chat_time_received)

        var tv_seen: TextView = itemView.findViewById(R.id.tv_seen)

    }

    override fun getItemViewType(position: Int): Int {
        return if (chatArrayList[position].getReceiverId().equals(currentUser_sender)) {
            MSG_TYPE_LEFT_RECEIVED
        } else  MSG_TYPE_RIGHT_RECEIVED
    }


}