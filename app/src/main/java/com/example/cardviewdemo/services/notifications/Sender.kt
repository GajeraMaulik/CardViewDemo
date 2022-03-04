package com.example.cardviewdemo.services.notifications

class Sender(data: Data, token: String?) {

    var data: Data? = null
    var to: String? = null

    fun Sender(data: Data?, to: String?) {
        this.data = data
        this.to = to
    }
}