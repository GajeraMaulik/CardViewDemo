package com.example.cardviewdemo.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cardviewdemo.services.APIServices
import com.example.cardviewdemo.services.model.UserList
import com.example.cardviewdemo.services.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitActivityViewmodel:ViewModel() {

     var recyclerListData: MutableLiveData<UserList>

    init {
        recyclerListData = MutableLiveData()

    }


    fun getUserListObserverable() : MutableLiveData<UserList> {
        return recyclerListData
    }

    fun getUsersList() {

        val retroInstance = Client.getRetroInstance("https://gorest.co.in/public/v2/").create(APIServices::class.java)

        val call = retroInstance.getUsersList()
        call.enqueue(object : Callback<UserList> {
            override fun onFailure(call: Call<UserList>, t: Throwable) {
                recyclerListData.postValue(null)
            }

            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                if(response.isSuccessful) {
                    recyclerListData.postValue(response.body())
                } else {
                    recyclerListData.postValue(null)
                }
            }
        })
    }

    fun searchUser(searchText: String) {

        val retroInstance = Client.getRetroInstance("https://gorest.co.in/public/v2/").create(APIServices::class.java)
        val call = retroInstance.searchUsers(searchText)
        call.enqueue(object : Callback<UserList> {
            override fun onFailure(call: Call<UserList>, t: Throwable) {
                recyclerListData.postValue(null)
            }

            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                if(response.isSuccessful) {
                    recyclerListData.postValue(response.body())
                } else {
                    recyclerListData.postValue(null)
                }
            }
        })
    }

}