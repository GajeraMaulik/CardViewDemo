package com.example.cardviewdemo.services.model

import android.annotation.SuppressLint
import androidx.annotation.XmlRes
import com.example.cardviewdemo.services.Xml
import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName
import com.tickaroo.tikxml.annotation.PropertyElement
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import retrofit2.http.Tag
import retrofit2.http.Url
import java.util.*
import javax.annotation.PropertyKey

//@Root(name = "item", strict = false)
@Xml(name = "item")
 class Item{
    @PropertyElement(name = "description")
    lateinit var  description: String
    @Element(name="guid")
//    lateinit var guid: Guid
    @PropertyElement(name="link")
    lateinit var link: String
    @PropertyElement(name = "pubDate")
    lateinit var pubDate: String
    @Element(name="source")
    //lateinit var source: Source
    @PropertyElement(name="title")
    lateinit var title: String


    var success = 0
    var error=1


constructor()

constructor(description: String,link: String,pubDate: String,title: String){
    this.description=description
    this.link=link
    this.pubDate=pubDate
  //  this.source=source
    this.title=title
        //this.guid=guid
}
     @JvmName("getDescription1")
     fun getDescription():String{
         return  description
     }

     @JvmName("setDescription1")
     fun setDescription(description: String){
         this.description=description
     }

     @JvmName("getLink1")
     fun getLink():String{
         return  link
     }

     @JvmName("setLink1")
     fun setLink(link: String){
         this.link=link
     }

     fun getPubdate():String{
         return  pubDate
     }
     @JvmName("setPubDate1")
     fun setPubDate(pubDate: String){
         this.pubDate=pubDate
     }
/*
     @JvmName("getSource1")
     fun getSource():Source{
         return  source
     }

     @JvmName("setSource1")
     fun setSource(source: Source){
         this.source=source

     }*/

     @JvmName("getTitle1")
     fun getTitle():String{
         return  title
     }

     @JvmName("setTitle1")
     fun setTitle(title: String){
         this.title=title
     }
}
