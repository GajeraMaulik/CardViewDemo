package com.example.cardviewdemo.Services.notifications

class Token {
    var token: String? = null


    constructor() {}
    constructor(token: String?) {
        this.token = token
    }
    @JvmName("getToken1")
    fun getToken(): String? {
        return token
    }
}