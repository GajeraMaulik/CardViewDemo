package com.example.cardviewdemo.listview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.R


class ListActivity() : AppCompatActivity(), ItemClickListener {


    private lateinit var adapter: ImageAdapter
    private lateinit var items: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
       val actionBar = supportActionBar
        actionBar!!.title = "Demo"
        actionBar.setDisplayHomeAsUpEnabled(true)



        items = fetchData()
        adapter = ImageAdapter(items, this,this)
        recyclerView.adapter = adapter

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onItemClick(click: Int, image: Int, text: String, view: View) {
        val intent = Intent(this, ImageActivity::class.java)
        //val putExtra = intent.putExtra("item_detail", 0)
        intent.putExtra("image", text)
       intent.putExtra("text", text)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition_image")
       startActivity(intent, options.toBundle())
        // startActivity(intent)
    }


    private fun fetchData(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("https://images.unsplash.com/photo-1599420186946-7b6fb4e297f0?ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1974&q=80")
        list.add("https://images.unsplash.com/photo-1635282922712-97888dc706dc?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1974&q=80")
        list.add("https://images.unsplash.com/photo-1593642532781-03e79bf5bec2?ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1976&q=80")
        list.add("https://images.unsplash.com/photo-1635277568031-f84f54257abc?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1974&q=80")
        list.add("https://images.unsplash.com/photo-1635243553791-d570233af040?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1974&q=80")
        list.add("https://images.unsplash.com/photo-1471897488648-5eae4ac6686b?ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1974&q=80")
        list.add("https://images.unsplash.com/photo-1534353436294-0dbd4bdac845?ixlib=rb-1.2.1&ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80")
        list.add("https://images.unsplash.com/photo-1635243843750-6b46f4a0aaa0?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1974&q=80")

        return list
    }

}


