package com.example.cardviewdemo.services.model

class Data{
     var title: String? = null
      var icon: Int? = null
     var message: String? = null
     var s: String?= null
     var userId_receiver: String? = null

     constructor(){

     }
     constructor(title:String?, icon: Int?, message:String?, s:String?, userId_receiver:String?){
         this.title =title
         this.icon = icon
         this.message = message
         this.s=s
         this.userId_receiver = userId_receiver
     }
}

