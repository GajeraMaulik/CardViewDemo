package com.example.cardviewdemo.crudstatic

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.R


class ItemListAdapter(
    private val items: ArrayList<String>,
    private  val context:Context
    ) : RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ItemViewHolder(view)

    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem

        holder.removebutton.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(context)
            val item: String = items[position]
            //set title for alert dialog
            builder.setTitle(R.string.dialogTitle)
            //set message for alert dialog
            builder.setMessage(R.string.dialogMessage)
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                items.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, items.size)
                Toast.makeText(context, "Removed $item", Toast.LENGTH_LONG).show()
            }

            //performing negative action
            builder.setNegativeButton("No")
            { dialogInterface, which ->
                builder.setOnDismissListener(dialogInterface.dismiss())

              //  Toast.makeText(context, "clicked No", Toast.LENGTH_LONG).show()
            }
            builder.show()
        })

        holder.ebutton.setOnClickListener(View.OnClickListener {
            AlertDialog(position, "Update Item", "Update")

        })

    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun AlertDialog(position: Int, title: String, btnText: String) {
        val dialog: Dialog = Dialog(context)
        dialog.setContentView(R.layout.add_item)

        val edtitem: EditText = dialog.findViewById(R.id.edtitem)
        val btnaction = dialog.findViewById<Button>(R.id.btnaction)
        val txt_title: TextView = dialog.findViewById(R.id.txt_title)
        txt_title.text = title
        btnaction.text = btnText

        edtitem.setText(items[position]).toString()
        btnaction.setOnClickListener(View.OnClickListener
        {
            var name: String = edtitem.text.toString()
            if (name.length >= 15) {
                edtitem.error = "Maximum Length Upto 15 "
            } else if (name != "") {
                name = edtitem.text.toString().trim()
                items[position] = name
                notifyItemChanged(position)
                dialog.dismiss()

            } else {
                edtitem.error = "Can Not be Blank"
               // Toast.makeText(context, "Please Enter Item", Toast.LENGTH_SHORT).show()
            }
            


        })
        dialog.show()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById<TextView>(R.id.title)
        val removebutton: ImageView = itemView.findViewById<ImageView>(R.id.remove_item)
        val ebutton: ImageView = itemView.findViewById<ImageView>(R.id.edit_btn)
        //val image: ImageView = itemView.findViewById<ImageView>(R.id.image1)

    }
}


private fun AlertDialog.Builder.setOnDismissListener(dismiss: Unit) {

}


