package com.example.cardviewdemo.services.notifications

import retrofit2.Response

class Sender(data: com.example.cardviewdemo.services.model.Data, token: Unit?) {

    var data: Data? = null
    var to: String? = null

    fun Sender(data: Data?, to: String?) {
        this.data = data
        this.to = to
    }
}