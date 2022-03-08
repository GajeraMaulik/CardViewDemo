package com.example.cardviewdemo.services.notifications

class Token {
    var token: String? = null

    constructor() {}
    constructor(token: String?) {
        this.token = token
    }
    fun getToken(token: String?){
        this.token = token
    }
}