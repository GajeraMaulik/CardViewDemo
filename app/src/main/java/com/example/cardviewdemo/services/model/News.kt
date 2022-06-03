package com.example.cardviewdemo.services.model

import com.example.cardviewdemo.services.Xml
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Xml(name = "rss")
 data class News(
    @Element(name = "channel") val  channel: Channel,
    @Attribute(name = "version")val version: String
){

}
