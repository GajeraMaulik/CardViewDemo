package com.example.cardviewdemo.JsonParsing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cardviewdemo.R
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.login.SignInActivity
import kotlinx.android.synthetic.main.activity_json_parsing.*
import org.json.JSONArray

class JsonParsingActivity : AppCompatActivity() {
    val url = "https://jsonplaceholder.typicode.com/posts"
    lateinit var adapter: JsonParsingAdapter
    lateinit var postList : ArrayList<PostItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json_parsing)

        val actionBar= supportActionBar
        actionBar!!.title="JsonParsing"
        actionBar.setDisplayHomeAsUpEnabled(true)

/*
        btn.setOnClickListener(View.OnClickListener {
            downloadTask()
            val intent = Intent(this@JsonParsingActivity, JsonResponeActivity::class.java)
            startActivity(intent)
            finish()
        })*/
        downloadTask()
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
                    postList.add(postItem)
                }
                if (postList == null){
                    SharePref.saveArrayList(this,postList,"Array")
                }
                setupRecycler()

              //  Log.e("Error", response.toString())


            }, Response.ErrorListener {})
        queue.add(request)


    }
    private fun setupRecycler() {

        adapter = JsonParsingAdapter(this,postList)
        rvLoaddata.adapter = adapter
        progress.visibility = View.GONE

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
