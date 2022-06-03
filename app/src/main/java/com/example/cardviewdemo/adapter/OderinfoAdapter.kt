package com.example.cardviewdemo.adapter

import android.content.res.XmlResourceParser
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.databinding.ItemOrderinfoBinding
import com.example.cardviewdemo.services.model.Orderinfo
import com.example.cardviewdemo.xmlparse.XMlparseActivity

class OderinfoAdapter(var oderinfoList: MutableList<Orderinfo>, var orderinfoList: Int, var arrayOf: Array<String>, var intArrayOf: IntArray): RecyclerView.Adapter<OderinfoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OderinfoAdapter.ViewHolder {
        val  binding=ItemOrderinfoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OderinfoAdapter.ViewHolder, position: Int) {
        with(holder){
            with(oderinfoList[position]){
                binding.ordNo.text= this.ord_no.toString()
                binding.cusNo.text=this.cus_no.toString()
                binding.itemNo.text=this.item_no
                binding.qtyOrdered.text=this.qty_ordered.toString()
                binding.lineNo.text=this.line_no.toString()
                binding.cusName.text=this.cus_name
                binding.upcCd.text=this.upc_cd
                binding.Scc14.text=this.SCC14
                binding.CasePack.text=this.CasePack
            }
        }

    }

    override fun getItemCount(): Int{
        return oderinfoList.size
    }
    inner class ViewHolder(val binding: ItemOrderinfoBinding):RecyclerView.ViewHolder( binding.root)

}