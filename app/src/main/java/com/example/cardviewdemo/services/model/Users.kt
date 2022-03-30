package com.example.cardviewdemo.services.model


 class Users{


    private lateinit var id: String
      private lateinit var emailId: String
      private lateinit var timestamp: String
     private lateinit var imageUrl: String
    private lateinit var username: String

     private lateinit var bio: String

     private lateinit var status: String
    constructor() {

    }

    constructor(id: String, username: String, emailId: String, timestamp: String, status: String, imageUrl: String, bio: String, ) {
        this.id = id
        this.username = username
        this.emailId = emailId
        this.timestamp = timestamp
        this.imageUrl = imageUrl
        this.bio = bio
        this.status = status
    }

    fun getId(): String {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getUsername(): String {
        return username
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun getEmailId(): String {
        return emailId
    }

    fun setEmailId(emailId: String) {
        this.emailId = emailId
    }

    fun getTimestamp(): String{
        return timestamp
    }

    fun setTimestamp(timestamp: String) {
        this.timestamp = timestamp
    }



    fun getStatus(): String {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }
     fun getImageUrl(): String {
         return imageUrl
     }

     fun setImageUrl(imageUrl: String) {
         this.imageUrl = imageUrl
     }

     fun getBio(): String {
         return bio
     }

     fun setBio(bio: String) {
         this.bio = bio
     }


 }
