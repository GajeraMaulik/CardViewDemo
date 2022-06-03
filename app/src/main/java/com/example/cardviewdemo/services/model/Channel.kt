package com.example.cardviewdemo.services.model

import com.example.cardviewdemo.services.Xml
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import org.simpleframework.xml.Root

@Xml(name = "channel")
data class Channel(

    @PropertyElement(name = "copyright") val copyright: String,
    @PropertyElement(name= "description")val description: String,
    @PropertyElement(name= "generator")val generator: String,
    @Element(name = "item")val item: List<Item>,
    @PropertyElement(name= "language")val language: String,
    @PropertyElement(name= "lastBuildDate")val lastBuildDate: String,
    @PropertyElement(name= "link")val link: String,
    @PropertyElement(name= "title")val title: String,
    @PropertyElement(name= "webMaster")val webMaster: String
)