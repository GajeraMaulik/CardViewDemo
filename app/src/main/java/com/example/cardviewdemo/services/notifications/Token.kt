package com.example.cardviewdemo.services.notifications

class Token {
    private var token: String? = null

    fun Token() {}

    fun Token(token: String?) {
        this.token = token
    }

    fun getToken(): String? {
        return token
    }

    fun setToken(token: String?) {
        this.token = token
    }
}