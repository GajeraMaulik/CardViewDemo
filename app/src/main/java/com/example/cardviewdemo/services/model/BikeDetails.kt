package com.example.cardviewdemo.services.model

class BikeDetails : ArrayList<BikeDetailsItem>()
data class BikeDetailsItem(
    val bikes: Int,
    val free: Int,
    val id: Int,
    val idx: Int,
    val lat: Int,
    val lng: Int,
    val name: String,
    val number: Int,
    val timestamp: String
)