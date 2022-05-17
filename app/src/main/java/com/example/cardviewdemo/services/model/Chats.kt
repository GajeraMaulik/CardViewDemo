package com.example.cardviewdemo.services.model

class Chats {
    private  var receiverId: String? =null
    private  var currentuserId: String?= null
    private var message: String?= null
       private var timestamp: Long? = null
    var seen:Boolean = false

    constructor()

    constructor(receiverId: String, currentuserId: String, message: String, timestamp: Long, seen: Boolean) {
        this.receiverId = receiverId
        this.currentuserId = currentuserId
        this.message = message
        this.timestamp = timestamp
        this.seen = seen
    }

    @JvmName("getSeen1")
    fun getSeen(): Boolean{
        return seen
    }

    @JvmName("setSeen1")
    fun setSeen(seen: Boolean) {
        this.seen = seen
    }


    fun getReceiverId(): String? {
        return receiverId
    }

    fun setReceiverId(receiverId: String) {
        this.receiverId = receiverId
    }

    fun getCurrentuserId(): String?{
        return currentuserId
    }

    fun setCurrentuserId(currentuserId: String) {
        this.currentuserId = currentuserId
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getTimestamp(): Long ?{
        return timestamp
    }

    fun setTimestamp(timestamp: Long) {
        this.timestamp = timestamp
    }
}