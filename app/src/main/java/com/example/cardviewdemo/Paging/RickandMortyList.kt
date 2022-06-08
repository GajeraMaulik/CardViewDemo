package com.example.cardviewdemo.Paging

data class RickandMortyList(
    val info: Info,
    val results: List<CharaterData>
)

data class CharaterData(
    val image: String,
    val name: String,
    val species: String,
)

data class Info(
    val count: Int?,
    val next: String?,
    val pages: String?,
    val prev: String?
)