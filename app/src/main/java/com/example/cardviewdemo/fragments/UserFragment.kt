package com.example.cardviewdemo.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.R
import com.example.cardviewdemo.adapter.UserFragmentAdapter
import com.example.cardviewdemo.services.model.Users
import com.example.cardviewdemo.viewModel.DatabaseViewModel

class UserFragment: Fragment(){
    private lateinit var databaseViewModel: DatabaseViewModel
    var chatFragment = ChatFragment()
    private lateinit var mUSer: ArrayList<Users>
    private var currentUserId: String? = null
    private var recyclerView: RecyclerView? = null
    private var userFragmentAdapter: UserFragmentAdapter? = null
    var et_search: EditText? = null




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_user, container, false)
        init(view)
        fetchingAllUserNAme()
        return view
    }


    override fun onResume() {
        super.onResume()
    }

    private fun fetchingAllUserNAme() {
        databaseViewModel.fetchingUserDataCurrent()
        databaseViewModel.fetchUserCurrentData?.observe(viewLifecycleOwner){ dataSnapshot ->
            val user = dataSnapshot.getValue(Users::class.java)
            if (user != null) {
             //   d("TAG", user.getUsername())
                currentUserId = user.getId()

            }
        }
        databaseViewModel.fetchUserByNameAll()
        databaseViewModel.fetchUserNames?.observe(viewLifecycleOwner) { dataSnapshot ->
            if (et_search?.text.toString() == "") {
                mUSer.clear()
                for (snapshot in dataSnapshot!!.children) {
                    val user: Users = snapshot.getValue(Users::class.java)!!
                    if (!currentUserId.equals(user.getId())) {
                        mUSer.add(user)

                    }
                    userFragmentAdapter = UserFragmentAdapter(mUSer,requireContext(),false)

                    recyclerView?.adapter = userFragmentAdapter
                }
            }
        }
    }

    private fun init(view: View) {
        databaseViewModel = DatabaseViewModel()
     /*   databaseViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(Objects.requireNonNull(Activity()).application))
            .get<DatabaseViewModel>(DatabaseViewModel::class.java)*/
        recyclerView = view.findViewById(R.id.user_list_recycle_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView?.context, DividerItemDecoration.VERTICAL)
        recyclerView?.addItemDecoration(dividerItemDecoration)
        mUSer = ArrayList<Users>()
        et_search = view.findViewById<EditText>(R.id.et_search)

        et_search?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                searchUsers(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                if (et_search?.text.toString().startsWith(" ")) et_search?.setText("")
            }
        })
    }

    private fun searchUsers(searchText: String) {
        if (!(searchText.isEmpty() && searchText == "")) {
            databaseViewModel.fetchSearchedUser(searchText)
            databaseViewModel.fetchSearchUser!!.observe(viewLifecycleOwner) { dataSnapshot ->
                mUSer.clear()
                for (snapshot in dataSnapshot.children) {
                    val users: Users = snapshot.getValue<Users>(Users::class.java)!!
                    if (!users.getId().equals( currentUserId)) {
                        mUSer.add(users)
                    }
                }
                userFragmentAdapter = UserFragmentAdapter(mUSer,requireContext(),false)
                recyclerView!!.adapter = userFragmentAdapter
            }
        } else {
            fetchingAllUserNAme()
        }
    }
}


