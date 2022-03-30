package com.example.cardviewdemo.fragments

import android.app.Activity
import android.content.Context
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
import com.example.cardviewdemo.adapter.UserFragmentAdapter
import com.example.cardviewdemo.services.model.ChatList
import com.example.cardviewdemo.services.model.Users
import com.example.cardviewdemo.viewModel.DatabaseViewModel
import com.example.cardviewdemo.viewModel.LogInViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


class ChatFragment : Fragment(){
    lateinit var userAdapter: UserFragmentAdapter
    private lateinit var mUsers: ArrayList<Users>
    private var currentUserId: String? = null
    private lateinit var userList //list of all other users with chat record
            : ArrayList<ChatList>
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var logInViewModel: LogInViewModel
    private lateinit var recyclerView_chat_fragment: RecyclerView
    var relative_layout_chat_fragment: RelativeLayout? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_chat, container, false)
        init(view)
        fetchAllChat()
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

    private fun fetchAllChat() {
        databaseViewModel.fetchingUserDataCurrent()
        databaseViewModel.fetchUserCurrentData?.observe(viewLifecycleOwner
        ) { dataSnapshot ->
            val users= dataSnapshot.getValue(Users::class.java)
            if (users != null) {
                currentUserId = users.getId()
            }
        }
        databaseViewModel.getChaListUserDataSnapshot(currentUserId)
        databaseViewModel.getChaListUserDataSnapshot?.observe(viewLifecycleOwner
        ) { dataSnapshot ->
            for (dataSnapshot1 in dataSnapshot.children) {
                val chatList = dataSnapshot1.getValue(ChatList::class.java)
                userList.add(chatList!!)
            }
            chatLists()
        }
    }

    private fun chatLists() {
        databaseViewModel.fetchUserByNameAll()
        databaseViewModel.fetchUserNames?.observe(viewLifecycleOwner) { dataSnapshot ->
            mUsers.clear()
            for (dataSnapshot1 in dataSnapshot!!.children) {
                val users = dataSnapshot1.getValue(Users::class.java)
                for (chatList in userList) {
                    assert(users != null)
                    if (users!!.getId() == chatList.getId()) {
                        if (!mUsers.contains(users)) mUsers.add(users)
                    }
                }
            }
            if (mUsers.size < 1) {
                relative_layout_chat_fragment!!.visibility = View.VISIBLE
            } else {
                relative_layout_chat_fragment!!.visibility = View.GONE
            }
            userAdapter = UserFragmentAdapter(mUsers,requireActivity(),true)
            recyclerView_chat_fragment.adapter = userAdapter
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
        mUsers = ArrayList()
        userList = ArrayList()

        userAdapter = UserFragmentAdapter()

    }
}