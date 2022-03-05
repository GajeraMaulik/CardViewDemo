package com.example.cardviewdemo.services.model

 class UserProfile {

       var Username: String? = null
       var Email: String? = null
       var Uid: String? = null

    constructor()

    constructor(Username: String?, Email: String?, Uid: String?) {
        this.Username = Username
        this.Email = Email
        this.Uid = Uid
    }
/*

     fun getusername(): String? {
         return Username
     }
     fun  setusername(user:String){
         this.Username = user
     }

*/


}
