package com.example.cardviewdemo.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.R
import com.example.cardviewdemo.Adapter.ChatListAdapter
import com.example.cardviewdemo.Adapter.UserFragmentAdapter
import com.example.cardviewdemo.Chat.userId_sender
import com.example.cardviewdemo.Services.model.ChatList
import com.example.cardviewdemo.Services.model.Users
import com.example.cardviewdemo.ViewModel.DatabaseViewModel
import com.example.cardviewdemo.ViewModel.LogInViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


class ChatFragment : Fragment(){
    lateinit var userAdapter: UserFragmentAdapter
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





    @SuppressLint("NotifyDataSetChanged")
    private fun chatLists() {

        databaseViewModel.getChaListUserDataSnapshot(userId_sender)
        databaseViewModel.getChaListUserDataSnapshot?.observe(viewLifecycleOwner
        ) { dataSnapshot ->
            userList= dataSnapshot.children.map { it.getValue(ChatList::class.java)} as ArrayList<ChatList?>

            if (userList.size < 1) {
                recyclerView_chat_fragment.visibility = View.VISIBLE
            } else {
                relative_layout_chat_fragment?.visibility = View.GONE
            }

            chatAdapter = ChatListAdapter(userList, requireContext(), true)
            recyclerView_chat_fragment.adapter = chatAdapter
            chatAdapter.notifyDataSetChanged()

        }

    }


    private fun updateToken(token: String) {
        logInViewModel.updateToken(token)
    }


    private fun init(view: View) {


      //  databaseViewModel = DatabaseViewModel()
      //  logInViewModel = LogInViewModel()
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