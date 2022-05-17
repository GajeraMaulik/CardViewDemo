package com.example.cardviewdemo.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.R
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.adapter.ChatListAdapter
import com.example.cardviewdemo.adapter.UserFragmentAdapter
import com.example.cardviewdemo.chat.channelid
import com.example.cardviewdemo.chat.userId_sender
import com.example.cardviewdemo.services.model.ChatList
import com.example.cardviewdemo.services.model.Chats
import com.example.cardviewdemo.services.model.Users
import com.example.cardviewdemo.services.repository.FirebaseInstanceDatabase
import com.example.cardviewdemo.viewModel.DatabaseViewModel
import com.example.cardviewdemo.viewModel.LogInViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import kotlin.collections.ArrayList

var unseen=0

class ChatFragment : Fragment(){
    private val firebaseUser = FirebaseAuth.getInstance().currentUser!!
    var chatList =ChatList()
    lateinit var userAdapter: UserFragmentAdapter
    lateinit var firebaseInstanceDatabase: FirebaseInstanceDatabase
    private var currentUserId: String? = null
    lateinit var chatAdapter:ChatListAdapter
    private lateinit var mUSer: ArrayList<Users>
  //  private lateinit var mUsers: ArrayList<ChatList?>
    private lateinit var userList //list of all other users with chat record
            : ArrayList<ChatList?>
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var logInViewModel: LogInViewModel
    private lateinit var recyclerView_chat_fragment: RecyclerView
    var relative_layout_chat_fragment: RelativeLayout? = null
    private var userFragmentAdapter: ChatListAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_chat, container, false)
        init(view)
        getUnseenmessage()
        chatLists()

       getTokens()

        return view
    }

    fun getTokens() {

     //   MessagingServices.sharedPref = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            // Get new FCM registration token

            val token = task.result
            Log.d("TAG","FCM : $token")

            if (task.isSuccessful) {
                updateToken(token)
            }else {                Log.w("MAulik", "Fetching FCM registration token failed", task.exception)
                                    return@OnCompleteListener
            }



            // MessagingServices.token = token
        })
    }

    fun getUnseenmessage(){
        firebaseInstanceDatabase.instance.child("Chats").child("$channelid").child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    Log.d("seen1", "seen: $datasnapshot")
                    var unread = 0
                    var seen:Any = false
                    for (snapshot in datasnapshot.children) {
                        val chats = snapshot.getValue(Chats::class.java)
                        Log.d("seen1", "seen1 : ${chats!!.getSeen()}")
                        // seensize = chats.seen as ArrayList<Boolean>
                         seen=snapshot.child("seen").getValue(false)!!
//                        val seen1 = snapshot.child("seen").getValue(true)
                            var count = 0
                        if  (seen == false){
                            unread+=1
                        }
                        Log.d("abd", "false---->: $seen")
//                        d("abd","true---------->:$seen1")
                    }

                    unseen=unread


                    d("qwertyuiop","count--::::: $unread")
                   // chatList.setUnreadmessage(unseen)
                }

                override fun onCancelled(error: DatabaseError)  {

                }
            })
    }




    @SuppressLint("NotifyDataSetChanged")
    private fun chatLists() {
        userList.clear()
        databaseViewModel.getChaListUserDataSnapshot(userId_sender)
        databaseViewModel.getChaListUserDataSnapshot?.observe(viewLifecycleOwner
        ) { dataSnapshot->
            d("maulik",",,,,,,$dataSnapshot")
            userList= dataSnapshot.children.map { it.getValue(ChatList::class.java)} as ArrayList<ChatList?>


            chatAdapter = ChatListAdapter(userList, requireContext(), true)
            recyclerView_chat_fragment.adapter = chatAdapter
            chatAdapter.notifyDataSetChanged()

        }

    }

    override fun onStart() {
        super.onStart()
        if (userList.size < 1) {
            recyclerView_chat_fragment.visibility = View.VISIBLE
        } else {
            relative_layout_chat_fragment?.visibility = View.GONE
        }

    }


    private fun updateToken(token: String) {
        logInViewModel.updateToken(token)
    }


    private fun init(view: View) {


      //  databaseViewModel = DatabaseViewModel()
      //  logInViewModel = LogInViewModel()
        firebaseInstanceDatabase = FirebaseInstanceDatabase()
        databaseViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(Objects.requireNonNull(Activity()).application))[DatabaseViewModel::class.java]

        logInViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(Objects.requireNonNull(Activity()).application))[LogInViewModel::class.java]
        relative_layout_chat_fragment = view.findViewById(R.id.relative_layout_chat_fragment)
        recyclerView_chat_fragment = view.findViewById(R.id.recycler_view_chat_fragment)
        val dividerItemDecoration = DividerItemDecoration(recyclerView_chat_fragment.context,
            DividerItemDecoration.VERTICAL)
        recyclerView_chat_fragment.addItemDecoration(dividerItemDecoration)
        userList = ArrayList()

        userAdapter = UserFragmentAdapter()

    }
}