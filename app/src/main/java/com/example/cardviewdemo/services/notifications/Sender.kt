package com.example.cardviewdemo.services.notifications

import retrofit2.Response
import com.example.cardviewdemo.services.notifications.Data
class Sender{

    var data: Data? = null
    var to: String? = null

constructor(){
}
    constructor(data: Data?, to: String?) {
        this.data = data
        this.to = to
    }
}