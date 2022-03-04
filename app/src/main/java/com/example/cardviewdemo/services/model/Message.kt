package com.example.cardviewdemo.services.model

 class Message {
    var message:String? = null
     var senderId : String? = null
     var receiverId:String? = null
     var time: Long? = null

     constructor() //empty for firebase

     constructor(message: String?, senderId:String?,receiverId:String?, time: Long?){
         this.message = message
         this.senderId = senderId
         this.receiverId = receiverId
         this.time = time
     }

 }
