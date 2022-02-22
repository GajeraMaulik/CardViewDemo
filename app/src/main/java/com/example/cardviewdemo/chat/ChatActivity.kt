package com.example.cardviewdemo.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardviewdemo.R
import com.example.cardviewdemo.adapter.MessageAdapter
import com.example.cardviewdemo.data.Message
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val actionBar= supportActionBar
        actionBar!!.title="Chat"
        actionBar.setDisplayHomeAsUpEnabled(true)

        initFirebase()

        setupSendButton()

        createFirebaseListener()

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun initFirebase() {
        //init firebase
        FirebaseApp.initializeApp(applicationContext)

//        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG)

        //get reference to our db
        databaseReference = FirebaseDatabase.getInstance().reference
    }
    private fun createFirebaseListener(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val toReturn: ArrayList<Message> = ArrayList();

                for(data in dataSnapshot.children){
                    val messageData = data.getValue<Message>(Message::class.java)

                    //unwrap
                    val message = messageData?.let { it } ?: continue

                    toReturn.add(message)
                }

                //sort so newest at bottom
                toReturn.sortBy { message ->
                    message.timestamp
                }

                setupAdapter(toReturn)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //log error
            }
        }
        databaseReference?.child("messages")?.addValueEventListener(postListener)
    }
    private fun setupAdapter(data: ArrayList<Message>){
        val linearLayoutManager = LinearLayoutManager(this)
        rvChatView.layoutManager = linearLayoutManager
        rvChatView.adapter = MessageAdapter(data) {
            Toast.makeText(this, "${it.message} clicked", Toast.LENGTH_SHORT).show()
        }

        //scroll to bottom
        rvChatView.scrollToPosition(data.size - 1)
    }
    private fun setupSendButton() {
        sendButton.setOnClickListener {
            if (!mainActivityEditText.text.toString().isEmpty()){
                sendData()
            }else{
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun sendData(){
        databaseReference?.
        child("messages")?.
        child(java.lang.String.valueOf(System.currentTimeMillis()))?.
        setValue(Message(mainActivityEditText.text.toString()))

        //clear the text
        mainActivityEditText.setText("")
    }


}