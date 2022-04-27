package com.example.cardviewdemo.constants

import android.util.Log
import com.example.cardviewdemo.adapter.MessageAdapter
import com.example.cardviewdemo.adapter.UserFragmentAdapter
import com.example.cardviewdemo.services.model.Chats
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.HashMap

class Constants {

    companion object{
        lateinit var user:String


        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY = "AAAAkDonE_M:APA91bFvZOr69axqTEKhdlkP95_XPTDDJ7MqhfX1iM6QiyAfFyWdEGnzImOMmSU4IVQh8DnXpr36o5KYXGZp5-hRX2lZJkN-WVm3lx_dcyPtVDkkgZ5qjWTif5vwnGrGcbJL0_glZuOo" // get firebase server key from firebase project setting
        const val CONTENT_TYPE = "application/json"
        const val OAUTH_TOKEN = "ya29.A0ARrdaM9AXWcYKIWyvDtmmnw2lgWzGywxuxTzaxnDKg6CKFBbgsFOj60GeIR47U4YbLGecv16I96fGaJTjJkj26win8UvU9B9CrazE8C5x24wlVZ0YWDVbuJXdLnmZ8h5RS8m28VheEUQI2wNB2Yh69atXZr5"





    }

}