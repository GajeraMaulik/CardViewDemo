package com.example.cardviewdemo.chat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardviewdemo.R
import com.example.cardviewdemo.adapter.MessageAdapter
import com.example.cardviewdemo.data.Message
import com.example.cardviewdemo.databinding.ActivityChatViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat_view.*
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
       // setContentView(R.layout.activity_chat_view)
        binding = ActivityChatViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar= supportActionBar
        val name = intent.getStringExtra("Username")
        actionBar!!.title = name
        actionBar.setDisplayHomeAsUpEnabled(true)


        val receiverUid = intent.getStringExtra("Uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mRef = FirebaseDatabase.getInstance().reference

        senderRoom = receiverUid + senderUid
        receiverRoom  = senderUid + receiverUid


        messageList = ArrayList()
        adapter = MessageAdapter(this, messageList)

        rvChatView.layoutManager = LinearLayoutManager(this)
      //  adapter.setHasStableIds(true)
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
        messageBox.setOnClickListener {
            rvChatView.postDelayed({
                rvChatView.scrollToPosition((rvChatView.adapter as MessageAdapter).itemCount - 1)

            },500 )
        }

        // adding the message to database
        ivSend.setOnClickListener {
            val message  = messageBox.text.toString()
            if (message.isNotEmpty()){
                val messageObject = Message(message,senderUid,System.currentTimeMillis())
                mRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)

                    }
                messageBox.setText("")
                rvChatView.postDelayed({
                    rvChatView.scrollToPosition((rvChatView.adapter as MessageAdapter).itemCount - 1)

                },500 )

                d("TAG","$message")

            }else{
                d("TAG","$message")
                Toast.makeText(this,"Message Box Empty",Toast.LENGTH_LONG).show()
            }


        }


    }
      override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun onBackKeyPressedOnKeyboard() {
        binding.messageBox.clearFocus()
    }
}