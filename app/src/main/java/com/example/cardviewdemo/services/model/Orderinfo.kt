package com.example.cardviewdemo.services.model

data class Orderinfo(val ord_no:Int=0,val cus_no:Int=0,val item_no:String?="" ,val qty_ordered:Double?=0.0,val line_no:Int=0,val cus_name:String?="",val upc_cd:String?="",
                val SCC14:String?="",val CasePack:String?="") {
}