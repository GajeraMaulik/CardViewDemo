package com.example.cardviewdemo.data

 class Message {
    constructor() //empty for firebase

    constructor(messageText: String){
        message = messageText
    }
    var message: String? = null
    var timestamp: Long = System.currentTimeMillis()
}
