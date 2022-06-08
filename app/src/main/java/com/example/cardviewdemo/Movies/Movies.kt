package com.example.cardviewdemo.Movies

 class Movies {

    lateinit var moviesItem:ArrayList<MoviesItem>
    constructor()
    constructor(movies: ArrayList<MoviesItem>){
        this.moviesItem =movies
    }
    @JvmName("getMovies1")
    fun  getMovies():ArrayList<MoviesItem>{
        return moviesItem
    }
}
 class MoviesItem{
     lateinit var site: String
     lateinit var title: String
     lateinit var url: String
     constructor()
     constructor(site:String,title: String,url:String){
         this.site=site
         this.title=title
         this.url=url
     }

     @JvmName("getSite1")
     fun getSite():String{
         return  site
     }
     @JvmName("getTitle1")
     fun getTitle():String{
         return title
     }
     @JvmName("getUrl1")
     fun getUrl():String{
         return url
     }
 }