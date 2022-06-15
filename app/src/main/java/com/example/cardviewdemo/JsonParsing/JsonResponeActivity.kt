package com.example.cardviewdemo.JsonParsing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.cardviewdemo.R
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.databinding.ActivityJsonResponeBinding

class JsonResponeActivity : AppCompatActivity() {
    lateinit var binding : ActivityJsonResponeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJsonResponeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val actionBar= supportActionBar
        actionBar!!.title="JsonParsingRespone"
        actionBar.setDisplayHomeAsUpEnabled(true)


        val arrayList = SharePref.getArrayList(this,"Array")
        Log.d("010101","ArrayList----> $arrayList")

       /* val list = findViewById<TextView>(R.id.arrayList)
        list.append(arrayList.toString())*/

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}