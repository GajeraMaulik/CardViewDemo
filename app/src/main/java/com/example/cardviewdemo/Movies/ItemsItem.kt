package com.example.cardviewdemo.Movies


data class ItemsItem(
    val description: String,
    val id: Int,
    val img: String,
    val name: String,
    val price: Int,
    val quantity: Int,
)

data class WallpapesImagesItem(
    val characters: ArrayList<Character>,
    val manga_anime: ArrayList<MangaAnime>,
    val popular: ArrayList<Popular>,
)
data class Character(
    var thumbnail: String,
    var title: String,
    var wallpaper_count: String,
    var slug: String,
    var category: String,
)
data class MangaAnime(
    val category: String,
    val slug: String,
    val thumbnail: String,
    val title: String,
    val wallpaper_count: String,
)
data class Popular(
    val category: String,
    val slug: String,
    val thumbnail: String,
    val title: String,
    val wallpaper_count: String,
)
data class Root (
    var art_id :Int,
    var animename: String,
    var arturl: String,
    var sensitivity :Int =0,
    var page:Int=0
    )