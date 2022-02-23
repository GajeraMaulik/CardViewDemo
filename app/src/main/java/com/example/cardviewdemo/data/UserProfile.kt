package com.example.cardviewdemo.data

import android.widget.EditText

class UserProfile {
     var  Username : String? = null
      var Email : String? = null
     var Uid:String? =null

    constructor()

    constructor(username: String?, email: String?, uid: String?) {
        this.Username = username
        this.Email = email
        this.Uid = uid
    }

    /* fun getusername(): String?{
        return Username
     }

     fun setusername(username: String?){
         this.Username = username
     }


     fun getemail(): String?{
         return Email
     }

     fun setemail(email: String?){
         this.Email = email
     }

    fun  getuid(): String? {
        return Uid
    }
    fun setuid(uid: String?) {
        this.Uid = uid
    }

    *//* fun getpassword(): String?{
         return password
     }

     fun setpassword(password: String?){
         this.password = password
     }
*/
}