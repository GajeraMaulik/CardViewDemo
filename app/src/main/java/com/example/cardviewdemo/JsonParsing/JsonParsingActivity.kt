package com.example.cardviewdemo.JsonParsing

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cardviewdemo.R
import com.example.cardviewdemo.services.Connectivity.ConnectivityLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_json_parsing.*
import org.json.JSONArray
import java.lang.reflect.Type


class JsonParsingActivity : AppCompatActivity() {

    val url = "https://jsonplaceholder.typicode.com/posts"
    lateinit var adapter: JsonParsingAdapter
    lateinit var postList : ArrayList<PostItem>
    private lateinit var connectivityLiveData: ConnectivityLiveData
     var sharedPreferences:SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json_parsing)

        val actionBar= supportActionBar
        actionBar!!.title="JsonParsing"
        actionBar.setDisplayHomeAsUpEnabled(true)

        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.

        postList = ArrayList()
        connectivityLiveData= ConnectivityLiveData(application)




        setupShared()

    }

    fun setupShared(){

/*        if (sharedPreferences == null && checkForInternet()) {
            // if the sharedPreferences is empty
            // creating a new array list.
            //  progress.visibility=View.GONE
            downloadTask()
            // noInternet.visibility =View.GONE

        }else if(sharedPreferences != null && !checkForInternet()){
            loadData()
        }else{
            progress.visibility= View.GONE
            noInternet.visibility =View.VISIBLE
        }*/


       connectivityLiveData.observe(this, Observer {isAvailable->
            when(isAvailable)
            {
                true->{
                    // checking below if the sharedPreferences is empty or not
                    if (sharedPreferences == null){
                        downloadTask()
                        d("save", "---true if ---${downloadTask()}")
                    }else{
                        //downloadTask()
                        loadData()
                        d("save", "---true else---${loadData()}")

                    }

                }
                false ->{
                        if(sharedPreferences != null){
                            //downloadTask()
                            loadData()

                            d("save", "---false if--- ${loadData()}")
                        }else{
                            progress.visibility= View.GONE
                            noInternet.visibility =View.VISIBLE
                            d("save", "---false else--- no internet")

                        }
                }
            }
        })


    }


    fun downloadTask() {
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                val data = response.toString()


                 postList = ArrayList()
                var jArray = JSONArray(data)

                for(i in 0..jArray.length()-1){
                    val postItem =PostItem()
                    var jobject =jArray.getJSONObject(i)
                    postItem.setUserid(jobject.getInt("userId"))
                    postItem.setId(jobject.getInt("id"))
                    postItem.setTitle(jobject.getString("title"))
                    postItem.setBody(jobject.getString("body"))

                    /*    Log.e("Error",userId.toString())
                    Log.e("Error",id.toString())
                    Log.e("Error",title.toString())
                    Log.e("Error",body.toString())*/
                    postList!!.add(postItem)
                }
                saveData(postList)


              //  saveData()
              //  Log.e("Error", response.toString())


            }, Response.ErrorListener {})
        queue.add(request)


    }

    private fun setupRecycler(postList1: ArrayList<PostItem>?) {

        adapter = JsonParsingAdapter(this,postList1!!)
        rvLoaddata.adapter = adapter
        progress.visibility = View.GONE


    }
    private fun loadData() {

        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)


        // creating a variable for gson.
        val gson = Gson()

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        val json = sharedPreferences!!.getString("post", null)

        // below line is to get the type of our array list.
        val type: Type = object : TypeToken<ArrayList<PostItem>>() {}.type

        // in below line we are getting data from gson
        // and saving it to our array list
        postList = gson.fromJson(json, type)

        saveData(postList)

        d("post","-------->$postList")


    }

    private fun saveData(postList1: ArrayList<PostItem>?) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.

        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)


        // creating a variable for editor to
        // store data in shared preferences.
        val editor = sharedPreferences!!.edit()

        // creating a new variable for gson.
        val gson = Gson()

        // getting data from gson and storing it in a string.
        val json = gson.toJson(postList1)

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("post", json)

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply()

        setupRecycler(postList1!!)
        // after saving data we are displaying a toast message.
        Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show()
    }
     fun checkForInternet(): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true



                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
