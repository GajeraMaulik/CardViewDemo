package com.example.cardviewdemo.services.model

import android.app.Application

class App: Application() {
    companion object{
        lateinit var user:String
    }
}