package com.example.cardviewdemo.data

import android.widget.EditText

class UserProfile {
     var  username : String? =null
     var email : String?=null
    var password: String?=null

    constructor()

    constructor(username: String?, email: String?, password: String?) {
        this.username = username
        this.email = email
        this.password = password
    }

     fun getusername(): String?{
        return username
     }

     fun setusername(username: String?){
         this.username = username
     }


     fun getemail(): String?{
         return email
     }

     fun setemail(email: String?){
         this.email = email
     }

     fun getpassword(): String?{
         return password
     }

     fun setpassword(password: String?){
         this.password = password
     }

}