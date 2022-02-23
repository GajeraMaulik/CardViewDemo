package com.example.cardviewdemo.chat

import android.annotation.SuppressLint
import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardviewdemo.R
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.adapter.MessageAdapter
import com.example.cardviewdemo.data.App
import com.example.cardviewdemo.data.Message
import com.example.cardviewdemo.data.UserProfile
import com.example.cardviewdemo.databinding.ActivityChatBinding
import com.example.cardviewdemo.databinding.ActivityChatViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat_view.*
import java.util.*
import kotlin.collections.ArrayList

private lateinit var binding: ActivityChatViewBinding
private  lateinit var adapter: MessageAdapter
private lateinit var messageList: ArrayList<Message>
private lateinit var mRef: DatabaseReference

var receiverRoom: String? = null
var senderRoom:String? = null
class ChatViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_view)
        binding = ActivityChatViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("Username")
        val receiverUid = intent.getStringExtra("Uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom  = senderUid + receiverUid

        supportActionBar?.title = name

        messageList = ArrayList()
        adapter = MessageAdapter(this, messageList)

        rvChatView.layoutManager = LinearLayoutManager(this)
        rvChatView.adapter = adapter

        // logic for adding data to recyclerView
        mRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{

                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postsnapshot in snapshot.children){
                        Log.d("data","$postsnapshot")
                        val message = postsnapshot.getValue(Message::class.java)

                       messageList.add(message!!)
                    }
                    adapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        // adding the message to database
        sentBtn.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject =Message(message,senderUid,Calendar.getInstance().timeInMillis)
            mRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }
    }
}