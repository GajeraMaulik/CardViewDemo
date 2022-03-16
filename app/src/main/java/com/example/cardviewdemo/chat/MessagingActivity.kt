package com.example.cardviewdemo.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cardviewdemo.R
import com.example.cardviewdemo.adapter.MessageAdapter
import com.example.cardviewdemo.databinding.ActivityMessagingViewBinding
import com.example.cardviewdemo.fragments.BottomSheetProfileDetailUser
import com.example.cardviewdemo.services.APIServices
import com.example.cardviewdemo.services.model.*
import com.example.cardviewdemo.services.model.Data
import com.example.cardviewdemo.services.notifications.*
import com.example.cardviewdemo.viewModel.DatabaseViewModel
import com.example.cardviewdemo.viewModel.LogInViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_messaging_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException



var receiverRoom: String? = null
var senderRoom:String? = null
lateinit var userId_receiver // userId of other user who'll receive the text // Or the user id of profile currently opened
        : String
lateinit var userId_sender // current user id
        : String
class MessagingActivity : AppCompatActivity() {
    lateinit var binding: ActivityMessagingViewBinding

    lateinit var logInViewModel: LogInViewModel
    lateinit var databaseViewModel: DatabaseViewModel


    var profileUserNAme: String? = null
    var profileImageURL: String? = null
    var bio: String? = null
    var currentFirebaseUser: FirebaseUser? = null


    var chat: String? = null
    var timeStamp: Long? = null

    var user_status: String? = null
    var messageAdapter: MessageAdapter? = null
      private lateinit var chatsArrayList: ArrayList<Mymessage>
    lateinit var context: Context
    var bottomSheetProfileDetailUser: BottomSheetProfileDetailUser? = null

    lateinit var client1: Client1
    lateinit var apiService: APIServices
    var notify = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_messaging_view)
        window.statusBarColor = ContextCompat.getColor(this,R.color.colorChat)

        binding = ActivityMessagingViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId_receiver = intent.getStringExtra("userid")!!
        userId_sender = FirebaseAuth.getInstance().currentUser.toString()

        senderRoom = userId_receiver + userId_sender
        receiverRoom = userId_sender + userId_receiver


        init()
        getCurrentFirebaseUser()
        fetchAndSaveCurrentProfileTextAndData()

        chatsArrayList = ArrayList()
        messageAdapter = MessageAdapter(chatsArrayList, context, userId_sender)
        recycler_view_messages_record.adapter = messageAdapter

        binding.ivUserImage.setOnClickListener {
            profileUserNAme?.let { it1 ->
                profileImageURL?.let { it2 ->
                    bio?.let { it3 ->
                        openBottomSheetDetailFragment(it1,
                            it2,
                            it3)
                    }
                }
            }
        }


        binding.ivSendButton.setOnClickListener {
            notify = true
            chat = binding.etChat.text.toString().trim { it <= ' ' }
            if (chat != "") {
                addChatInDataBase()
            } else {
                Toast.makeText(this@MessagingActivity, "Message can't be empty.", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.etChat.setText("")
        }



    }

    private fun openBottomSheetDetailFragment(username: String, imageUrl: String, bio: String) {
        bottomSheetProfileDetailUser =
            BottomSheetProfileDetailUser(username, imageUrl, bio)
        assert(supportActionBar != null)
        bottomSheetProfileDetailUser!!.show(supportFragmentManager, "edit")
    }

    private fun getCurrentFirebaseUser() {
        logInViewModel.firebaseUserLogInStatus
        logInViewModel.firebaseUserLoginStatus.observe(this
        ) { firebaseUser ->
            currentFirebaseUser = firebaseUser
            userId_sender = currentFirebaseUser!!.uid
        }
    }


    private fun fetchAndSaveCurrentProfileTextAndData() {
      /*  if (userId_receiver == null) {
            userId_receiver = intent.getStringExtra("userId")
        }*/
        databaseViewModel.fetchSelectedUserProfileData(userId_receiver)
        databaseViewModel.fetchSelectedProfileUserData?.observe(this
        ) { dataSnapshot ->
            val user = dataSnapshot.getValue(Users::class.java)!!
            profileUserNAme = user.getUsername()
            profileImageURL = user.getImageUrl()
            bio = user.getBio()
            user_status = user.getStatus()
            try {
                if (user_status!!.contains("online") && isNetworkConnected()) {
                    binding.ivUserStatusMessageView.setBackgroundResource(R.drawable.online_status)
                } else {
                    binding.ivUserStatusMessageView.setBackgroundResource(R.drawable.offline_status)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            binding.tvProfileUserName.text = profileUserNAme
            if (profileImageURL == "default") {
                binding.ivUserImage.setImageResource(R.drawable.sample_img)
            } else {
                Glide.with(applicationContext).load(profileImageURL).into(binding.ivUserImage)
            }
            fetchChatFromDatabase(userId_receiver, userId_sender)
        }
        addIsSeen()
    }

    fun addIsSeen() {
        val isSeen = "seen"
        databaseViewModel.fetchChatUser()
        databaseViewModel.fetchedChat?.observe(this
        ) { dataSnapshot ->
            for (dataSnapshot1 in dataSnapshot!!.children) {
                d("TAG","data : $dataSnapshot")
                val chats = dataSnapshot1.getValue(Chats::class.java)
                if (chats?.getSenderId().equals(userId_receiver) && chats?.getReceiverId().equals(userId_sender)) {
                    databaseViewModel.addIsSeenInDatabase(isSeen, dataSnapshot1)
                    d("TAG","data1: $dataSnapshot1")
                }
            }
        }
    }


    @Throws(InterruptedException::class, IOException::class)
    fun isNetworkConnected(): Boolean {   //check internet connectivity
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchChatFromDatabase(myId: String, senderId: String) {
        databaseViewModel.fetchChatUser()
        databaseViewModel.fetchedChat?.observe(this
        ) { dataSnapshot ->
            chatsArrayList.clear()
            for (snapshot in dataSnapshot!!.children) {


                val chats:Mymessage= snapshot.getValue(Mymessage::class.java)!!
                d("TAG","chat :$chats")

                if (chats.getReceiverId() == senderId && chats.getSenderId() == myId || chats.getReceiverId() == myId && chats.getSenderId() == senderId) {
                        chatsArrayList.add(chats)
                   }

                d("TAG","chats :$chatsArrayList")

                messageAdapter = MessageAdapter(chatsArrayList, context, userId_sender)
                recycler_view_messages_record.adapter = messageAdapter
                recycler_view_messages_record.postDelayed({
                    recycler_view_messages_record.scrollToPosition((recycler_view_messages_record.adapter as MessageAdapter).itemCount - 1)

                }, 500)
                //binding.recyclerViewMessagesRecord.adapter?.notifyDataSetChanged()

            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addChatInDataBase() {
       // val msg = chat

        val tsLong = System.currentTimeMillis()
        timeStamp = tsLong
        databaseViewModel.addChatDb(userId_receiver, userId_sender, chat, timeStamp)
        databaseViewModel.successAddChatDb?.observe(this
        ) { aBoolean ->
            if (aBoolean) {


                // Toast.makeText(MessageActivity.this, "Sent.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this@MessagingActivity, "Message can't be sent.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        databaseViewModel.fetchingUserDataCurrent()
        databaseViewModel.fetchUserCurrentData?.observe(this
        ) { dataSnapshot ->
            val users = dataSnapshot.getValue(Users::class.java)
            assert(users != null)
            if (notify) {
                if (chat != null) {
                    sendNotification(userId_receiver,userId_sender, chat!!)
                }
            }
            notify = false
        }
    }

    private fun sendNotification(userId_receiver: String?, username: String?, msg: String) {
        databaseViewModel.getTokenDatabaseRef()
        databaseViewModel.getTokenRefDb?.observe(this
        ) { databaseReference ->
            val query = databaseReference.orderByKey().equalTo(userId_receiver)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val token = snapshot.getValue(Token::class.java)
                        val data = Data(userId_sender,R.drawable.ic_baseline_textsms_24,"$username:$msg","New Message",userId_receiver)
                        assert(token != null)
                        val sender = Sender(data,token?.token)
                        apiService.sendNotification(sender)
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

                                override fun onFailure(
                                    call: Call<MyResponse?>,
                                    t: Throwable,
                                ) {
                                }
                            })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }


    private fun init() {
      //  databaseViewModel = DatabaseViewModel()
      //  logInViewModel = LogInViewModel()
        val actionBar= supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)

        chatsArrayList = ArrayList()


        databaseViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                application))[DatabaseViewModel::class.java]
        logInViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                application))[LogInViewModel::class.java]
        context = this@MessagingActivity
        client1 = Client1()
        apiService = client1.getClient("https://fcm.googleapis.com/").create(APIServices::class.java)

        binding.ivBackButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, UsersActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        })

    }

    private fun currentUser(userid: String) {
        val editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit()
        editor.putString("currentuser", userid)
        editor.apply()
    }


    private fun addStatusInDatabase(status: String) {
        databaseViewModel.addStatusInDatabase("status", status)
    }

    override fun onResume() {
        super.onResume()
        addStatusInDatabase("online")
        currentUser(userId_receiver!!)
    }

    override fun onPause() {
        super.onPause()
        addStatusInDatabase("offline")
        currentUser("none")
    }
    fun onBackKeyPressedOnKeyboard() {
        binding.etChat.clearFocus()
    }


}










