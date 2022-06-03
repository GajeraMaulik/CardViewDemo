package com.example.cardviewdemo.services.model

import android.media.Rating


class Products{
    var  productsItem :ArrayList<ProductsItem>
    val sucess: Int =0

   fun getProduct():ArrayList<ProductsItem>{
       return  productsItem
   }
    fun  setProduct(productsItem: ArrayList<ProductsItem>){
        this.productsItem=productsItem
    }
    init {
        productsItem =ArrayList()
    }
}
 class ProductsItem{
     val items: List<Products>? = null

     lateinit var category: String
     lateinit var description: String
     //val id: Int
     lateinit var image: String
     var price: Double=0.0
   lateinit var  rating: Rating
     lateinit var title: String
    val sucess: Int =0

     constructor()
     constructor( category: String, description: String,rating: Rating, image: String, price: Double, title: String,){
        this.category=category
        this.description=description
         this.rating= rating
        this.image=image
        this.price=price
        this.title=title
     }
    @JvmName("getTitle1")
    fun getTitle():String{
        return  title
    }
    @JvmName("setTitle1")
    fun setTitle(title: String){
        this.title =title
    }

    @JvmName("getPrice1")
    fun getPrice():Double{
        return  price
    }

    @JvmName("setPrice1")
    fun setPrice(price: Double){
        this.price = price
    }

    @JvmName("getDescription1")
    fun getDescription():String{
        return  description
    }
    @JvmName("setDescription1")
    fun setDescription(description: String){
        this.description= description
    }
    @JvmName("getCategory1")
    fun getCategory():String{
        return  category
    }

    @JvmName("setCategory1")
    fun setCategory(category: String){
        this.category= category
    }
    @JvmName("getImage1")
    fun getImages():String{
        return  image
    }

    fun setImages(image: String){
        this.image=image
    }
     @JvmName("getRating1")
     fun getRating():Rating{
         return rating
     }
     class Rating{
         var count: Int?=null
        var rate: Double?=null

        constructor()
        constructor(count:Int,rate:Double){
            this.count=count
            this.rate=rate
        }

         @JvmName("getCount1")
         fun  getCount():Int?{
             return count
         }
         @JvmName("getRate1")
         fun getRate():Double?{
             return rate
         }
    }
}

