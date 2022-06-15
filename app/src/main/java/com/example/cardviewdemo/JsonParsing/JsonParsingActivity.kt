package com.example.cardviewdemo.JsonParsing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.cardviewdemo.R
import kotlinx.android.synthetic.main.activity_json_parsing.*
import org.json.JSONArray

class JsonParsingActivity : AppCompatActivity() {
    val url = "https://jsonplaceholder.typicode.com/posts"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json_parsing)

        btn.setOnClickListener(View.OnClickListener {
            downloadTask()
        })
    }

    fun downloadTask() {
        val queue = Volley.newRequestQueue(this)
        val reques = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                val data = response.toString()
                var jArray = JSONArray(data)

                for(i in 0..jArray.length()-1){
                    var jobject =jArray.getJSONObject(i)
                    var userId =jobject.getInt("userId")
                    var id =jobject.getInt("id")
                    var title = jobject.getString("title")
                    var body = jobject.getString("body")
                    Log.e("Error",userId.toString())
                    Log.e("Error",id.toString())
                    Log.e("Error",title.toString())
                    Log.e("Error",body.toString())
                }

              //  Log.e("Error", response.toString())


            }, Response.ErrorListener {})
        queue.add(reques)
    }
}
