package com.example.cardviewdemo.services.model

import com.example.cardviewdemo.services.Xml
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root
import retrofit2.http.Url

@Xml(name = "source")
data class Source(
    @Attribute(name = "text") val  text: String,
     @Attribute(name = "url") val url: Url
     ){
}