package com.example.cardviewdemo.services.model

class Chats {
    private  var receiverId: String?=null
    private var senderId: String? = null
    private var message: String? = null
    private var timestamp: String? = null
    private var seen = false
    constructor() {}

    constructor(receiverId: String?, senderId: String?, message: String?, timestamp: String?, seen: Boolean) {
        this.receiverId = receiverId
        this.senderId = senderId
        this.message = message
        this.timestamp = timestamp
        this.seen = seen
    }

    fun getSeen(): Boolean {
        return seen
    }

    fun setSeen(seen: Boolean) {
        this.seen = seen
    }


    fun getReceiverId(): String? {
        return receiverId
    }

    fun setReceiverId(receiverId: String?) {
        this.receiverId = receiverId
    }

    fun getSenderId(): String? {
        return senderId
    }

    fun setSenderId(senderId: String?) {
        this.senderId = senderId
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getTimestamp(): String? {
        return timestamp
    }

    fun setTimestamp(timestamp: String?) {
        this.timestamp = timestamp
    }
}