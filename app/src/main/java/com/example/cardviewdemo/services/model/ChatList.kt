package com.example.cardviewdemo.services.model

class ChatList {
    private var id: String? = null
    private var timestamp: Long? = null

    constructor()

    constructor(id: String?, timestamp: Long?) {
        this.id = id
        this.timestamp = timestamp
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getTimestamp(): Long? {
        return timestamp
    }

    fun setTimestamp(timestamp: Long?) {
        this.timestamp = timestamp
    }

}