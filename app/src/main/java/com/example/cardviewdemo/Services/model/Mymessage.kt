package com.example.cardviewdemo.Services.model

import kotlin.properties.Delegates

class Mymessage {
      lateinit var message:String
      lateinit var senderId : String
      lateinit var receiverId:String
      var  timestamp by Delegates.notNull<Long>()
    var seen:Boolean = false

      constructor() //empty for firebase

     constructor(message: String, senderId:String,receiverId:String, timestamp: Long,seen:Boolean){
         this.message = message
         this.senderId = senderId
         this.receiverId = receiverId
         this.timestamp = timestamp
         this.seen = seen
     }

     @JvmName("getSeen1")
     fun getSeen(): Boolean {
         return seen
     }

     @JvmName("setSeen1")
     fun setSeen(seen: Boolean) {
         this.seen = seen
     }


     @JvmName("getReceiverId1")
     fun getReceiverId(): String {
         return receiverId
     }

     @JvmName("setReceiverId1")
     fun setReceiverId(receiverId: String) {
         this.receiverId = receiverId
     }

     @JvmName("getSenderId1")
     fun getSenderId(): String {
         return senderId
     }

     @JvmName("setSenderId1")
     fun setSenderId(senderId: String) {
         this.senderId = senderId
     }

     @JvmName("getMessage1")
     fun getMessage(): String {
         return message
     }

     @JvmName("setMessage1")
     fun setMessage(message: String) {
         this.message = message
     }

     @JvmName("getTimestamp1")
     fun getTimestamp(): Long {
         return timestamp
     }

     @JvmName("setTimestamp1")
     fun setTimestamp(timestamp: Long) {
         this.timestamp = timestamp
     }

 }
