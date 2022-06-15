package com.example.cardviewdemo.JsonParsing

 class PostItem{
    lateinit var body: String
    var id: Int=0
    lateinit var title: String
    var userId: Int=0

    constructor()
    constructor(body: String, id: Int, title: String, userId: Int){
        this.body = body
        this.id = id
        this.title = title
        this.userId = userId
    }

     @JvmName("getId1")
     fun getId():Int{
         return id
     }

     @JvmName("setId1")
     fun setId(id: Int){
         this.id=id
     }


     fun getUserid():Int{
         return  userId
     }

     fun setUserid(userId: Int){
         this.userId=userId
     }
     @JvmName("getTitle1")
     fun getTitle():String{
         return title
     }

     @JvmName("setTitle1")
     fun setTitle(title: String){
         this.title=title
     }

     @JvmName("getBody1")
     fun getBody():String{
         return body
     }

     @JvmName("setBody1")
     fun setBody(body: String){
         this.body=body
     }

}