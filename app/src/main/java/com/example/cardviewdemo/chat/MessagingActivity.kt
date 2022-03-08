package com.example.cardviewdemo.chat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.adapter.MessageAdapter
import com.example.cardviewdemo.databinding.ActivityMessagingViewBinding
import com.example.cardviewdemo.services.APIServices
import com.example.cardviewdemo.services.MessagingServices
import com.example.cardviewdemo.services.model.*
import com.example.cardviewdemo.services.model.Data
import com.example.cardviewdemo.services.notifications.*
import com.example.cardviewdemo.viewModel.DatabaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_messaging_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private lateinit var binding: ActivityMessagingViewBinding
private  lateinit  var mymessageList: ArrayList<Mymessage>
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
         val name = intent.getStringExtra("Username").toString()
         userName = SharePref.getStringValue(this,"User").toString()

        actionBar!!.title = name
        actionBar.setDisplayHomeAsUpEnabled(true)

         receiverUid = intent.getStringExtra("Uid")
         firebaseUser = FirebaseAuth.getInstance().currentUser

       // firebaseUser = FirebaseAuth.getInstance().currentUser
        mRef = FirebaseDatabase.getInstance().reference
        databaseViewModel = DatabaseViewModel()

        //senderRoom = receiverUid + firebaseUser
    //   receiverRoom = firebaseUser + receiverUid



        mymessageList = ArrayList()
        messageAdapter = MessageAdapter(this, mymessageList)
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
                Sendmessage(firebaseUser!!.uid, receiverUid!!, message,System.currentTimeMillis())
                    d("TAG","Me : $firebaseUser")
                topic = "/topics/${firebaseUser!!.uid}"
                    resetInput()
                MessagingServices.token?.let {
                   // Message(Data(firebaseUser!!.uid, message, "New Message",firebaseUser?.uid!!),NotificationX(message,firebaseUser!!.uid!!),it)
                    PushNotification(NotificationData(firebaseUser!!.uid,message),it)
                        .also {
                        sendNotification1(receiverUid!!,firebaseUser!!.uid,message)
                    }
                }
            }else{
                d("TAG","Message$message")
                Toast.makeText(this,"Message Box Empty",Toast.LENGTH_LONG).show()
            }

        }


    }
    private fun resetInput() {
        // Clean text box
        messageBox.text?.clear()

        // Hide keyboard
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }


   @SuppressLint("NotifyDataSetChanged")
    fun Sendmessage(senderId:String, receiverId:String, message: String, time:Long) {
       val messageObject = Mymessage(message, senderId, receiverId, time)
       mRef.child("Chats").child(senderId).child("messages").push()
           .setValue(messageObject).addOnSuccessListener {
               mRef.child("Chats").child(receiverId).child("messages").push()
                   .setValue(messageObject)
           }
       binding.rvChatView.adapter!!.notifyDataSetChanged()
       resetInput()
       rvChatView.postDelayed({
           rvChatView.scrollToPosition((rvChatView.adapter as MessageAdapter).itemCount - 1)

       }, 500)
       d("TAG", "Message: $message")


     /*  databaseViewModel.fetchingUserDataCurrent()
       databaseViewModel.fetchUserCurrentData!!.observe(this,
           Observer { dataSnapshot ->
               val users: UserProfile = dataSnapshot?.getValue(UserProfile::class.java)!!
               if (notify) {
                   sendNotification1(receiverId, users.getUsername()!!, message)
               }
               notify = false
           })*/

   }


    fun readMessage(senderID : String,receiverId: String) {
        mRef.child("Chats").child(senderID).child("messages")
            .addValueEventListener(object : ValueEventListener {

                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    mymessageList.clear()
                    for (postsnapshot in snapshot.children) {
                        Log.d("data", "$postsnapshot")
                        val message = postsnapshot.getValue(Mymessage::class.java)

                        if (message!!.senderId.equals(senderID) && message.receiverId.equals(receiverId) ||
                            message.senderId.equals(receiverId) && message.receiverId.equals(senderID)
                        ){
                            mymessageList.add(message)

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

   /*     mRef.child("Chats").child(senderRoom!!).child("messages")
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
    private fun sendNotification1(userId_receiver:String, username:String, msg: String){
            databaseViewModel.getTokenDatabaseRef()
        databaseViewModel.getTokenRefDb!!.observe(this) { databaseReference ->
            val query: Query = databaseReference!!.orderByKey().equalTo(userId_receiver)
            query.addValueEventListener(object : ValueEventListener {
                override  fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val token: Token? = snapshot.getValue(Token::class.java)
                        val data = Data("$username",
                            msg,
                            "New Message",
                            userId_receiver)
                        assert(token != null)
                        val sender = Sender(data, token?.getToken(token.token))
                        apiService!!.sendNotification(sender)
                            .enqueue(object : Callback<MyResponse?> {
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
        }
    }


    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
       try {
            Client.api = Client.client.create(APIServices::class.java)
           var notificationObject ="{\n" +
                   "    \"to\": \"foBd9K3bRFe3JcHTBB_7Ja:APA91bHUluTgYRwfJ0luPmTw5kLZ8sdaLKoIu8nuAWDHAaovpkQ9tAb-mqjFxzmmguMiHlrjbm0v5KiHx5WQAJsuuRU3FsTd5X8l_yAHONxQ8RLy0DN6Audv8ZjM_rvS6eTX9--x9QtR\",\n" +
                   "    \"notification\": {\n" +
                   "        \"title\": \"Check this Mobile (title)\",\n" +
                   "        \"body\": \"Rich Notification testing (body)\",\n" +
                   "        \"mutable_content\": true,\n" +
                   "        \"sound\": \"Tri-tone\"\n" +
                   "    },\n" +
                   "    \"data\": {\n" +
                   "        \"url\": \"<url of media image>\",\n" +
                   "        \"dl\": \"<deeplink action on tap of notification>\"\n" +
                   "    }\n" +
                   "}"
          //  val jsonData=toJson(notificationObject)
           val token: String? =MessagingServices.token
           //val notificationpush:Notificationpush=Notificationpush(notification.data,Notification("Messsage","XYZ"),token.toString())
           val oldResponse=Client.api.postNotification(notification)
           //d("TAG","Old Response :$oldResponse")
        //   val response = Client.api.Notification(notification)


// Send a message to the devices subscribed to the provided topic.

// Send a message to the devices subscribed to the provided topic.

            d("TAG","Response :$oldResponse")
          // val sender : Sender = Sender(response,t)
//            if(response.isSuccessful) {
//               Log.d("m", "Response: ${Gson().toJson(response)}")
//            } else {
//                Log.e("TAG", response.errorBody()!!.string())
//            }
        } catch(e: Exception) {
            Log.e("TAG", e.toString())
        }
    }
    private fun toJson(data:String): JSONObject {

        var newData: String = data.replace("\"","")
        newData = newData.replace("{","")
        newData = newData.replace("}","")

        val newObject = newData.split(":")
        val name = newObject[0]
        val value = newObject[1]
        val rootObject = JSONObject()
        rootObject.put(name,value)

        return rootObject
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    fun onBackKeyPressedOnKeyboard() {
        binding.messageBox.clearFocus()
    }



}









