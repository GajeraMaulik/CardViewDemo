package com.example.cardviewdemo.retrofit

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.R
import com.example.cardviewdemo.adapter.RecyclerViewAdapter
import com.example.cardviewdemo.services.model.User
import com.example.cardviewdemo.services.model.UserList
import com.example.cardviewdemo.viewModel.RetrofitActivityViewmodel
import kotlinx.android.synthetic.main.activity_retrofit.*

class RetrofitActivity : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel:RetrofitActivityViewmodel
     var lastVisibleItemPosition: Int?=0
    lateinit var scrollListener: RecyclerView.OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)

        val actionBar= supportActionBar
        actionBar!!.title="Retrofit"
        actionBar.setDisplayHomeAsUpEnabled(true)

        initRecyclerView()
        initViewModel()
        searchUser()

        createUserButton.setOnClickListener {
            startActivity(Intent(this@RetrofitActivity,CreateNewUserActivity::class.java))
        }
    }

    private fun searchUser() {
        search_button.setOnClickListener {
            if(!TextUtils.isEmpty(searchEditText.text.toString())) {
                viewModel.searchUser(searchEditText.text.toString())
            } else {
                viewModel.getUsersList()
            }
        }



    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(RetrofitActivityViewmodel::class.java)
        viewModel.getUserListObserverable().observe(this, Observer<UserList> {
            if(it == null) {
                Toast.makeText(this@RetrofitActivity, "no result found...", Toast.LENGTH_LONG).show()
            } else {
                recyclerViewAdapter.userList = it.data.toMutableList()
                recyclerViewAdapter.notifyDataSetChanged()
            }
        })
        viewModel.getUsersList()
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RetrofitActivity)
            val decoration =
                DividerItemDecoration(this@RetrofitActivity, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            recyclerViewAdapter = RecyclerViewAdapter(this@RetrofitActivity)
            adapter = recyclerViewAdapter
            setRecyclerViewScrollListener()


        }
    }
    private fun setRecyclerViewScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val manager =LinearLayoutManager(this@RetrofitActivity)
                val  lastVisibleItemPosition: Int = manager.findLastVisibleItemPosition()
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                if(totalItemCount == lastVisibleItemPosition + 1) {
                    Log.d("MyTAG", "Load new list")
                    recyclerView.removeOnScrollListener(scrollListener)
                }

            }
        }
    }



    override fun onItemEditCLick(user: User) {
        val intent = Intent(this@RetrofitActivity, CreateNewUserActivity::class.java)
        intent.putExtra("user_id", user.id)
        startActivityForResult(intent, 1000)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 1000) {
            viewModel.getUsersList()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}