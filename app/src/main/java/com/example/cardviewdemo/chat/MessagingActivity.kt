package com.example.cardviewdemo.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.adapter.MessageAdapter
import com.example.cardviewdemo.databinding.ActivityMessagingViewBinding
import com.example.cardviewdemo.services.APIServices
import com.example.cardviewdemo.services.model.Message
import com.example.cardviewdemo.services.model.NotificationData
import com.example.cardviewdemo.services.model.PushNotification
import com.example.cardviewdemo.services.model.UserProfile
import com.example.cardviewdemo.services.notifications.*
import com.example.cardviewdemo.viewModel.DatabaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.activity_messaging_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private lateinit var binding: ActivityMessagingViewBinding
private  lateinit  var messageList: ArrayList<Message>
private lateinit var mRef: DatabaseReference
var firebaseAuth: FirebaseAuth? = null
var receiverRoom: String? = null
var senderRoom:String? = null
var receiverUid : String? = null

var userName : String? = null

class MessagingActivity : AppCompatActivity() {
    var firebaseUser : FirebaseUser? = null
    private  lateinit var messageAdapter : MessageAdapter
    private lateinit var databaseViewModel:DatabaseViewModel
    var topic = ""
    var apiService : APIServices? = null
    var notify = false
    lateinit var name :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessagingViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
         name = intent.getStringExtra("Username").toString()
         userName = SharePref.getStringValue(this,"User").toString()

        actionBar!!.title = name
        actionBar.setDisplayHomeAsUpEnabled(true)

         receiverUid = intent.getStringExtra("Uid")
         firebaseUser = FirebaseAuth.getInstance().currentUser

       // firebaseUser = FirebaseAuth.getInstance().currentUser
        mRef = FirebaseDatabase.getInstance().reference

       // senderRoom = receiverUid + firebaseUser
     //   receiverRoom = firebaseUser + receiverUid


        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        binding.rvChatView.adapter = messageAdapter




        // logic for adding data to recyclerView
        readMessage(firebaseUser!!.uid,receiverUid!!)



        messageBox.setOnClickListener {
            d("box","hello")
            rvChatView.postDelayed({
                rvChatView.scrollToPosition((rvChatView.adapter as MessageAdapter).itemCount - 1)
            }, 500)
        }




        // adding the message to database
        ivSend.setOnClickListener {
            val message  = messageBox.text.toString()
            if (message.isNotEmpty()){
                Sendmessage(firebaseUser!!.uid,receiverUid!!, message,System.currentTimeMillis())
                messageBox.setText("")
                topic = "/topics/$receiverUid"
                PushNotification(NotificationData( userName!!,message),
                    topic).also {
                    sendNotification(it)
                }

            }else{
                d("TAG","$message")
                Toast.makeText(this,"Message Box Empty",Toast.LENGTH_LONG).show()
            }

        }


    }


   @SuppressLint("NotifyDataSetChanged")
    fun Sendmessage(senderId:String, receiverId:String, message: String, time:Long) {
       val messageObject = Message(message, senderId, receiverId, time)
       mRef.child("Chats").child(senderId).child("messages").push()
           .setValue(messageObject).addOnSuccessListener {
               mRef.child("Chats").child(receiverId).child("messages").push()
                   .setValue(messageObject)
           }
       binding.rvChatView.adapter!!.notifyDataSetChanged()
       messageBox.setText("")
       rvChatView.postDelayed({
           rvChatView.scrollToPosition((rvChatView.adapter as MessageAdapter).itemCount - 1)

       }, 500)
       d("TAG", "$message")

/*       val msg = Message()
       databaseViewModel.fetchingUserDataCurrent()
       databaseViewModel.fetchUserCurrentData!!.observe(this,
           Observer { dataSnapshot ->
            //   val users = dataSnapshot?.getValue(UserProfile::class.java)
               if (notify) {
                   sendNotification(receiverId, name, msg)
               }
               notify = false
           })*/

   }

    fun readMessage(senderID : String,receiverId: String) {
        mRef.child("Chats").child(senderID).child("messages")
            .addValueEventListener(object : ValueEventListener {

                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postsnapshot in snapshot.children) {
                        Log.d("data", "$postsnapshot")
                        val message = postsnapshot.getValue(Message::class.java)

                        if (message!!.senderId.equals(senderID) && message.receiverId.equals(receiverId) ||
                            message.senderId.equals(receiverId) && message.receiverId.equals(senderID)
                        ){
                            messageList.add(message)

                        }
                        rvChatView.postDelayed({
                            rvChatView.scrollToPosition((rvChatView.adapter as MessageAdapter).itemCount - 1)

                        }, 500)
                    }
                    binding.rvChatView.adapter?.notifyDataSetChanged()

                }


                override fun onCancelled(error: DatabaseError) {
                }

            })

      /*  mRef.child("Chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postsnapshot in snapshot.children) {
                        Log.d("data", "$postsnapshot")
                        val message = postsnapshot.getValue(Message::class.java)

                        messageList.add(message!!)

                        rvChatView.postDelayed({
                            rvChatView.scrollToPosition((rvChatView.adapter as MessageAdapter).itemCount - 1)

                        }, 500)
                    }
                    binding.rvChatView.adapter!!.notifyDataSetChanged()

                }


                override fun onCancelled(error: DatabaseError) {
                }

            })*/
    }
    private fun sendNotification(notification:PushNotification) = CoroutineScope(Dispatchers.IO).launch {
      /*  databaseViewModel.getTokenDatabaseRef()
        databaseViewModel.getTokenRefDb!!.observe(this,
            Observer { databaseReference ->
                val query: Query = databaseReference!!.orderByKey().equalTo(userId_receiver)
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val token: Token? = snapshot.getValue(Token::class.java)
                            val data = Data(firebaseUser.toString(), "$username: $msg", "New Message", userId_receiver)
                            assert(token != null)
                            val sender = Sender(data, token?.getToken())
                            apiService?.sendNotification(sender)
                                ?.enqueue(object : Callback<MyResponse?> {
                                    override fun onResponse(
                                        call: Call<MyResponse?>,
                                        response: Response<MyResponse?>,
                                    ) {
                                        if (response.code() == 200) {
                                            assert(response.body() != null)
                                            if (response.body()!!.success != 1) {
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<MyResponse?>, t: Throwable) {}
                                })
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            })*/
       try {
            Client.api = Client.client.create(APIServices::class.java)
            val response = Client.api.postNotification(notification)

          //  d("maulik","R$response")
            if(response.isSuccessful) {
               Log.d("m", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch(e: Exception) {
            Log.e("TAG", e.toString())
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    fun onBackKeyPressedOnKeyboard() {
        binding.messageBox.clearFocus()
    }
    private fun getUserName(): String? {
        val user = firebaseAuth?.currentUser
        return if (user != null) {
            user.displayName
        } else return null
    }
}







