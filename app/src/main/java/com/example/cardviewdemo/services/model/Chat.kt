package com.example.cardviewdemo.services.model

 class Chat{


    var senderId:String? = null
    var receiverId:String? = null
    var message:String? = null
    var time: Long? = null

     constructor()

     constructor(message: String?, senderId:String?,receiverId:String?, time: Long?){
         this.message = message
         this.senderId = senderId
         this.receiverId = receiverId
         this.time = time
     }


 }
