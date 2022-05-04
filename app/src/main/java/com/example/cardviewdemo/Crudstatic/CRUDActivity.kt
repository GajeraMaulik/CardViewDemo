package com.example.cardviewdemo.Crudstatic


import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CRUDActivity() : AppCompatActivity() {
    private lateinit var adapter: ItemListAdapter
    private lateinit var items: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_r_u_d)
        val actionBar= supportActionBar
        actionBar!!.title="Demo"
        actionBar.setDisplayHomeAsUpEnabled(true)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val mButtonAdd = findViewById<FloatingActionButton>(R.id.additems)
        val mButtonEdit = findViewById<ImageView>(R.id.edit_btn)
        items = fetchData()
        adapter = ItemListAdapter(items, this)
        recyclerView.adapter = adapter

        mButtonAdd.setOnClickListener(View.OnClickListener {
            val dialog: Dialog = Dialog(this)
            dialog.setContentView(R.layout.add_item)

            val edtitem: EditText = dialog.findViewById(R.id.edtitem)
            val btnaction = dialog.findViewById<Button>(R.id.btnaction)

            btnaction.setOnClickListener(View.OnClickListener {
                var name: String = edtitem.text.toString()

                if (name.length >= 15) {
                    edtitem.error = "Maximum Length Upto 15"

                } else if (name != "") {
                    name = edtitem.text.toString().trim()
                    items.add(0, name)
                    adapter.notifyItemInserted(0)
                    adapter.notifyDataSetChanged()
                    dialog.dismiss()


                } else {
                    edtitem.error = "Can Not be Blank"
                   //Toast.makeText(this, "Please Enter Item", Toast.LENGTH_SHORT).show()
                }
            })
            dialog.show()
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun fetchData(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 1..20) {
            list.add("Item $i")

        }
        return list
    }

}
















