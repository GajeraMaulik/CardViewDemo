package com.example.cardviewdemo.data

 class Message {
    var message:String? = null
     var senderId : String? = null
     var time: Long? = null

     constructor() //empty for firebase

     constructor(message: String?,senderId:String?,time:Long?){
         this.message = message
         this.senderId = senderId
         this.time = time
     }

 }
