package com.example.cardviewdemo.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.ProgressBar
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
import com.example.cardviewdemo.services.model.Chats
import com.example.cardviewdemo.services.model.Users
import com.example.cardviewdemo.services.notifications.*
import com.example.cardviewdemo.services.repository.FirebaseInstanceDatabase
import com.example.cardviewdemo.viewModel.DatabaseViewModel
import com.example.cardviewdemo.viewModel.LogInViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_messaging_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


var newReceiver: String? = ""
var newCurrentuser:String? = ""
var Currentuser:String? = ""
var ReceiverUser:String?=""
var messageRoom:String?=""
var messageRoom1:String?=""
var userId_receiver // userId of other user who'll receive the text // Or the user id of profile currently opened
        : String? = null
 var userId_sender // current user id
        : String? = null


class MessagingActivity : AppCompatActivity() {
    lateinit var binding: ActivityMessagingViewBinding
    lateinit var client: Client
    lateinit var apiService: APIServices


    lateinit var logInViewModel: LogInViewModel
    lateinit var databaseViewModel: DatabaseViewModel
    lateinit var firebaseInstanceDatabase: FirebaseInstanceDatabase

    var profileUserNAme: String? = null
    var profileImageURL: String? = null
    var bio: String? = null
    lateinit var currentFirebaseUser: FirebaseUser
    var chats = Chats()

    var user =Users()
    var chat: String =""
    var timeStamp: Long? = null

    var user_status: String? = null
    lateinit var progressBar: ProgressBar
    var messageAdapter: MessageAdapter? = null
      private lateinit var chatsArrayList: ArrayList<Chats>
    var bottomSheetProfileDetailUser: BottomSheetProfileDetailUser? = null
    var notify = false
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this,R.color.colorChat)

        binding = ActivityMessagingViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId_receiver = intent.getStringExtra("userId")!!
        userId_sender  = intent.getStringExtra("user")





        firebaseInstanceDatabase = FirebaseInstanceDatabase()
        firebaseInstanceDatabase.instance.child("Chats").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //  if (snapshot.exists()){
                for (datasnapchot in snapshot.children){
                    val key = datasnapchot.key.toString().split("_")
                    Currentuser = key[0]
                    ReceiverUser =key[1]

                    if (userId_sender == Currentuser   && userId_receiver == ReceiverUser){
                        newCurrentuser = Currentuser
                        newReceiver = ReceiverUser

                        d("if","if")

                    }else if (userId_receiver == Currentuser   && userId_sender == ReceiverUser  ){
                        newCurrentuser = Currentuser
                        newReceiver = ReceiverUser
                        d("if","elseif")
                    }

                    d("key","newFirst name :$Currentuser")
                    d("key","new Second name :$ReceiverUser")
                    d("key","current :$userId_sender")
                    d("key","Receiver:$userId_receiver")
                    d("key","newCurrentuser:$newCurrentuser")
                    d("key","newReceiver:$newReceiver")
                }
                // }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })



        iv_user_image.setOnClickListener {
            profileUserNAme?.let { it1 ->
                profileImageURL?.let { it2 ->
                    bio?.let { it3 ->
                        openBottomSheetDetailFragment(it1, it2, it3)
                    }
                }
            }
        }

        et_chat.setOnClickListener {
            d("box","hello")
            recycler_view_messages_record.postDelayed({
                recycler_view_messages_record.scrollToPosition((recycler_view_messages_record.adapter as MessageAdapter).itemCount - 1)
            }, 100)
        }

         iv_send_button.setOnClickListener {
            notify = true
            chat =et_chat.text.toString().trim { it <= ' ' }
            if (chat != "") {
                addChatInDataBase()

            } else {
                Toast.makeText(this@MessagingActivity, "Message can't be empty.", Toast.LENGTH_SHORT)
                    .show()
            }
            et_chat.setText("")
        }

        init()
        getCurrentFirebaseUser()
        fetchAndSaveCurrentProfileTextAndData()


    }


    private fun openBottomSheetDetailFragment(username: String, imageUrl: String, bio: String) {
        bottomSheetProfileDetailUser =
            BottomSheetProfileDetailUser(username, imageUrl, bio)
        assert(supportActionBar != null)
        bottomSheetProfileDetailUser!!.show(supportFragmentManager, "edit")
    }

    private fun getCurrentFirebaseUser() {
        logInViewModel = LogInViewModel()
        logInViewModel.firebaseUserLogInStatus
        logInViewModel.firebaseUserLoginStatus.observe(this
        ) { firebaseUser ->
            currentFirebaseUser = firebaseUser
            userId_sender = currentFirebaseUser.uid
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun fetchAndSaveCurrentProfileTextAndData() {
        if (userId_receiver == null) {
            userId_receiver = intent.getStringExtra("userId")
        }
        databaseViewModel.fetchSelectedUserProfileData(userId_receiver!!)
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
                Glide.with(applicationContext).load(profileImageURL).into(iv_user_image)
            }
            if (recycler_view_messages_record != null){
                fetchChatFromDatabase(userId_sender!!, userId_receiver!!)
              binding.progressBar1.visibility = View.GONE

            }

            //fetchChatFromDatabase1()

        }
        addIsSeen()
    }

    fun addIsSeen() {
        val isSeen = "seen"
        databaseViewModel.fetchChatUser()
        databaseViewModel.fetchedChat!!.observe(this
        ) { dataSnapshot ->
            for (dataSnapshot in dataSnapshot!!.children) {
                d("TAG","data : $dataSnapshot")
                val chats = dataSnapshot.getValue(Chats::class.java)
                if (chats?.getCurrentuserId().equals(userId_receiver) && chats?.getReceiverId().equals(
                        userId_sender)) {
                    databaseViewModel.addIsSeenInDatabase(isSeen, dataSnapshot)
                    d("TAG","data1: $dataSnapshot")
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
    private fun fetchChatFromDatabase(senderId:String,receiverId:String) {

        databaseViewModel.fetchChatUser()
        databaseViewModel.fetchedChat?.observe(this
        ) { dataSnapshot ->
            chatsArrayList.clear()
             for (snapshot in dataSnapshot.children) {
                 chats = snapshot.getValue(Chats::class.java)!!

                if (chats.getReceiverId().equals(receiverId) && chats.getCurrentuserId().equals(senderId)|| chats.getReceiverId().equals(senderId )&&
                    chats.getCurrentuserId().equals(receiverId)) {
                  chatsArrayList.add(chats)

                    d("TAG","cht : hiiiiiiiii")

                }

            }
            progressBar.visibility
            messageAdapter = MessageAdapter(chatsArrayList, this, userId_sender!!)
            recycler_view_messages_record.adapter = messageAdapter
             recycler_view_messages_record.postDelayed({
                 recycler_view_messages_record.scrollToPosition((recycler_view_messages_record.adapter as MessageAdapter).itemCount - 1)

             }, 1000)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
   /* private fun fetchChatFromDatabase1() {
        // chats = Chats()
       val senderId :String = userId_receiver!!
        val receiverId:String = userId_sender!!
         // ="${senderId}_$receiverId"

        databaseViewModel.fetchChatUser()
        databaseViewModel.fetchedChat?.observe(this
        ) { dataSnapshot ->

            chatsArrayList.clear()
            for (snapshot in dataSnapshot!!.children) {

                chats= snapshot.getValue(Chats::class.java)!!
                d("TAG","chat :$chatsArrayList")
                if (chats.getReceiverId() == senderId && chats.getSenderId() == receiverId || chats.getReceiverId() == receiverId && chats.getSenderId() == senderId) {
                    chatsArrayList.add(chats)
                }
            }
            d("TAG","chats :$chatsArrayList")

            messageAdapter = MessageAdapter(chatsArrayList, this,"${newCurrentuser}_$newReceiver")
            recycler_view_messages_record.adapter = messageAdapter

            binding.recyclerViewMessagesRecord.adapter?.notifyDataSetChanged()
            recycler_view_messages_record.postDelayed({
                recycler_view_messages_record.scrollToPosition((recycler_view_messages_record.adapter as MessageAdapter).itemCount - 1)

            }, 1000)
        }
    }
*/
    private fun addChatInDataBase() {
        val msg = chat

        val tsLong = System.currentTimeMillis()
        timeStamp = tsLong

        if (newCurrentuser=="" && newReceiver==""){
            newCurrentuser = userId_sender
            newReceiver= userId_receiver

        }
        d("key","addchat current:$newCurrentuser")
        d("key","addchat receiver:$newReceiver")

        databaseViewModel.addChatDb(userId_sender, userId_receiver, msg, timeStamp)

        d("key","addchat after current:$newCurrentuser")
        d("key","addchat afeter receiver: $newReceiver")
        // databaseViewModel.addChatDb1(comunication,msg,timeStamp)

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
            if (notify ) {
                sendNotification(userId_receiver,users!!.getUsername(), msg)
            }
            notify = false
        }
    }

    private fun sendNotification(userId_receiver: String?, username: String, msg: String) {
        databaseViewModel.getTokenDatabaseRef()
        databaseViewModel.getTokenRefDb?.observe(this
        ) { databaseReference ->
             val query = databaseReference.orderByKey().equalTo(userId_receiver)
            query.keepSynced(true)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {

                        val token = snapshot.getValue(Token::class.java)
                        assert(token != null)
                        val data: Data = Data(userId_sender, R.drawable.ic_baseline_textsms_24, "$username: $msg", "New Message", userId_receiver)

                        val sender = Sender(data, token?.getToken())
                        apiService.sendNotification(sender)
                            .enqueue(object : Callback<MyResponse> {
                                override fun onResponse(
                                    call: Call<MyResponse>,
                                    response: Response<MyResponse>,
                                ) {
                                    if (response.code() == 200) {
                                        assert(response.body() != null)
                                        if (response.body()?.success != 1) {
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
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }


    private fun init() {
        val actionBar= supportActionBar
        actionBar!!.hide()
        actionBar.setDisplayHomeAsUpEnabled(true)
        d("u","userId_sender:$userId_sender")
        d("TAG","userId_receiver:$userId_receiver")
        client = Client

        progressBar = ProgressBar(this)
        chatsArrayList = ArrayList()
        databaseViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                application))[DatabaseViewModel::class.java]
        logInViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                application))[LogInViewModel::class.java]

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIServices::class.java)

        binding.ivBackButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, UsersActivity::class.java)
          //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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
        onBackKeyPressedOnKeyboard()
    }
    fun onBackKeyPressedOnKeyboard() {
        binding.etChat.clearFocus()
    }


}










